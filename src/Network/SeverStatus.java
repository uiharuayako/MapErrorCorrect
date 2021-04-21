package Network;

import javafx.scene.paint.Color;

/**
 * @author Ayako
 */
public class SeverStatus {
    static String toolName = "PEN";
    static double lineSize = 1;
    static double fontSize = 12;
    static boolean drawPoly = true;
    static String fontFamily = "AIGDT";
    static Color color = Color.BLACK;
    static String myText = "Uiharu";
    static Boolean fill = false;
    static String thisStatus;
    static double mouseX1;
    static double mouseX2;
    static double mouseY1;
    static double mouseY2;
    static String mouse;
    public static void setSeverStatus(String status){
        thisStatus = status;
        int index=thisStatus.indexOf("$");
        toolName=thisStatus.substring(0,index);// 获取信息

        thisStatus=thisStatus.substring(index+1);// 截短字符串
        index=thisStatus.indexOf("$");// 更新索引位置
        lineSize=Double.parseDouble(thisStatus.substring(0,index));// 获取信息

        thisStatus=thisStatus.substring(index+1);// 截短字符串
        index=thisStatus.indexOf("$");// 更新索引位置
        fontSize=Double.parseDouble(thisStatus.substring(0,index));// 获取信息

        thisStatus=thisStatus.substring(index+1);// 截短字符串
        index=thisStatus.indexOf("$");// 更新索引位置
        drawPoly=Boolean.parseBoolean(thisStatus.substring(0,index));// 获取信息

        thisStatus=thisStatus.substring(index+1);// 截短字符串
        index=thisStatus.indexOf("$");// 更新索引位置
        fontFamily=thisStatus.substring(0,index);// 获取信息

        thisStatus=thisStatus.substring(index+1);// 截短字符串
        index=thisStatus.indexOf("$");// 更新索引位置
        double r=Double.parseDouble(thisStatus.substring(0,index));// 获取信息

        thisStatus=thisStatus.substring(index+1);// 截短字符串
        index=thisStatus.indexOf("$");// 更新索引位置
        double g=Double.parseDouble(thisStatus.substring(0,index));// 获取信息

        thisStatus=thisStatus.substring(index+1);// 截短字符串
        index=thisStatus.indexOf("$");// 更新索引位置
        double b=Double.parseDouble(thisStatus.substring(0,index));// 获取信息

        thisStatus=thisStatus.substring(index+1);// 截短字符串
        index=thisStatus.indexOf("$");// 更新索引位置
        double o=Double.parseDouble(thisStatus.substring(0,index));// 获取信息
        color = new Color(r,g,b,o);

        thisStatus=thisStatus.substring(index+1);// 截短字符串
        index=thisStatus.indexOf("$");// 更新索引位置
        myText=thisStatus.substring(0,index);// 获取信息

        thisStatus=thisStatus.substring(index+1);// 截短字符串
        index=thisStatus.indexOf("$");// 更新索引位置
        fill=Boolean.parseBoolean(thisStatus.substring(0,index));// 获取信息

        thisStatus=thisStatus.substring(index+1);// 截短字符串
        index=thisStatus.indexOf("$");// 更新索引位置
        mouseX1=Double.parseDouble(thisStatus.substring(0,index));// 获取信息

        thisStatus=thisStatus.substring(index+1);// 截短字符串
        index=thisStatus.indexOf("$");// 更新索引位置
        mouseY1=Double.parseDouble(thisStatus.substring(0,index));// 获取信息

        thisStatus=thisStatus.substring(index+1);// 截短字符串
        index=thisStatus.indexOf("$");// 更新索引位置
        mouseX2=Double.parseDouble(thisStatus.substring(0,index));// 获取信息

        thisStatus=thisStatus.substring(index+1);// 截短字符串
        index=thisStatus.indexOf("$");// 更新索引位置
        mouseY2=Double.parseDouble(thisStatus.substring(0,index));// 获取信息

        mouse=thisStatus.substring(index+1);
    }
}
