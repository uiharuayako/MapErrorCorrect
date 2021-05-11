import FXStage.*;
import icon.IconImage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;

//程序入口
public class AppMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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

    }


}