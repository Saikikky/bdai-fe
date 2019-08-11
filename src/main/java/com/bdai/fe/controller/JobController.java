package com.bdai.fe.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bdai.fe.config.StreamTask;
import com.bdai.fe.dto.JobDto;
import com.bdai.fe.dto.ResultCode;
import com.bdai.fe.entity.Flow;
import com.bdai.fe.entity.Job;
import com.bdai.fe.entity.JobDevices;
import com.bdai.fe.mapper.FlowMapper;
import com.bdai.fe.mapper.JobDevicesMapper;
import com.bdai.fe.mapper.JobMapper;
import com.bdai.fe.mapper.SchemaMapper;
import com.bdai.fe.service.*;
import com.bdai.fe.util.JobLazyExecute;
import com.bdai.fe.util.RemoteShellExecutor;
import com.bdai.fe.util.SshUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.bdai.fe.util.Obj2byte;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequestMapping("/preparation/v1")
@RestController
public class JobController {
    private final Log logger = LogFactory.getLog(getClass());
    @Autowired
    SchemaService schemaService;
    @Autowired
    FlowService flowService;
    @Autowired
    JobService jobService;
    @Autowired
    IUtilService iUtilService;
    @Autowired
    SchemaMapper schemaMapper;
    @Autowired
    JobMapper jobMapper;
    @Autowired
    FlowMapper flowMapper;
    @Autowired
    JobDevicesMapper jobDevicesMapper;
    @Autowired
    PrivilegeService privilegeService;
    private ConcurrentHashMap<String, Runnable> taskMap = new ConcurrentHashMap<>();

    private RemoteShellExecutor executor = new RemoteShellExecutor("bdai1", "root", "");

