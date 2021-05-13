package Network;

import FXStage.MyStatus;

import java.io.*;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

public class Sever implements Runnable {
    // 建立连接
    Socket s = null;
    // 文件大小
    String comm;// 接受指令
    // 用户相关
    String ID;
    String name;
    String mapName;
    // 标志进程是否结束
    boolean exit = false;

    Sever(Socket mySS) {
        s = mySS;
    }

    @Override
    public void run() {
        while (!exit) {
            comm = null;
            System.out.println("start");
            // 读取命令
            try {
                InputStreamReader isr = new InputStreamReader(s.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                comm = br.readLine();
            } catch (IOException e) {
                System.out.println("服务器与客户端断开连接");
            }
            System.out.println("get comm" + comm);
            // 解析协议
            int index = comm.indexOf("$");
            String protocol = comm.substring(0, index);
            // 解析名称
            comm = comm.substring(index + 1);
            index = comm.indexOf("$");
            name = comm.substring(0, index).trim();
            try {
                PreparedStatement ps = MyStatus.myCon.prepareStatement("select workMapName from user where name = '" + name + "'");
                ResultSet rs= ps.executeQuery();
                while (rs.next()){
                    mapName = rs.getString(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ("sync".equals(protocol)) {
                // 获取姓名
                name = comm.substring(index + 1);
                for (int i = 0; i < SeverApp.mySevers.size(); i++) {
                    Sever thisSever = SeverApp.mySevers.get(i);
                    System.out.println("当前名称：" + name);
                    System.out.println("对方名称：" + thisSever.name);
                    String thisMapName = null;
                    try {
                        PreparedStatement ps = MyStatus.myCon.prepareStatement("select workMapName from user where name = '" + thisSever.name + "'");
                        ResultSet rs= ps.executeQuery();
                        while (rs.next()){
                            thisMapName = rs.getString(1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 如果当前地图名，和对方地图名相同
                    if (!Objects.equals(thisMapName, mapName)) {
                        // 发送消息
                        thisSever.sendMessage();
                    } else {
                        System.out.println("同id，不发送");
                    }
                }
            }
        }
    }

    void sendMessage() {
        try {
            PrintStream myPS = new PrintStream(s.getOutputStream());
            myPS.println("sync$");
            myPS.flush();
            Thread.sleep(50);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
