# -*- coding: utf-8 -*
from pyspark.sql import SparkSession
import numpy as np
import uuid
from sklearn import preprocessing
from sklearn.preprocessing import PolynomialFeatures, Imputer
from sklearn.impute import SimpleImputer
from pyspark import SparkContext
from pyspark.sql import SQLContext
from sklearn.externals import joblib
import os
import sys
import json
import pymysql

sc = SparkContext()

sparkSession = SparkSession.builder \
    .appName('compute_customer_age') \
    .config('spark.executor.memory', '2g') \
    .enableHiveSupport() \
    .getOrCreate()



def init(tablename, vid):
    global sqlContext
    str=''
    if tablename + "_1" in sqlContext.tableNames("etl"):
        sql1 = "select " + vid + " from " + tablename + "_1 where deviceId in (" + deviceId + ")"
        print(sql1)
        df1 = sparkSession.sql(sql1)
        rdd_val1 = df1.replace(to_replace='',value='nan').rdd
        np_val1 = np.array(rdd_val1.collect())  # 把rdd转成numpy

        # str=str[0:-1]
        # np_val1 = np.fromstring(str,dtype=np.int8,sep=',')
        print('===========================np.val1')
        print(np_val1)
    if tablename + "_2" in sqlContext.tableNames("etl"):
        sql2 = "select " + vid + " from " + tablename + "_2 where deviceId in (" + deviceId + ")"
        print(sql2)
        df2 = sparkSession.sql(sql2)
        rdd_val2 = df2.replace(to_replace='',value='nan').rdd
        # rdd_val2 = df2.replace(to_replace=r'^\s*$', value='np.nan').rdd
        np_val2 = np.array(rdd_val2.collect())  # 把rdd转成numpy
        # np_val2 = np.fromstring(np_val2, dtype=np.float)
        print('===========================np.val2')
        print(np_val2)
    if tablename + "_3" in sqlContext.tableNames("etl"):
        sql3 = "select " + vid + " from " + tablename + "_3 where deviceId in (" + deviceId + ")"
        print(sql3)
        df3 = sparkSession.sql(sql3)
        rdd_val3 = df3.replace(to_replace='',value='nan').rdd
        np_val3 = np.array(rdd_val3.collect())  # 把rdd转成numpy
        # np_val3 = np.fromstring(np_val3, dtype=np.float)

        print('===========================np.val3')
        print(np_val3)


    if tablename + "_1" in sqlContext.tableNames("etl"):
        if tablename + "_2" in sqlContext.tableNames("etl"):
            if tablename + "_3" in sqlContext.tableNames("etl"):
                np_val = np.concatenate([np_val1, np_val2, np_val3], axis=0)
                print('=============Line:50')
                print(np_val)
                print('=============Line:52')
            else:
                np_val = np.concatenate([np_val1, np_val2], axis=0)
        else:
            if tablename + "_3" in sqlContext.tableNames("etl"):
                np_val = np.concatenate([np_val1, np_val3], axis=0)
    else:
        if tablename + "_2" in sqlContext.tableNames("etl"):
            if tablename + "_3" in sqlContext.tableNames("etl"):
                np_val = np.concatenate([np_val2, np_val3], axis=0)
            else:
                np_val = np_val2
        else:
            if tablename + "_3" in sqlContext.tableNames("etl"):
                np_val = np_val3
    return np_val

# 标准化
def scale(tablename, vid):
    np_val = init(tablename, vid)
    print(np_val)
    np_val = np_val.astype(np.object)
    val_scale = preprocessing.scale(np_val)
    print(val_scale)

    global result_data
    result_data = np.concatenate([result_data, val_scale], axis=1)
    print(result_data)
    joblib.dump(val_scale, '/py_code/model/scale.pkl', compress=3)


# 归一化
def minmaxscaler(tablename, vid):
    np_val = init(tablename, vid)

    min_max_scaler = preprocessing.MinMaxScaler()

    np_val = np_val.astype(np.object)
    X_train_minmax = min_max_scaler.fit_transform(np_val)

    # 处理数组
    global result_data
    result_data = np.concatenate([result_data, X_train_minmax], axis=1)

    # 保存
    joblib.dump(min_max_scaler, '/py_code/model/minmaxscaler.pkl', compress=3)




