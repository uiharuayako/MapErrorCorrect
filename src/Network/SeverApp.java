package Network;

import javafx.embed.swing.JFXPanel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SeverApp {
    public static ArrayList<Sever> mySevers = new ArrayList<>();
    public static void main(String args[]) {
        try {
            ServerSocket ss = new ServerSocket(4004);
            ExecutorService pool = Executors.newFixedThreadPool(3);
            while (true) {
                Socket s = ss.accept();
                Sever tmp = new Sever(s);
                pool.execute(tmp);
                mySevers.add(tmp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
