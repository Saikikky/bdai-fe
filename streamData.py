# -*- coding: utf-8 -*
import numpy as np
from sklearn.externals import joblib
from sklearn import preprocessing
from kafka import KafkaConsumer, KafkaProducer
import sys
import json

if __name__ == "__main__":

    while (True):
        # ip
        ip = sys.argv[1]
        # port
        port = sys.argv[2]
        # topic
        topic = sys.argv[3]
        # method
        method = sys.argv[4]

        '''去kafka拉取数据并解析'''
        group_id = "preing" + ip + port + topic
        consumer = KafkaConsumer(topic, group_id=group_id, bootstrap_servers=ip+":"+port)

        print("come in")
        print(consumer)
        for msg in consumer:
            # 每一条msg都是一个"1,2,3,4"类型字符串
            print(msg)
            m = str(msg.value)
            m = m[2:len(m) - 1].split(',')
            print(m)
            b = []
            for a in m:
                b.append(float(a))
            print(b)
            d = np.array(b)
            np_val = np.ndarray(shape=(1, len(d)), buffer=d)
            print("np_val")
            print(np_val)
            '''将kafka拉取的数据后进行预处理'''
            if method == "scale":
                data = preprocessing.scale(np_val)
            else:
                # 保存的路径
                address = '/py_code/model/'
                simp = joblib.load(address + method + '.pkl')
                data = simp.fit_transform(np_val)
            datalist = list(data)
            data = ''.join([str(a) for a in datalist])
            print(datalist)
            '''将预处理之后的数据处理之后扔进kafka生产者'''
            producer = KafkaProducer(bootstrap_servers=[ip + ':' + port])
            pretopic = "pre" + topic
            future = producer.send(pretopic, value=bytes(data, encoding="utf-8"))
            # 函数等待单条消息发送完成或超时
            result = future.get(timeout=10)
            print(result)
