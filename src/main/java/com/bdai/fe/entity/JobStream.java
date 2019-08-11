package com.bdai.fe.entity;

public class JobStream {
    private String ip;
    private String port;
    private String topic;
    private String pid;

    public JobStream() {
    }

    public JobStream(String ip, String port, String topic, String pid) {
        this.ip = ip;
        this.port = port;
        this.topic = topic;
        this.pid = pid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "JobStream{" +
                "ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", topic='" + topic + '\'' +
                ", pid='" + pid + '\'' +
                '}';
    }
}
