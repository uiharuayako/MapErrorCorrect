package FXStage;

import java.io.*;
import java.net.Socket;

/**
 * @author Ayako
 */
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
            }
        }
    }
}
