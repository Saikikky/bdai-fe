package com.bdai.fe.util;

import com.jcraft.jsch.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
 * 这里账号密码端口以及host后面可以用配置文件管理,spring注入即可。
 * */
public class SshUtil {
    private static String userName = "root";
    private static int port = 22;
    private static String host = "bdai1";
    private static String keyPath = "/root/.ssh/id_rsa";
    private Session session;
    private ChannelExec channelExec;
    private final static Log logger = LogFactory.getLog(SshUtil.class);

    public void getConnection() throws JSchException {
        logger.debug("SshUtil getConnection v2");
        JSch jSch = new JSch(); // 创建JSch对象
        jSch.addIdentity(keyPath);
        session = jSch.getSession(userName, host, port);// 根据用户名，主机ip和端口获取一个Session对象
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);// 为Session对象设置properties
        session.setTimeout(30000);// 设置超时
        session.connect();// 通过Session建立连接
        logger.debug("SshUtil connect open");
    }

    public void close() {
        logger.debug("SshUtil connect close");
        session.disconnect();
    }


    public void executeCommand(String command) throws JSchException, IOException {
        logger.debug("method executeCommand() :" + command);
        channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);
        channelExec.setInputStream(null);
        channelExec.setErrStream(System.err);
        InputStream in = channelExec.getInputStream();
        channelExec.connect();
        logger.debug("connect success");
        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0)
                    break;
                System.out.print(new String(tmp, 0, i));
            }
            if (channelExec.isClosed()) {
                if (in.available() > 0)
                    continue;
                logger.debug("exit-status: " + channelExec.getExitStatus());
                break;
            }
        }
        channelExec.disconnect();
    }

}
