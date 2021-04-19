package FXStage;

import javafx.scene.paint.Color;

// 记录当前窗口的状态，使用的工具情况，线的粗细等
public class MyStatus {
    public static String toolName = "PEN";
    public static double lineSize = 1;
    public static double fontSize = 12;
    public static boolean drawPoly = true;
    public static String fontFamily = "AIGDT";
    public static Color color = Color.BLACK;
    public static String myText = "Uiharu";
    public static String nickName = "Ayako";
    public static Boolean fill = false;
    public static boolean isUpdate = false;// 服务器的图片是否有更新
    public static boolean isAutoSave = true;
    public static boolean networkConnect = false;// 是否有连接服务器的意愿
    public final static String id="" + System.currentTimeMillis();// 作为唯一标识符
    public static void setFontFamily(String font){
        MyStatus.fontFamily = font;
    }
    public static void setColor(Color c){
        MyStatus.color = c;
    }
    public static String status2Str(){
        return toolName+"$"+lineSize+"$"+fontSize+"$"+drawPoly+"$"+fontFamily+"$"+color.getRed()+"$"+color.getGreen()+"$"+color.getBlue()+"$"+color.getOpacity()+"$"+myText+"$"+fill;
    }
}
