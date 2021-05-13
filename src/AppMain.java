import FXStage.*;
import icon.IconImage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

//程序入口
public class AppMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 连接数据库
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            MyStatus.myCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/?serverTimezone=UTC",
                    "root",
                    "Test123456");
            MyStatus.myStm = MyStatus.myCon.createStatement();
            MyStatus.myStm.execute("CREATE DATABASE IF NOT EXISTS MapErrorDB");
            MyStatus.myStm.execute("USE MapErrorDB");
            MyStatus.myStm.execute("CREATE TABLE IF NOT EXISTS User(name char(50),pwMD5 char(32),id timestamp,online bit,workMapName char(50))");
            MyStatus.myStm.execute("CREATE TABLE IF NOT EXISTS Pics(fileName char(50),imgData mediumblob,fileErrors mediumtext)");
            MyStatus.myStm.execute("CREATE TABLE IF NOT EXISTS Logs(userName char(50),time timestamp,infoLine text)");
        }catch (SQLException e){
            e.printStackTrace();
        }
        //以下是有关目录的创建
        File pics = new File("./我的作品");
        if (!pics.exists()) {
            pics.mkdir();
        }
        //以下是主要组件的添加
        primaryStage.getIcons().add(IconImage.getImage("ICON"));
        ToolsPane tools = new ToolsPane();
        MyEditBar myEditBar = new MyEditBar();
        MyNetBar myNetBar = new MyNetBar();
        MyCanvas canvas = new MyCanvas(myEditBar, myNetBar);
        MyMenuBar menuBar = new MyMenuBar(canvas);
        BorderPane bp = new BorderPane();
        //把netbar和editbar整合到Tab中
        TabPane rightTabs = new TabPane();
        Tab editTab = new Tab("信息编辑");
        editTab.setContent(myEditBar.getRoot());
        editTab.setClosable(false);
        Tab netTab = new Tab("网络");
        netTab.setContent(myNetBar.getRoot());
        netTab.setClosable(false);
        rightTabs.getTabs().addAll(editTab, netTab);
        bp.setTop(menuBar.getMenu());
        bp.setLeft(tools.getToolsPane());
        bp.setCenter(canvas.getCanvas());
        bp.setBottom(canvas.getStatusBar());
        bp.setRight(rightTabs);
        //以下是必要的附加代码
        primaryStage.setScene(new Scene(bp));
        primaryStage.setTitle("地图错误改正工具 beta 0.1.0 -by Uiharu");
        primaryStage.show();
        primaryStage.setOnHiding( event -> {MyDBProcess.setOnline(false);} );
    }


}