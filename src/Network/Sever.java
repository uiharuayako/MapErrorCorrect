package Network;

import java.io.*;
import java.net.Socket;

public class Sever implements Runnable {
    // 建立连接
    Socket s = null;
    // 文件大小
    String comm;// 接受指令
    // 用户相关
    String ID;
    String name;
    // 标志进程是否结束
    boolean exit = false;

    Sever(Socket mySS) {
        s = mySS;
    }

    @Override
    public void run() {
        for (Sever thisSever : SeverApp.mySevers) {
            try {
                if(!thisSever.ID.equals(ID)) {
                    PrintStream myPS = new PrintStream(s.getOutputStream());
                    // 命令格式：sync$id$文件长度$当前工具
                    //         join$id$你的昵称
                    //         stop$id
                    myPS.println("join$" + thisSever.ID + "$" + thisSever.name);
                    myPS.flush();
                    Thread.sleep(50);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
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
            // 解析id
            comm = comm.substring(index + 1);
            index = comm.indexOf("$");
            ID = comm.substring(0, index).trim();
            if (protocol.equals("join")) {
                name = comm.substring(index + 1);// 获取姓名
                for (int i = 0; i < SeverApp.mySevers.size(); i++) {
                    Sever thisSever = SeverApp.mySevers.get(i);
                    System.out.println("当前id" + ID);
                    System.out.println("get id" + thisSever.ID);
                    if (!thisSever.ID.equals(ID)) {
                        thisSever.sendMessage(ID,name,0);
                    } else {
                        System.out.println("同id，不发送");
                    }
                }
            }
            if (protocol.equals("stop")) {
                exit = true;
                for (Sever thisSever : SeverApp.mySevers) {
                    System.out.println("当前id" + ID);
                    System.out.println("get id" + thisSever.ID);
                    if (!thisSever.ID.equals(ID)) {
                        thisSever.sendMessage(ID,name,1);
                    } else {
                        System.out.println("同id，不发送");
                    }
                }
                try {
                    s.close();
                } catch (Exception e) {
                }
            }
        }
    }

    void sendMessage(String newID,String newName,int cho) {
        try {
            PrintStream myPS = new PrintStream(s.getOutputStream());
            // 命令格式：sync$id$文件长度$当前工具
            //         join$id$你的昵称
            //         stop$id
            if (cho == 1) {
                myPS.println("stop$" + newName);
            } else {
                myPS.println("join$" + newID + "$" + newName);
            }
            myPS.flush();
            Thread.sleep(50);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
