package FXStage;

import icon.IconImage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sun.security.provider.MD5;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MyMenuBar {
    MenuBar myBar;
    MyCanvas tmpCanvas;

    public MyMenuBar(MyCanvas getCanvas) {
        tmpCanvas = getCanvas;
        // 以下是菜单栏的设计
        myBar = new MenuBar();
        // 文件选单
        Menu fileMenu = new Menu();
        fileMenu.setText("文件");
        fileMenu.setStyle("-fx-font-size:16;");
        MenuItem newFile = new MenuItem("新建");
        newFile.setStyle("-fx-font-size:14;");
        newFile.setAccelerator(KeyCombination.keyCombination("Ctrl+n"));
        newFile.setOnAction(event -> {
            tmpCanvas.clear();
        });
        MenuItem openImage = new MenuItem("打开");
        openImage.setStyle("-fx-font-size:14;");
        openImage.setAccelerator(KeyCombination.keyCombination("Ctrl+o"));
        openImage.setOnAction(event -> {
            boolean isOK = true;
            if (!tmpCanvas.getList().isEmpty()) {
                Alert confirmOpen = new Alert(Alert.AlertType.CONFIRMATION, null, new ButtonType("取消", ButtonBar.ButtonData.NO),
                        new ButtonType("确定", ButtonBar.ButtonData.YES));
                confirmOpen.setTitle("数据删除警告");
                confirmOpen.setHeaderText("您当前正在进行的是打开文件操作");
                confirmOpen.initStyle(StageStyle.UTILITY);
                confirmOpen.setContentText("这意味着当前画板内容将被清空");
                Optional<ButtonType> buttonType = confirmOpen.showAndWait();
                // 根据点击结果
                if (!buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)) {
                    isOK = false;
                }
            }
            if (isOK) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("尺寸800*800的PNG文件", "*.png"));
                fileChooser.setInitialDirectory(new File("./我的作品"));
                fileChooser.setTitle("打开图片");// 打开图片
                File file = fileChooser.showOpenDialog(new Stage());
                if (file != null) {
                    try {
                        Image image = new Image(new FileInputStream(file));
                        tmpCanvas.setImage(image);
                        MyStatus.mapName = MyStatus.getFileName(file);
                        MyStatus.originImg = file;
                        // 下面这行代码是为了创建文件
                        new File(MyStatus.getMecPath()).createNewFile();
                        // 把文件添加到数据库
                        MyDBProcess.savePic();
                        MyDBProcess.addLog("上传图片");
                    } catch (Exception ex) {
                    }
                }
            }
            tmpCanvas.myEditBar.loadInfo();
            tmpCanvas.drawFromFile();
        });
        MenuItem saveImage = new MenuItem("保存");
        saveImage.setStyle("-fx-font-size:14;");
        saveImage.setAccelerator(KeyCombination.keyCombination("Ctrl+s"));
        saveImage.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("./我的作品"));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("我的绘画", "*.png"));
            fileChooser.setTitle("保存图片");// 保存图片
            File file = fileChooser.showSaveDialog(new Stage());
            try {
                if (file != null) {
                    ImageIO.write(tmpCanvas.getNewImage(), "PNG", file);
                    file = null;
                }
            } catch (IOException ignored) {
            }
        });
        fileMenu.getItems().addAll(newFile, openImage, saveImage);
        // 编辑选单
        Menu editMenu = new Menu();
        editMenu.setText("编辑");
        editMenu.setStyle("-fx-font-size:16;");
        MenuItem undo = new MenuItem("撤销");
        undo.setStyle("-fx-font-size:14;");
        undo.setAccelerator(KeyCombination.keyCombination("Ctrl+z"));
        undo.setOnAction(event -> {
            tmpCanvas.undo();
        });
        MenuItem redo = new MenuItem("重做");
        redo.setStyle("-fx-font-size:14;");
        redo.setAccelerator(KeyCombination.keyCombination("Ctrl+y"));
        redo.setOnAction(event -> {
            tmpCanvas.redo();
        });
        editMenu.getItems().addAll(undo, redo);
        // 文档选单
        Menu docMenu = new Menu();
        docMenu.setText("文档");
        docMenu.setStyle("-fx-font-size:16;");
        MenuItem docEdit = new MenuItem("文档编辑");
        docEdit.setOnAction(event -> {
            new MyNotePad(MyStatus.getMecPath()).getStage().show();
            tmpCanvas.myEditBar.loadInfo();
            redrawMapByFile();
        });
        MenuItem redrawFromDoc = new MenuItem("文档重绘");
        redrawFromDoc.setStyle("-fx-font-size:16;");
        redrawFromDoc.setOnAction(event -> {
            redrawMapByFile();
        });
        docMenu.getItems().addAll(docEdit, redrawFromDoc);
        // 网络选单
        Menu netMenu = new Menu();
        netMenu.setText("网络");
        netMenu.setStyle("-fx-font-size:16;");
        MenuItem logInItem = new MenuItem("登录");
        logInItem.setOnAction(event -> {
            // 加入框架
            Stage loginStage = new Stage();
            BorderPane loginPane = new BorderPane();
            // 注册信息框
            VBox loginVBox = new VBox();
            loginVBox.setAlignment(Pos.CENTER);
            loginVBox.setSpacing(10);
            Label nameLabel = new Label("输入昵称:");
            Label passwdLabel = new Label("输入密码:");
            TextField nameTF = new TextField(MyStatus.nickName);
            PasswordField pwPF = new PasswordField();
            HBox nameHB = new HBox();
            nameHB.setAlignment(Pos.CENTER);
            nameHB.getChildren().addAll(nameLabel, nameTF);
            HBox pwHB = new HBox();
            pwHB.setAlignment(Pos.CENTER);
            pwHB.getChildren().addAll(passwdLabel, pwPF);
            loginVBox.getChildren().addAll(nameHB, pwHB);
            // 搞个按钮
            Button loginB = new Button("登录");
            loginB.setPrefSize(50, 40);
            loginB.setDisable(true);
            // 用户名与密码必须非空，才能使用登录按键
            loginPane.setOnMouseMoved(event1 -> {
                if (!nameTF.getText().isEmpty() && !pwPF.getText().isEmpty()) {
                    loginB.setDisable(false);
                }
            });
            // 开始时禁用
            Label rightLabel = new Label();
            rightLabel.setAlignment(Pos.CENTER);
            loginB.setOnAction(event1 -> {
                PreparedStatement stmt = null;
                try {
                    // 当登录按钮被按下
                    // 检查昵称是否存在
                    boolean isExist = false;
                    PreparedStatement ps = MyStatus.myCon.prepareStatement("select name from maperrordb.user where name = '" + nameTF.getText() + "'");
                    ResultSet rs = ps.executeQuery();
                    Alert loginInfo = new Alert(Alert.AlertType.INFORMATION, null, new ButtonType("确认"));
                    loginInfo.initStyle(StageStyle.UTILITY);
                    loginInfo.setTitle("登录信息框框");
                    while (rs.next()) {
                        isExist = true;
                    }
                    // 如果不存在
                    if (!isExist) {
                        loginInfo.setHeaderText("该昵称不存在");
                        rightLabel.setText("昵称不存在");
                        loginInfo.show();
                    } else {
                        // 如果昵称确实存在
                        ps = MyStatus.myCon.prepareStatement("select pwMD5 from maperrordb.user where name = '" + nameTF.getText() + "'");
                        rs = ps.executeQuery();
                        rightLabel.setText("密码错误！");
                        loginInfo.setHeaderText("用户存在然而密码错了QAQ");
                        while (rs.next()) {
                            // 如果用户名对应的密码md5正确
                            if (rs.getString(1).equals(MD5Utils.toMD5(pwPF.getText()))) {
                                // 更改内部状态：证明已经登录
                                MyStatus.rightAccount = true;
                                MyStatus.nickName = nameTF.getText();
                                loginInfo.setHeaderText("登陆成功哒！当前用户：" + MyStatus.nickName);
                                MyDBProcess.addLog("用户"+MyStatus.nickName+"登录成功");
                                loginStage.close();
                            }
                        }
                        loginInfo.show();
                    }
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                }

            });
            VBox rightVB = new VBox();
            rightVB.setAlignment(Pos.CENTER);
            rightVB.setSpacing(10);
            rightVB.getChildren().addAll(rightLabel, loginB);
            // 整体布局
            loginPane.setLeft(loginVBox);
            loginPane.setCenter(rightVB);
            loginPane.setMinWidth(400);
            // 下面这个选项开始设成true，但是不好，会挡住之后的弹窗
            loginStage.setAlwaysOnTop(false);
            loginStage.setScene(new Scene(loginPane));
            loginStage.setTitle("登录已有账号");
            loginStage.getIcons().add(IconImage.getImage("ICON"));
            loginStage.show();
        });
        MenuItem signUpItem = new MenuItem("注册");
        signUpItem.setOnAction(event -> {
            // 加入框架
            Stage regStage = new Stage();
            BorderPane regPane = new BorderPane();
            // 注册信息框
            VBox regVBox = new VBox();
            regVBox.setAlignment(Pos.CENTER);
            regVBox.setSpacing(10);
            Label nameLabel = new Label("输入昵称:");
            Label passwdLabel = new Label("输入密码:");
            Label confpwLabel = new Label("确认密码:");
            TextField nameTF = new TextField("请输入昵称");
            PasswordField pwPF = new PasswordField();
            PasswordField cfpwPF = new PasswordField();
            HBox nameHB = new HBox();
            nameHB.setAlignment(Pos.CENTER);
            nameHB.getChildren().addAll(nameLabel, nameTF);
            HBox pwHB = new HBox();
            pwHB.setAlignment(Pos.CENTER);
            pwHB.getChildren().addAll(passwdLabel, pwPF);
            HBox cfpwHB = new HBox();
            cfpwHB.setAlignment(Pos.CENTER);
            cfpwHB.getChildren().addAll(confpwLabel, cfpwPF);
            regVBox.getChildren().addAll(nameHB, pwHB, cfpwHB);
            // 搞个按钮
            Button confB = new Button("注册");
            confB.setPrefSize(50, 40);
            // 开始时禁用
            confB.setDisable(true);
            // 搞个确认两次密码相同的Label
            Label samePW = new Label();
            samePW.setAlignment(Pos.CENTER);
            regPane.setOnMouseMoved(event1 -> {
                // 如果密码和确认密码同时非空，且一样
                if (!pwPF.getText().isEmpty() && !cfpwPF.getText().isEmpty() && cfpwPF.getText().equals(pwPF.getText())) {
                    samePW.setText("√");
                    // 启用注册按钮
                    confB.setDisable(false);
                }
                // 如果不一样
                if (!pwPF.getText().isEmpty() && !cfpwPF.getText().isEmpty() && !cfpwPF.getText().equals(pwPF.getText())) {
                    samePW.setText("密码不一致");
                }
            });
            confB.setOnAction(event1 -> {
                PreparedStatement stmt = null;
                try {
                    // 检测重名
                    boolean isRepreted = false;
                    PreparedStatement ps = MyStatus.myCon.prepareStatement("select name from maperrordb.user where name = '" + nameTF.getText() + "'");
                    ResultSet rs = ps.executeQuery();
                    Alert regInfo = new Alert(Alert.AlertType.INFORMATION, null, new ButtonType("确认"));
                    regInfo.initStyle(StageStyle.UTILITY);
                    regInfo.setTitle("注册信息框框");
                    while (rs.next()) {
                        isRepreted = true;
                        regInfo.setHeaderText("该昵称已经被注册，换一个吧QAQ");
                        samePW.setText("昵称重复！");
                        regInfo.show();
                    }
                    if (!isRepreted) {
                        // 改名
                        MyStatus.nickName = nameTF.getText();
                        // 成功登陆状态
                        MyStatus.rightAccount=true;
                        stmt = MyStatus.myCon.prepareStatement("insert into user(name,pwMD5,id) values(?,?,?)");
                        stmt.setString(1, MyStatus.nickName);
                        stmt.setString(2, MD5Utils.toMD5(cfpwPF.getText()));
                        stmt.setTimestamp(3, MyStatus.id);
                        stmt.executeUpdate();
                        stmt.clearParameters();
                        regInfo.setHeaderText("注册成功！");
                        MyDBProcess.addLog("用户"+MyStatus.nickName+"注册成功");
                        tmpCanvas.netLabel.update();
                        regStage.close();
                        regInfo.show();
                    }
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                }

            });
            VBox rightVB = new VBox();
            rightVB.setAlignment(Pos.CENTER);
            rightVB.setSpacing(10);
            rightVB.getChildren().addAll(samePW, confB);
            // 整体布局
            regPane.setLeft(regVBox);
            regPane.setCenter(rightVB);
            regPane.setMinWidth(400);
            // 下面这个选项开始设成true，但是不好，会挡住之后的弹窗
            regStage.setAlwaysOnTop(false);
            regStage.setScene(new Scene(regPane));
            regStage.setTitle("注册新的账号");
            regStage.getIcons().add(IconImage.getImage("ICON"));
            regStage.show();
        });
        MenuItem connectNet = new MenuItem("与网络连接");
        connectNet.setStyle("-fx-font-size:14;");
        connectNet.setOnAction(event -> {
            try {
                if (MyStatus.networkConnect) {
                    connectNet.setText("与网络连接");
                    tmpCanvas.disconnect();
                    signUpItem.setDisable(false);
                    MyStatus.networkConnect = false;
                } else {
                    connectNet.setText("与网络断开");
                    tmpCanvas.connectNet();
                    signUpItem.setDisable(true);
                    MyStatus.networkConnect = true;
                }
            } catch (Exception e) {
            }
            tmpCanvas.netLabel.update();
        });
        netMenu.getItems().addAll(connectNet, logInItem, signUpItem);
        // 添加进menu
        myBar.getMenus().addAll(fileMenu, editMenu, docMenu, netMenu);
    }

    public MenuBar getMenu() {
        return myBar;
    }

    //使用文本文档和原图像重绘
    public void redrawMapByFile() {
        try {
            tmpCanvas.setImage(new Image(new FileInputStream(MyStatus.originImg)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        tmpCanvas.myEditBar.loadInfo();
        tmpCanvas.drawFromFile();
    }
}
