package FXStage;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class ToolsPane {
    //整个工具栏的结构
    private VBox toolsPane;
    //初始化
    public ToolsPane(){
        toolsPane = new VBox();
        toolsPane.setAlignment(Pos.CENTER);
        toolsPane.setSpacing(10);
        MyPalette palette = new MyPalette();
        MyButtons buttons = new MyButtons();
        toolsPane.getChildren().add(palette.getColorPanel());
        toolsPane.getChildren().add(buttons.getToolsVBox());
    }
    public VBox getToolsPane() {return toolsPane;}
}
