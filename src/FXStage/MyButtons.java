package FXStage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

//设定按钮
public class MyButtons {
    private VBox toolsVBox;
    MyButtons(){
        TilePane buttonBox = new TilePane();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10,10,30,10));
        buttonBox.setPrefColumns(2);
        buttonBox.setHgap(5);
        buttonBox.setVgap(5);
        String[] buttons={"PEN","RUBBER","PIN","TEXT","LINE","RECTANGLEY","RECTANGLEZ","POLYGON","OVAL"};
        for(int i=0;i<buttons.length;i++){
            ToolButton tmpButton=new ToolButton(buttons[i]);
            buttonBox.getChildren().add(tmpButton);
        }
        Label titleL=new Label("工具栏");
        titleL.setFont(Font.font ("Microsoft YaHei", 20));
        titleL.setStyle("-fx-font-weight:bold");
        CheckBox isFilled=new CheckBox("设置图形填充");
        isFilled.setSelected(false);
        isFilled.setOnAction(event -> {
            MyStatus.fill=isFilled.isSelected();
        });
        isFilled.setStyle("-fx-font-weight:bold");
        toolsVBox = new VBox();
        toolsVBox.setAlignment(Pos.CENTER);
        toolsVBox.setSpacing(10);
        toolsVBox.getChildren().addAll(titleL,isFilled,buttonBox);
    }
    public VBox getToolsVBox() {return toolsVBox;}
}
