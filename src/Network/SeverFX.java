package Network;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SeverFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        new Thread(() -> {
                try {
                    ArrayList<Sever> mySevers = new ArrayList<>();
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
        );
        //primaryStage.setScene(new Scene(mySeverCanvas.getCanvas()));
        primaryStage.show();
    }
}