    /**
     * 在返回的json数据中 { "code": 7777, "flows": [ { "mode": "global_static", "method":
     * "fillmissing", "vid": "电压",--------------------------->>>>>>vid属性对应的field
     * "value": { "peak": "200k", "value": "220" } } ], "dataset": "空调1空调2预处理",
     * "device": "1, 2, 3", "tableName": null }
     * ，Value对应代码的value,flows中每个json对应代码的Attribute，总的返回对应代码的parameters
     *
     * @param jsonObject
     * @return
     */
    @PutMapping(value = "/job")
    public Map<String, Object> get(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        Map<String, Object> code = new HashMap<>();
        List<Object> list = new ArrayList<>();
        list.add(2);
        list.add(3);
        String userId = privilegeService.verify(request.getCookies(), list);
        if ("2".equals(userId)) {
            code.put("code", 30501);
            return code;
        }
        String SQLStr = "";
        Job job = new Job();// 存job信息
        Map<String, Object> parameters = new HashMap<>();
        List<Map<String, Object>> flows = new ArrayList<>();// 装每个flow的属性值
        int schema = jsonObject.getInteger("schema");
        String dataset = jsonObject.getString("dataset");// 传入参数2
        String device = jsonObject.getString("devices");// device的所有id，前端传入参数3

        try {
            String method = null;// 处理方法
            Integer deviceType = schemaMapper.getDeviceTypeBySchemaId(schema);// device的id传入参数1-----》拿去找deviceName
            List<Flow> flowBySid = flowMapper.getFlowBySid(schema);

            // 没有id返回30502
            if (flowBySid.size() == 0) {
                code.put("code", 30502);
                return code;
            }
            device = device.substring(1, device.length() - 1);// 截取掉device中的[]

            System.out.println("======传入参数获取的值======\n" + schema + dataset + device);
            int i = 0;// 遍历flow，给每个Map加入一个FLow
            for (Flow flow : flowBySid) {
                /*
                 * 定义Flow(即List)元素的一系列参数
                 */
                String mode = null;// 缺失值填补方法
                Map<String, Object> values = new HashMap<>();// 预处理方法参数
                HashMap<String, Object> attribute = new HashMap<>();// 记录flow的属性
                method = flow.getMethod();
                Integer fill_value;// 填充值
                String field = null;// 属性------------->>>>>变成vid返回
                Integer fieldId;

                attribute.put("method", method);// 添加Flow的处理方法
                Object params = Obj2byte.byte2obj(flow.getParams());// 把byte[]转成object类型的params
                String paramsData = params.toString();
                System.out.println(paramsData);
                paramsData = paramsData.substring(1, paramsData.length() - 1);
                String[] splitDatas = paramsData.split(",");// params里面的参数获取出来
                System.out.println(Arrays.toString(splitDatas));

                for (String data : splitDatas) {
                    String valueAttrName = data.split("=")[0].trim();
                    switch (valueAttrName) {// 取属性比较
                        case "field":
                            field = data.split("=")[1];
                            // attribute.put("vid",field);//------------------------>这里还要根据字段属性查属性ID
                            // 拼上V，所有的返回一个建表的字段
                            // 还是要用的--------》用DM的接口返回id
                            fieldId = jobDevicesMapper.getField_ByDeviceTypeAndFieldName(deviceType, field);
                            attribute.put("vid", "V" + fieldId);
                            break;
                        case "mode":
                            mode = data.split("=")[1];
                            System.out.println("======mode的值======\n" + mode);
                            attribute.put("mode", mode);
                            break;
                        case "fill_value":
                            fill_value = Integer.parseInt(data.split("=")[1].trim());
                            values.put("fill_value", fill_value);
                            break;
                        default:
                            System.out.println("======剩余params里的值======\n" + data);
                            System.out.println("======剩余params里放入values里的值======\n" + valueAttrName + data.split("=")[1]);
                            values.put(valueAttrName, data.split("=")[1]);// 其余所有参数放在value里面
                    }
                    // 如果没有mode字段还是要添加字段保持字段完整
                    if (mode == null || mode.equals(""))
                        attribute.put("mode", "00");
                }
                // if (values.size()!=0)//value里有值
                System.out.println("======一个params里value的值======\n" + values.toString());
                attribute.put("values", values);// value可能有多个参数，都放在一个value里
                System.out.println("======一个params里所有的值======\n" + attribute);
                flows.add(attribute);
            }
            /* 给hive的建表语句 */
            for (Map<String, Object> flow : flows) {
                for (Map.Entry<String, Object> stringObjectEntry : flow.entrySet()) {
                    if (stringObjectEntry.getKey().equals("vid")) {// if (stringObjectEntry.getKey().equals("field"))
                        String devieceType_fieldName = (String) stringObjectEntry.getValue();

                        /*
                         * 在这一句应该去调用DM的GET /devicemanagement/v1/devicetype?ID=XX接口 Integer
                         * devieceType_fieldID =
                         * iUtilService.getField_ByDeviceTypeAndFieldName(request.getCookies(),
                         * deviceType, devieceType_fieldName); 替换下面这句
                         */

                        Integer devieceType_fieldID = jobDevicesMapper.getField_ByDeviceTypeAndFieldName(deviceType,
                                devieceType_fieldName);
                        // 隔离区-----------------
                        SQLStr += devieceType_fieldName + " String,";
                        // SQLStr += "V"+ devieceType_fieldID+ " String,";
                    }
                }
            }
            //----改调用jobService.jobSave
            //jobService.jobSave(schema,Integer.parseInt(userId.trim()),dataset,device);,暂弃不用

            job.setSid(schema);
            job.setExcutor_id(Integer.parseInt(userId));
            job.setDataset_name(dataset);
            System.out.println("=======job的信息=======");
            System.out.println(job.toString());
            jobMapper.jobSave(job);
            System.out.println("========job的信息和job的id============");
            System.out.println(job.toString() + "---------" + job.getId());
            String[] deviceToTable = device.split(",");
            for (String deviceId : deviceToTable) {
                JobDevices jobDevices = new JobDevices();
                jobDevices.setDevice_id(Integer.parseInt(deviceId.trim()));
                jobDevices.setJob_id(job.getId());
                System.out.println("=======jobDevice的信息=======");
                System.out.println(jobDevices.toString());
                jobDevicesMapper.jobDeviceSave(jobDevices);
            }
            //-------------------到上面的

            SQLStr = SQLStr.substring(0, SQLStr.length() - 1);
            //把对象放入对象列表中
            parameters.put("dataset", dataset);
            parameters.put("jobId", job.getId());
            parameters.put("deviceId", device);//parameters.put("device",device);
            parameters.put("table", "T" + deviceType.toString());//parameters.put("deviceType",deviceType);
            parameters.put("list", flows);//parameters.put("flows",flows);
            parameters.put("table_columns", SQLStr);//parameters.put("SQL",SQLStr);

            Object jsonParameters = JSONObject.toJSON(parameters);
            jsonParameters = "'" + jsonParameters + "'";
            System.out.println(jsonParameters);//加了单引号的结果


            //执行python脚本,传入参数依次是fields,mode和value
            String cmd = "source /etc/profile\npython3 /py_code/preprocessing.py " + jsonParameters;

            //更新状态码 0 开始执行 1执行成功 2执行失败 1由python脚本进行更新
            jobMapper.updateStatus(0, job.getId());
            //executor.exec(cmd);改由另起一个线程执行
            Runnable jobLazyExecute = new JobLazyExecute(jobMapper, executor, cmd, job.getId());
            new Thread(jobLazyExecute, "执行python脚本").start();
            code.put("code", 0);//错误返回
            //return parameters;

            return code;

        } catch (Exception e) {
            jobMapper.updateStatus(2, job.getId());
            e.printStackTrace();
        }
        code.put("code", 30500);// 错误返回
        // return parameters;
        return code;
    }

