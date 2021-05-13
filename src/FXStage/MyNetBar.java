package FXStage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * @author Ayako
 */
public class MyNetBar {
    NameList tool = new NameList();
    ObservableList<People> names;
    ObservableList<String> images;
    private TableView nameTable = new TableView();
    private ListView imageList = new ListView();
    ;
    VBox root;
    Label nameLabel;
    Label isOnline;
    boolean changePic = false;

    public MyNetBar() {
        // 整体
        root = new VBox();
        root.setSpacing(20);
        root.setPadding(new Insets(15, 20, 0, 10));
        root.setAlignment(Pos.CENTER);
        // 状态显示
        nameLabel = new Label("昵称：" + MyStatus.nickName);
        nameLabel.setFont(Font.font("Microsoft YaHei", 20));
        nameLabel.setStyle("-fx-font-weight:bold");
        isOnline = new Label("当前" + (MyStatus.rightAccount ? "在线" : "离线"));
        isOnline.setFont(Font.font("Microsoft YaHei", 20));
        isOnline.setStyle("-fx-font-weight:bold");
        // 好友列表
        TableColumn nameCol = new TableColumn("姓名");
        nameCol.setCellValueFactory(new PropertyValueFactory<People, String>("name"));
        nameCol.setMinWidth(110);
        TableColumn idCol = new TableColumn("在线情况");
        idCol.setCellValueFactory(new PropertyValueFactory<People, String>("isOnline"));
        idCol.setMinWidth(140);
        nameTable.setItems(names);
        nameTable.getColumns().addAll(nameCol, idCol);
        // 图片列表
        imageList.setItems(images);
        // 开始禁用，登录成功才给用
        imageList.setDisable(true);
        imageList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (MyStatus.rightAccount) {
                MyStatus.mapName = newValue.toString();
                MyDBProcess.getPicNow();
                changePic = true;
            }
        });
        root.getChildren().addAll(nameLabel, nameTable, isOnline, imageList);
    }

    public VBox getRoot() {
        return root;
    }

    public void update() {
        nameLabel.setText("昵称：" + MyStatus.nickName);
        isOnline.setText("当前" + (MyStatus.rightAccount ? "在线" : "离线"));
        NameList.update();
        ImageList.update();
        names = FXCollections.observableArrayList(NameList.myNameList);
        images = FXCollections.observableArrayList(ImageList.myImageList);
        nameTable.setItems(names);
        imageList.setItems(images);
        nameTable.refresh();
        imageList.refresh();
        if(MyStatus.rightAccount){
            imageList.setDisable(false);
        }
    }
}
