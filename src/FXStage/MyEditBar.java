package FXStage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Ayako
 */
public class MyEditBar {
    VBox root;
    TextArea infoArea;
    HBox pointBox;
    Label pointLabel;
    Label infoLabel;
    TextField pointInfo;
    TextArea infoOutArea;
    Label infoOutLabel;
    Label searchLabel;
    Button searchB;
    HBox changedBox;
    Button prevB;
    Button nextB;
    Button firstB;
    Button lastB;
    TextField searchBar;
    Button saveButton;
    Button loadButton;
    HBox fileButtons;
    // 储存搜索到的行数
    int searchNum = 0;
    InfoLine searchLine;
    ArrayList<InfoLine> myInfo = new ArrayList<>();
    ArrayList<MyPoint> posOfPois = new ArrayList<>();

    public MyEditBar() {
        // 整体
        root = new VBox();
        root.setSpacing(10);
        root.setPadding(new Insets(5, 20, 0, 10));
        root.setMaxWidth(320);
        root.setAlignment(Pos.CENTER);
        // 初始化各个控件
        Label titleLabel = new Label("编辑栏");
        titleLabel.setStyle("-fx-font-weight:bold");
        titleLabel.setFont(Font.font("SimHei", 25));
        titleLabel.setAlignment(Pos.TOP_CENTER);
        infoLabel = getLabel("编辑错误信息", 20);
        // 编辑错误描述
        infoArea = new TextArea();
        infoArea.setPrefWidth(root.getPrefWidth() - 40);
        // 搜索错误信息
        searchLabel = getLabel("搜索条目", 20);
        searchBar = new TextField();
        searchB = new Button("搜索");
        searchB.setStyle("-fx-font-weight:bold");
        searchB.setOnAction(event -> {
            loadInfo();
            // 获取待搜索字符串
            String targetStr = searchBar.getText();
            // 暂时禁用保存按钮
            saveButton.setDisable(true);
            // 开始搜索，不用for in，为的是获取行号
            for (int i = 0; i < myInfo.size(); i++) {
                // 如果找到了
                if (myInfo.get(i).infoStr.contains(targetStr)) {
                    saveButton.setDisable(false);
                    searchNum = i;
                    searchLine = myInfo.get(i);
                    update();
                    break;
                }
            }
        });
        // 切换条目的HBox
        changedBox = new HBox();
        // 切换条目的Button
        firstB = new Button("首条");
        firstB.setOnAction(event -> {
            searchNum = 0;
            update();
        });
        prevB = new Button("←");
        prevB.setOnAction(event -> {
            if (searchNum > 0) {
                searchNum--;
                update();
            }
        });
        nextB = new Button("→");
        nextB.setOnAction(event -> {
            if (searchNum < myInfo.size() - 1) {
                searchNum++;
                update();
            }
        });
        lastB = new Button("末条");
        lastB.setOnAction(event -> {
            searchNum = myInfo.size() - 1;
            update();
        });
        changedBox.getChildren().addAll(searchB, firstB, prevB, nextB, lastB);
        changedBox.setAlignment(Pos.CENTER);
        changedBox.setSpacing(10);
        // 点信息的HBox
        pointBox = new HBox();
        pointLabel = getLabel("位置:", 20);
        pointInfo = new TextField();
        pointBox.getChildren().addAll(pointLabel, pointInfo);
        pointBox.setAlignment(Pos.CENTER);
        pointBox.setSpacing(20);
        // 备注信息
        infoOutLabel = getLabel("描述", 20);
        infoOutArea = new TextArea();
        // 文件相关操作
        fileButtons = new HBox();
        saveButton = new Button("保存更改");
        saveButton.setOnAction(event -> {
            // 当按下保存按钮
            // 首先将point中的更改写入搜索到的line，当然，也可能是其他line
            searchLine.pointStr = pointInfo.getText();
            searchLine.infoStr = infoOutArea.getText();
            // 因为不知道写入了几次，因此将内存中的info的整体list写入文件
            try {
                File file = new File(MyStatus.mapName + ".mec");
                FileWriter fileWriter = new FileWriter(file);
                PrintWriter pw = new PrintWriter(fileWriter);
                for (InfoLine thisLine : myInfo) {
                    pw.println(thisLine.getLineStr());
                    pw.flush();
                }
                fileWriter.flush();
                fileWriter.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // 初始禁止更改
        saveButton.setDisable(true);
        loadButton = new Button("导入文件");
        loadButton.setOnAction(event -> {
            loadInfo();
        });
        fileButtons.getChildren().addAll(saveButton, loadButton);
        fileButtons.setAlignment(Pos.CENTER);
        fileButtons.setSpacing(80);
        root.getChildren().addAll(titleLabel, infoLabel, infoArea, new Separator(), searchLabel, searchBar, changedBox, new Separator(), pointBox, infoOutLabel, infoOutArea, fileButtons);
    }

    public VBox getRoot() {
        return root;
    }

    Label getLabel(String str, int size) {
        Label result = new Label(str);
        result.setFont(Font.font("YouYuan", size));
        result.setStyle("-fx-font-weight:300");
        return result;
    }

    // 已经用catch环绕
    void loadInfo() {
        myInfo.clear();
        posOfPois.clear();
        try {
            File file = new File(MyStatus.mapName + ".mec");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String thisLine;
            while ((thisLine = br.readLine()) != null) {
                InfoLine changedLine = new InfoLine(thisLine);
                posOfPois.add(changedLine.position);
                myInfo.add(changedLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void update() {
        searchLine = myInfo.get(searchNum);
        pointInfo.setText(searchLine.pointStr);
        infoOutArea.setText(searchLine.infoStr);
        searchLabel.setText("搜索条目 类型:" + MyStatus.subString(searchLine.statusStr, "$开始$", "$工具名$") + "(" + (searchNum + 1) + "/" + myInfo.size() + ")");
        // 如果当前已经到最后一条，禁用下一条以及末尾按键
        if (searchNum == myInfo.size() - 1) {
            nextB.setDisable(true);
            lastB.setDisable(true);
        } else {
            nextB.setDisable(false);
            lastB.setDisable(false);
        }
        // 如果当前已经到第一条，禁用第一条以及首个按键
        if (searchNum == 0) {
            prevB.setDisable(true);
            firstB.setDisable(true);
        } else {
            prevB.setDisable(false);
            firstB.setDisable(false);
        }
    }
}
