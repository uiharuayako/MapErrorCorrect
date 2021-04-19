package FXStage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

// 调色板类
public class MyPalette {
    private VBox content;
    private TilePane tilePane;
    private Color presentColor = Color.BLACK;
    private ColorPicker colorPicker = new ColorPicker();
    private List<ColorButton> colorButton = new ArrayList<ColorButton>();
    private String[] color = {"#ffffff", "#000000", "#848683", "#c4c3be", "#cdbedb", "#b97b56",
            "#ffadd6", "#f01e1f", "#89010d", "#fef007", "#ffc80c", "#ff7c26",
            "#efe2ab", "#b6e51d", "#24b04f", "#93dceb", "#6997bb", "#07a0e6",
            "#d9a2dc", "#9c4ca1","#3b46e0"};
    public MyPalette(){
        initUI();
    }
    private void initUI(){

        content = new VBox();
        content.setSpacing(20);
        content.setPadding(new Insets(15,10,0,10));
        content.setAlignment(Pos.CENTER);
        Label titleC=new Label("调色板");
        titleC.setFont(Font.font ("Microsoft YaHei", 20));
        titleC.setStyle("-fx-font-weight:bold");
        content.getChildren().add(titleC);
        colorPicker = new ColorPicker();
        colorPicker.setPrefWidth(160);
        colorPicker.setStyle("-fx-background-color:#3C3F41;-fx-base:#000000;-fx-color-label-visible:true;");
        colorPicker.setValue(presentColor);
        colorPicker.setOnAction((ActionEvent t) -> {
            presentColor = colorPicker.getValue();
            MyStatus.setColor(presentColor);
        });

        tilePane = new TilePane();
        tilePane.setAlignment(Pos.CENTER);
        tilePane.setPadding(new Insets(0,10,0,10));
        tilePane.setPrefColumns(3);
        tilePane.setHgap(5);
        tilePane.setVgap(5);

        DropShadow shadow = new DropShadow();
        //不把这一段封装到ColorButton类的原因是..用到了color这个数组
        for(int i=0; i<color.length; i++){
            ColorButton cButton = new ColorButton(color[i]);
            cButton.setStyle("-fx-base: " + color[i] + ";");
            cButton.setPrefSize(40, 25);
            cButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
                cButton.setEffect(shadow);
            });

            cButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
                cButton.setEffect(null);
            });

            cButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                presentColor = Color.web( ((ColorButton) e.getSource()).getName() );
                colorPicker.setValue(presentColor);
                MyStatus.setColor(presentColor);
            });
            colorButton.add(cButton);
        }
        //字体
        Label fasLabel=new Label("字体与大小");
        fasLabel.setFont(Font.font ("Microsoft YaHei", 20));
        fasLabel.setStyle("-fx-font-weight:bold");
        ComboBox<String> fontFamily=new ComboBox<String>();
        ObservableList<String> fontFamilyItems = FXCollections.observableArrayList();
        fontFamilyItems.addAll(Font.getFamilies());
        fontFamily.setStyle("-fx-background-color:#3C3F41;-fx-base:#000000");
        fontFamily.setPrefWidth(160);
        fontFamily.setItems(fontFamilyItems);
        fontFamily.getSelectionModel().select(0);
        fontFamily.valueProperty().addListener((ov, oldv, newv) -> {
            MyStatus.setFontFamily(newv);
        });
        //大小
        Label sLabel=new Label("设置大小：1-36，当前01");
        sLabel.setFont(Font.font ("Microsoft YaHei", 16));
        sLabel.setStyle("-fx-font-weight:bold");
        Slider sizeS=new Slider();
        sizeS.setMax(36);
        sizeS.setMin(1);
        sizeS.setMaxWidth(150);
        sizeS.addEventHandler(MouseEvent.MOUSE_DRAGGED,event -> {
            MyStatus.lineSize=sizeS.getValue();
            MyStatus.fontSize=sizeS.getValue()+7;//这样设应该比较自然
            if((int)sizeS.getValue()<=9) sLabel.setText("设置大小：1-36，当前0"+(int)sizeS.getValue());
            else sLabel.setText("设置大小：1-36，当前"+(int)sizeS.getValue());
        });

        tilePane.getChildren().addAll(colorButton);
        content.getChildren().addAll(new Separator(),colorPicker);
        content.getChildren().add(tilePane);
        content.getChildren().addAll(new Separator(),fasLabel,fontFamily);
        content.getChildren().addAll(sLabel,sizeS,new Separator());
    }
    public VBox getColorPanel(){
        return content;
    }
}