#二值化
def binarizer(tablename, values, vid):
    np_val = init(tablename, vid)

    np_val = np_val.astype(np.object)
    threshold = values["threshold"]
    threshold = float(threshold)
    binarizer = preprocessing.Binarizer(threshold=threshold)
    binarizer_result = binarizer.fit_transform(np_val.astype(np.float))

    # 处理数组
    global result_data
    result_data = np.concatenate([result_data, binarizer_result], axis=1)

    # 保存
    joblib.dump(binarizer, '/py_code/model/binarizer.pkl', compress=3)


# def test(tablename):
#     np_val = init(tablename)
#     simp = joblib.load('/py_code/model/imp.pkl')
#     x = simp.fit_transform(np_val)
#     print(x)

'''
def polynomialFeatures(tablename, values, vid):
    np_val = init(tablename, vid)
    print("np_val")
    print(np_val)
    degree = values["degree"]
    poly = PolynomialFeatures(degree)
    poly_result = poly.fit_transform(np_val)
    print("poly_result")
    print(poly_result)

    # 处理数组
    global result_data
    result_data = np.concatenate([result_data, poly_result], axis=1)

    # 保存
    joblib.dump(poly, '/py_code/model/poly.pkl', compress=3)
'''



def simpleImputer (tablename, values, vid, mode):
    np_val = init(tablename, vid)

    missing_values = values["missing_values"]
    strategy = mode
    if strategy == "constant":
        fill_value = values["fill_value"]
        fill_value = float(fill_value)

    if strategy == "constant":
        if missing_values == "np.nan":
            simp = SimpleImputer(missing_values='nan', strategy=strategy, fill_value=fill_value)
        else:
            simp = SimpleImputer(missing_values=float(missing_values), strategy=strategy, fill_value=fill_value)
    else:
        if missing_values == "np.nan":
            simp = SimpleImputer(missing_values=np.NaN, strategy=strategy)
        else:
            simp = SimpleImputer(missing_values=float(missing_values), strategy=strategy)


    # print(np_val.dtype)
    if strategy=='constant':
        np_val=np_val.astype(np.object)
    else:
        np_val=np_val.astype(np.float)

    simpleImputer_result = simp.fit_transform(np_val)
    print("simpleImputer_result")
    print(simpleImputer_result)

    # 处理数组
    global result_data
    result_data = np.concatenate([result_data, simpleImputer_result], axis=1)
    print("============simpleImputer/result_data: Line:164================")
    print(result_data)

    # 保存
    joblib.dump(simp, '/py_code/model/simp.pkl', compress=3)



