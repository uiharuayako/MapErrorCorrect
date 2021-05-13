package FXStage;

import java.io.*;
import java.net.Socket;

/**
 * @author Ayako
 */
public class UpdateImage extends Thread {
    private Socket myS;
    String comm = "";// 接受指令
    // 用户相关
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
            // 读取命令
            try {
                InputStreamReader isr = new InputStreamReader(myS.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                comm = br.readLine();
            } catch (IOException e) {
                System.out.println("服务器与客户端断开连接");
            }
            // 解析协议
            int index = comm.indexOf("$");
            String protocol = comm.substring(0, index);
            if (protocol.equals("sync")) {
                // 该更新了
                MyStatus.isUpdate=true;
            }
        }
    }
}
