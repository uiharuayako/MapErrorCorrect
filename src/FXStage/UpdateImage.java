package FXStage;

import java.io.*;
import java.net.Socket;

public class UpdateImage extends Thread {
    private Socket myS;
    File file = new File("./我的作品/" + MyStatus.mapName + "AutoSave.png");
    int file_size;// 文件大小
    FileOutputStream fos = null;
    InputStream is = null;
    byte[] buffer = new byte[4096 * 2];
    String comm = "";// 接受指令
    // 用户相关
    String ID;
    String name;
    // 名单相关
    NameList tools = new NameList();// 这个用于维护myNamelist这个表

    UpdateImage(Socket s) {
        myS = s;
    }

    // 退出相关
    boolean exit = false;

    public void exitThread() {
        exit = true;
    }

    @Override
    public void run() {
        while (!exit) {
            comm = "";
            System.out.println("10010");
            // 读取命令
            try {
                InputStreamReader isr = new InputStreamReader(myS.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                comm = br.readLine();
            } catch (IOException e) {
                System.out.println("服务器与客户端断开连接");
            }
            System.out.println("get comm100" + comm);
            // 解析协议
            int index = comm.indexOf("$");
            String protocol = comm.substring(0, index);
            // 解析id
            comm = comm.substring(index + 1);
            index = comm.indexOf("$");
            ID = comm.substring(0, index).trim();
            if (protocol.equals("join")) {
                name = comm.substring(index + 1);// 获取姓名
                System.out.println("get name");
                //tools.addPeople(name, ID);
            }
            if (protocol.equals("stop")) {
                tools.deletePeople(ID);// 删掉
            }
            if (protocol.equals("sync")) {
                try {
                    Thread.sleep(50);
                    // 解析长度
                    comm = comm.substring(index + 1);
                    index = comm.indexOf("$");
                    String filesize = comm.substring(0, index).trim();
                    String toolname = comm.substring(index + 1);
                    System.out.println("得到工具信息" + toolname);
                    // 输出流
                    fos = new FileOutputStream(file);
                    file_size = Integer.parseInt(filesize);
                    is = myS.getInputStream();
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
                    while (count < file_size) {
                        //从输入流中读取一个数据包
                        size = is.read(buffer);

                        //将刚刚读取的数据包写到本地文件中去
                        fos.write(buffer, 0, size);
                        fos.flush();

                        //将已接收到文件的长度+size
                        count += size;
                        System.out.println("服务器端接收到数据包，大小为" + size);
                        MyStatus.isUpdate = true;
                    }
                    System.out.println("end");
                } catch (FileNotFoundException e) {
                    System.out.println("服务器写文件失败");
                } catch (Exception e) {
                    System.out.println("服务器：客户端断开连接");
                }
            }
        }
    }
}