    @GetMapping("/job")
    public JobDto getJobs(HttpServletRequest request) {
        List<Object> list = new ArrayList<>();
        list.add(2);
        list.add(3);
        String username = privilegeService.verify(request.getCookies(), list);
        if ("2".equals(username)) {
            return new JobDto(30601, null);
        }
        JobDto jobs = jobService.getJobs();
        return jobs;
    }

    @GetMapping("/job/{id}")
    public JobDto getJobs(@PathVariable("id") Integer id, HttpServletRequest request) {
        List<Object> list = new ArrayList<>();
        list.add(2);
        list.add(3);
        String username = privilegeService.verify(request.getCookies(), list);
        if ("2".equals(username)) {
            return new JobDto(30601, null);
        }
        JobDto jobs = jobService.getJobs(id);
        return jobs;
    }

    @DeleteMapping("/job/{id}")
    public ResultCode deleteSchemaById(@PathVariable("id") Integer id, HttpServletRequest request) {
        List<Object> list = new ArrayList<>();
        list.add(2);
        list.add(3);
        String username = privilegeService.verify(request.getCookies(), list);
        if ("2".equals(username)) {
            return new ResultCode(30701);
        }

        ResultCode resultCode = jobService.deleJobById(id);
        return resultCode;
    }

    @PostMapping("/job/stream")
    public Map<String, Object> jobStreamStart(HttpServletRequest request) {
        String ip = request.getParameter("ip");
        String port = request.getParameter("port");
        String topic = request.getParameter("topic");
        String method = request.getParameter("method");
        String cmd = ip + " " + port + " " + topic + " " + method;
        logger.debug("传入参数：" + cmd);
//        RemoteShellExecutor executor = new RemoteShellExecutor("115.156.128.241", "root", "root");
        Map<String, Object> result = new HashMap<>();
        try {
            logger.debug("开始执行");
            StreamTask streamTask = new StreamTask(cmd);
            Thread t = new Thread(streamTask);
            t.start();
            taskMap.putIfAbsent(ip + "," + port + "," + topic + "," + method, streamTask);
            logger.debug("执行完毕");
        } catch (Exception e) {
            logger.debug("!!!!!!!!数据流脚本启动失败!!!!!!!!!!!!");
            e.printStackTrace();
            result.put("code", 30802);
            return result;
        }
        result.put("code", 0);
        return result;
    }

    @GetMapping("/job/stopstream")
    public Map<String, Object> jobStreamStop(HttpServletRequest request) {
        //
        String ip = request.getParameter("ip");
        String port = request.getParameter("port");
        String topic = request.getParameter("topic");
        String method = request.getParameter("method");
        String cmd = ip + " " + port + " " + topic + " " + method;
        logger.debug("传入参数：" + cmd);
        StreamTask streamTask = (StreamTask) taskMap.get(ip + "," + port + "," + topic + "," + method);
        taskMap.remove(ip + "," + port + "," + topic + "," + method);
        Map<String, Object> result = new HashMap<>();
        if (streamTask != null) {
            streamTask.stop();
            result.put("code", 0);
        } else {
            result.put("code", -1);
        }
        return result;
    }

}
