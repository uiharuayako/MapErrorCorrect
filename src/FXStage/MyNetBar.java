package FXStage;

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
    private TableView nameTable = new TableView();
    VBox root;
    Label nameLabel;
    Label isOnline;
    TextArea toDoArea;
    public MyNetBar() {
        // 整体
        root = new VBox();
        root.setSpacing(20);
        root.setPadding(new Insets(15,20,0,10));
        root.setAlignment(Pos.CENTER);
        // 状态显示
        nameLabel = new Label("昵称："+MyStatus.nickName);
        nameLabel.setFont(Font.font ("Microsoft YaHei", 20));
        nameLabel.setStyle("-fx-font-weight:bold");
        isOnline = new Label("当前" + (MyStatus.networkConnect ? "在线" : "离线"));
        isOnline.setFont(Font.font ("Microsoft YaHei", 20));
        isOnline.setStyle("-fx-font-weight:bold");
        // 消息显示
        Label toDoLabel = new Label("便签");
        toDoLabel.setFont(Font.font ("Microsoft YaHei", 20));
        toDoLabel.setStyle("-fx-font-weight:bold");
        toDoArea = new TextArea();
        toDoArea.setPrefWidth(root.getPrefWidth()-20);
        // 好友列表
        TableColumn nameCol = new TableColumn("姓名");
        nameCol.setCellValueFactory(new PropertyValueFactory<People,String>("name"));
        nameCol.setMinWidth(110);
        TableColumn idCol = new TableColumn("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<People,String>("ID"));
        idCol.setMinWidth(140);
        nameTable.setItems(names);
        nameTable.getColumns().addAll(nameCol,idCol);
        root.getChildren().addAll(toDoLabel,toDoArea,nameLabel,isOnline,nameTable);
    }
    public VBox getRoot(){
        return root;
    }
    void update(){
            nameLabel.setText("昵称：" + MyStatus.nickName);
            isOnline.setText("当前" + (MyStatus.networkConnect ? "在线" : "离线"));
            names = FXCollections.observableArrayList(NameList.myNameList);
            nameTable.setItems(names);
            nameTable.refresh();
    }
}
