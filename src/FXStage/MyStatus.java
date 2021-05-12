package FXStage;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.scene.paint.Color;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

// 记录当前窗口的状态，使用的工具情况，线的粗细等，是一个纯静态类
public class MyStatus {
    public static String mapName = "我的地图";
    public static String toolName = "PEN";
    public static double lineSize = 1;
    public static double fontSize = 12;
    public static boolean drawPoly = true;
    public static String fontFamily = "AIGDT";
    public static Color color = Color.BLACK;
    public static String myText = "Uiharu";
    public static String nickName = "Ayako";
    public static Boolean fill = false;
    // 文件存储的关键
    public static ArrayList<MyPoint> points = new ArrayList<>();
    // 服务器的图片是否有更新
    public static boolean isUpdate = false;
    public static boolean isAutoSave = true;
    // 是否有连接服务器的意愿
    public static boolean networkConnect = false;
    // 数据库相关
    public static Connection myCon = null;
    public static Statement myStm = null;
    // 是否正确登录，很重要，是登录的依据
    public static boolean rightAccount = false;
    // 作为唯一标识符
    public final static Timestamp id = new Timestamp(System.currentTimeMillis());
    // 设置备注信息文本
    public static String infoText = "请输入备注";
    public static File originImg = new File("./我的作品/" + MyStatus.mapName + "AutoSave.png");

    public static void setFontFamily(String font) {
        MyStatus.fontFamily = font;
    }

    public static void setColor(Color c) {
        MyStatus.color = c;
    }

    public static String status2Str() {
        return "$开始$" + toolName + "$工具名$" + lineSize + "$粗细$" + fontSize + "$字体大小$" + fontFamily + "$字体类型$" + color.toString() + "$颜色$" + myText + "$文字$" + fill + "$填充$";
    }

    //这个函数用来设置status
    public static void setStatus(String statusStr) {
        toolName = subString(statusStr, "$开始$", "$工具名$");
        lineSize = Double.parseDouble(subString(statusStr, "$工具名$", "$粗细$"));
        fontSize = Double.parseDouble(subString(statusStr, "$粗细$", "$字体大小$"));
        fontFamily = subString(statusStr, "$字体大小$", "$字体类型$");
        color = Color.web(subString(statusStr, "$字体类型$", "$颜色$"));
        myText = subString(statusStr, "$颜色$", "$文字$");
        fill = Boolean.parseBoolean(subString(statusStr, "文字", "填充"));
    }

    //截取指定两个字符串间的字符串
    public static String subString(String str, String strStart, String strEnd) {
        /* 找出指定的2个字符在该字符串里面的位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);
        /* 开始截取 */
        return str.substring(strStartIndex, strEndIndex).substring(strStart.length());
    }

    // 将status中的points以文本方式返回
    public static String points2Str() {
        StringBuilder poiStr = new StringBuilder();
        for (MyPoint poi : points) {
            poiStr.append(poi.getPos());
        }
        return poiStr.toString();
    }

    // 将字符串转化为点列表
    public static void str2Points(String poiStr) {
        // 首先统计有几个点
        int num = countChar(poiStr, ',');
        // 共有num个点
        for (int i = 0; i < num; i++) {
            points.add(new MyPoint(subString(poiStr, "(", ")")));
            // 防止截取时字符串访问越界，必须保证被截取的字符串有2个点
            if (i != num - 1) {
                poiStr = cutFirstPoint(poiStr);
            }
        }
    }

    // 统计字符串中指定字符的个数
    public static int countChar(String str, char ch) {
        int freq = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ch) {
                freq++;
            }
        }
        return freq;
    }

    // 去掉储存点字符串中的第一个点，并返回剩余字符串
    public static String cutFirstPoint(String poiStr) {
        return poiStr.substring(poiStr.indexOf(')') + 1);
    }

    // 向list中加入一个点
    public static void addPoint(int x, int y) {
        points.add(new MyPoint(x, y));
    }

}