if __name__ == "__main__":

    sql1 = """
              use etl
              """
    sparkSession.sql(sql1)

    sqlContext = SQLContext(sc)

    jsonString = sys.argv[1]
    print("jsonString" + jsonString)
    jsonParams = json.loads(jsonString)
    # 获取参数列表
    jsonLists = jsonParams["list"]

    table_columns = jsonParams["table_columns"]
    sqlString = ""+table_columns + ") row format delimited fields terminated by ',' " \
                                                 "location 'hdfs://bdai1:9000/user/hive/"

    # 获得搜寻数据的表
    table = jsonParams["table"].lower()
    # 处理完之后数据存储的表名
    dataset = jsonParams["dataset"]
    deviceId = jsonParams["deviceId"]
    # jobId  = jsonParams["jobId"]
    jobId = jsonParams["jobId"]

    # 只有第一次需要对表进行初始化

    i = 0
    for jsonlist in jsonLists:
        # 获得预处理方法
        method = jsonlist["method"]

        # 获得需要进行预处理的数据属性
        vid = jsonlist["vid"]
        values = jsonlist["values"]
        # 用该参数去选择具体预处理方法 若该参数为空则去选择method来选择具体预处理方法
        mode = jsonlist["mode"]

        if i == 0:

            # 初始化处理完的表第一行
            if table + "_1" in sqlContext.tableNames("etl"):
                sqlInit1 = "select 1 from " + table + "_1 where deviceId in (" + deviceId + ")"
                df1 = sparkSession.sql(sqlInit1)
                rdd_init1 = df1.rdd
                result_data1 = np.array(rdd_init1.collect())  # 把rdd转成numpy
                print('====================size1')
                print(result_data1.size)

            if table + "_2" in sqlContext.tableNames("etl"):
                sqlInit2 = "select 1 from " + table + "_2 where deviceId in (" + deviceId + ")"
                df2 = sparkSession.sql(sqlInit2)
                rdd_init2 = df2.rdd
                result_data2 = np.array(rdd_init2.collect())  # 把rdd转成numpy
                print('====================size2')
                print(result_data2.size)
            if table + "_3" in sqlContext.tableNames("etl"):
                sqlInit3 = "select 1 from " + table + "_3 where deviceId in (" + deviceId + ")"
                df3 = sparkSession.sql(sqlInit3)
                rdd_init3 = df3.rdd
                result_data3 = np.array(rdd_init3.collect())  # 把rdd转成numpy
                print('====================size3')
                print(result_data3.size)

            if table + "_1" in sqlContext.tableNames("etl"):
                if table + "_2" in sqlContext.tableNames("etl"):
                    if table + "_3" in sqlContext.tableNames("etl"):
                        print('=======123=======')
                        result_data = np.concatenate([result_data1, result_data2, result_data3], axis=0)
                    else:
                        print('=======12=======')
                        result_data = np.concatenate([result_data1, result_data2], axis=1)
                else:
                    if table + "_3" in sqlContext.tableNames("etl"):
                        print('=======13=======')
                        result_data = np.concatenate([result_data1, result_data3], axis=0)
            else:
                if table + "_2" in sqlContext.tableNames("etl"):
                    if table + "_3" in sqlContext.tableNames("etl"):
                        print('=======23=======')
                        result_data = np.concatenate([result_data2, result_data3], axis=0)
                    else:
                        print('=======2=======')
                        result_data = result_data2
                else:
                    if table + "_3" in sqlContext.tableNames("etl"):
                        print('=======3=======')
                        result_data = result_data3

            print('===============Line:256==================')
            print(result_data.size)

        # 选择用哪个函数运行
        if (mode == "constant") or (mode == "mean") or (mode == "median") or (mode == "most_frequent"):
            simpleImputer(table, values, vid, mode)
        else:
            if method == "scale":
                scale(table, vid)
            elif method == "minmaxscaler":
                minmaxscaler(table, vid)
            elif method == "binarizer":
                binarizer(table, values, vid)
            elif method == "simpleImputer":
                simpleImputer(table, values, vid)
        i = 1

    print("===========finally result_data===========")
    print(result_data)
    tag = str(uuid.uuid1()) + table + ".csv"
    result_data=result_data.astype(np.float)

    #去除无效的第一列
    result_data = np.array([[row[i] for i in range(0, result_data[0].size) if i != 0] for row in result_data])
    print("=================result_data================")
    print(result_data)

    file = np.savetxt("/py_code/hive/" + tag, result_data, fmt="%.6f", delimiter=',')

    if dataset not in sqlContext.tableNames("etl"):
        sql = "create external table " + dataset + "(" + sqlString + dataset + "'"
        print(sql)
        sparkSession.sql(sql)
    os.system('source /etc/profile\nsh /py_code/file_move.sh ' +dataset + ' ' + tag)

    # 打开数据库连接
    db = pymysql.connect(host="db", user="root", passwd="bdaipass", port=3306, db="whxlbdai")

    # 使用cursor()方法获取操作游标
    cursor = db.cursor()

    # SQL 更新语句
    print("===========updatestart===========")
    sql = "UPDATE job SET progress = 1 WHERE id = '%d'" % (jobId)
    try:
        # 执行SQL语句
        cursor.execute(sql)
        # 提交到数据库执行
        db.commit()
    except:
        # 发生错误时回滚
        db.rollback()

    # 关闭数据库连接
    db.close()