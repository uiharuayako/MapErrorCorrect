package FXStage;

import icon.IconImage;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;

import java.util.Optional;

// 这是自定义的按钮类，这个按钮只有一个作用---改变信息(当然，文字输入除外
public class ToolButton extends Button {
    ToolButton(String name){
        this.setGraphic(IconImage.getImageView(IconImage.getImage(name)));//设定按钮外观
        if(name.equals("TEXT")){
            this.setOnAction(event -> {
                TextInputDialog dialog = new TextInputDialog("");
                dialog.setGraphic(IconImage.getImageView(IconImage.getImage("TEXT")));
                dialog.setTitle("文本输入框");
                dialog.setContentText("请输入");
                dialog.setHeaderText("输入希望显示的文字");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                    MyStatus.myText=result.get();
                }
                MyStatus.toolName=name;//别忘了设定当前工具
            });
        }
        else if(name.equals("POLYGON")){
            this.setOnAction(event -> {
                MyStatus.toolName=name;//别忘了设定当前工具
                MyStatus.drawPoly=!MyStatus.drawPoly;
            });
        }
        else{
            this.setOnAction(event -> {
            MyStatus.toolName=name;//设定当前工具
        });
        }
        this.setStyle("-fx-base:#aaaaaa;");
        this.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            this.setEffect(new DropShadow());
        });

        this.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            this.setEffect(null);
        });
    }

}
