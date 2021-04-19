import FXStage.MyCanvas;
import FXStage.MyMenuBar;
import FXStage.MyNetBar;
import FXStage.ToolsPane;
import Network.Sever;
import icon.IconImage;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//程序入口
public class AppMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //以下是有关目录的创建
        File pics = new File("./我的作品");
        if(!pics.exists()) {
            pics.mkdir();
        }
        //以下是主要组件的添加
        primaryStage.getIcons().add(IconImage.getImage("ICON"));
        ToolsPane tools=new ToolsPane();
        MyNetBar myNetBar=new MyNetBar();
        MyCanvas canvas=new MyCanvas(myNetBar);
        MyMenuBar menuBar=new MyMenuBar(canvas);
        BorderPane bp=new BorderPane();
        bp.setTop(menuBar.getMenu());
        bp.setLeft(tools.getToolsPane());
        bp.setCenter(canvas.getCanvas());
        bp.setBottom(canvas.getStatusBar());
        bp.setRight(myNetBar.getRoot());
        //以下是必要的附加代码
        primaryStage.setScene(new Scene(bp));
        primaryStage.setTitle("联网画板");
        primaryStage.show();

    }


}