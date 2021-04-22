package FXStage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import icon.IconImage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class MyNotePad{
    private Stage stage;
    private TextArea ta;
    private String path;
    BorderPane root;
    public MyNotePad(String path) {
        try {
            root = new BorderPane();
            //调用方法 添加菜单
            addMenus(root);
            //调用方法 添加多行文本框
            addMainTextArea(root);
            openFile(new File(path));
            stage=new Stage();
            stage.setScene(new Scene(root,800,800));
            stage.setTitle("编辑文本信息---"+path);
            stage.getIcons().add(IconImage.getImage("ICON"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Stage getStage(){
        return stage;
    }
    /**
     * 添加文本框
     */
    private void addMainTextArea(BorderPane root) {
        //创建多行文本框
        ta = new TextArea();
        root.setCenter(ta);
    }

    public void openFile(File file) {
        if (file != null && file.exists()) {
            try {
                //读取数据  放进多行文本框
                FileInputStream in = new FileInputStream(file);
                byte[] bs = new byte[(int) file.length()];
                in.read(bs);

                //将内容设置到多行文本框
                ta.setText(new String(bs));
                in.close();
                //将文件路径  保留到成员变量path中
                path = file.getPath();
                //更改窗口标题
                int lastIndex = path.lastIndexOf("\\");
                String title = path.substring(lastIndex + 1);
                setTitle(title + "-我的记事本");
            } catch (Exception e) {
            }
        }
    }

    /**
     * 布局对象添加菜单
     *
     * @param root
     */
    private void addMenus(BorderPane root) {
        //窗口-->Menubar-->Menu--->MenuItem
        //root-->Menubar-->Menu--->MenuItem
        //创建菜单条
        MenuBar bar = new MenuBar();
        //创建菜单
        Menu menu = new Menu("文件");
        //创建菜单项
        MenuItem item1 = new MenuItem("打开");
        item1.setOnAction(event -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("选择文件-打开");
            File file = fc.showOpenDialog(stage);
            openFile(file);

        });
        MenuItem item2 = new MenuItem("新建");
        item2.setOnAction(event -> {
            //清除文本框的内容
            ta.setText("");
            //更改窗口标题
            setTitle("新文件-我的记事本");
            //将开打的文件的路径设置为null
            path = null;
        });


        MenuItem item3 = new MenuItem("保存");
        item3.setOnAction(event -> {
            //需要判断是新建之后的保存  还是打开之后的保存
            //新建之后的保存
            if (path == null) {
                FileChooser fc = new FileChooser();
                fc.setTitle("选择文件-保存");
                //获取被用户选择的文件
                File file = fc.showSaveDialog(stage);
                //如果用户选了 而且文件是存在的
                if (file != null && file.exists()) {
                    //将多行文本框中的内容写到file指向的文件中去
                    try {
                        //创建输出流
                        FileOutputStream out = new FileOutputStream(file);
                        out.write(ta.getText().getBytes());
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {//打开之后的保存
                try {
                    //创建输出流
                    FileOutputStream out = new FileOutputStream(path);
                    out.write(ta.getText().getBytes());
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //建立菜单之间的关系
        menu.getItems().add(item1);
        menu.getItems().add(item2);
        menu.getItems().add(item3);
        bar.getMenus().add(menu);
        root.setTop(bar);
    }

    /**
     * 设置标题
     */
    private void setTitle(String title) {
        stage.setTitle(title);
    }
}
