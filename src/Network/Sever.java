package Network;

import FXStage.MyStatus;

import java.io.*;
import java.net.Socket;

public class Sever implements Runnable {
    // 建立连接
    Socket s = null;

    // 文件相关
    File file = new File("./服务器/AutoSave.png");
    int file_size;
    // 文件大小
    FileOutputStream fos = null;
    InputStream is = null;
    byte[] buffer = new byte[4096 * 2];// 缓存
    String comm;// 接受指令
    // 用户相关
    String ID;
    String name;
    // 标志进程是否结实
    boolean exit = false;

    Sever(Socket mySS) {
        s = mySS;
    }

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
            if (protocol.equals("sync")) {
                try {
                    // 解析长度
                    comm = comm.substring(index + 1);
                    index = comm.indexOf("$");
                    String filesize = comm.substring(0, index).trim();
                    // 输出流
                    fos = new FileOutputStream(file);
                    file_size = Integer.parseInt(filesize);
                    is = s.getInputStream();
                    //size为每次接收数据包的长度
                    int size = 0;
                    long count = 0;
                    byte[] b1 = new byte[file_size];
                    //**使用while循环接收数据包*//*
/*                    do{
                        fos.write(buffer, 0, size);
                        count += size;
                        fos.flush();
                        System.out.println("服务器端接收到数据包，大小为" + size);
                    }while(count-36 < file_size*//*(size = is.read(buffer)) != -1*//*);*/
                    Thread.sleep(50);
                    while (count < file_size) {
                        //从输入流中读取一个数据包
                        size = is.read(buffer);

                        //将刚刚读取的数据包写到本地文件中去
                        fos.write(buffer, 0, size);
                        fos.flush();

                        //将已接收到文件的长度+size
                        count += size;
                        System.out.println("服务器端接收到数据包，大小为" + size);
                    }
                    System.out.println("end");
                    for (int i = 0; i < SeverApp.mySevers.size(); i++) {
                        Sever thisSever = SeverApp.mySevers.get(i);
                        System.out.println("当前id" + ID);
                        System.out.println("get id" + thisSever.ID);
                        if (!thisSever.ID.equals(ID)) {
                            thisSever.sendImage();
                        } else {
                            System.out.println("同id，不发送");
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("服务器写文件失败");
                } catch (Exception e) {
                    System.out.println("服务器：客户端断开连接");
                }
            }
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
                myPS.println("stop$" + newID);
            } else {
                myPS.println("join$" + newID + "$" + newName);
            }
            myPS.flush();
            Thread.sleep(50);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    void sendImage() {
        buffer = new byte[4096 * 10];
        try {
            File newImage = new File("./服务器/AutoSave.png");
            FileInputStream fis = new FileInputStream(newImage);
            byte[] buffer = new byte[4096 * 2];
            PrintStream myPS = new PrintStream(s.getOutputStream());
            // 命令格式：sync$id$文件长度$当前工具
            //         join$id$你的昵称
            //         stop$id
            myPS.println("sync$" + ID + "$" + fis.available() + "$" + "end");
            myPS.flush();
            Thread.sleep(100);
            OutputStream os = s.getOutputStream();
            int size = 0;
            while ((size = fis.read(buffer)) != -1) {
                os.write(buffer, 0, size);
                os.flush();
            }
            Thread.sleep(20);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
