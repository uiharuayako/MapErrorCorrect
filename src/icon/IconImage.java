package icon;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// 这个类存放这图标对应的图片对象
public class IconImage {
    public static Image getImage(String name){
        if(name.equals("PEN")) return new Image("/icon/pen.png");
        if(name.equals("RUBBER")) return new Image("/icon/rubber.png");
        if(name.equals("PIN")) return new Image("/icon/pin.png");
        if(name.equals("TEXT")) return new Image("/icon/text.png");
        if(name.equals("LINE")) return new Image("/icon/line.png");
        if(name.equals("RECTANGLEY")) return new Image("/icon/rectangleY.png");
        if(name.equals("RECTANGLEZ")) return new Image("/icon/rectangleZ.png");
        if(name.equals("POLYGON")) return new Image("/icon/polygon.png");
        if(name.equals("ARC")) return new Image("/icon/arc.png");
        if(name.equals("OVAL")) return new Image("/icon/oval.png");
        return new Image("/icon/logo.png");
    }
    public static ImageView getImageView(Image curImg) {
        ImageView curImgV = new ImageView(curImg);
        curImgV.setFitHeight(30);
        curImgV.setFitWidth(30);
        return curImgV;
    }
}
