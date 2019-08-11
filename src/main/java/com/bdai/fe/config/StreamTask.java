package com.bdai.fe.config;

import com.bdai.fe.util.SshUtil;
import com.jcraft.jsch.JSchException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;


public class StreamTask implements Runnable {
    private SshUtil sshUtil = new SshUtil();
    private String cmd;
    private final Log logger = LogFactory.getLog(getClass());

    public StreamTask(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public void run() {
        logger.debug("start StreamTask cmd : " + "source /etc/profile\n  python3 /py_code/streamData.py " + cmd);
        try {
            sshUtil.getConnection();
            sshUtil.executeCommand("source /etc/profile\n  python3 /py_code/streamData.py " + cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("end StreamTask");
    }

    public void stop()  {
        logger.debug("stop StreamTask");
        try {// 115.156.128.241 9094 mkmk scale
            sshUtil.executeCommand("ps -ef|grep \"streamData.py "+cmd+"\"|grep -v grep|cut -c 9-15 |xargs kill -9");
        } catch (Exception e) {
            e.printStackTrace();
        }
        sshUtil.close();
        sshUtil = null;
        logger.debug("stop success");
    }
}
