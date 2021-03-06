package FXStage;

/**
 * @author Ayako
 */
// 用来规范每一行的文本
public class InfoLine {
    public String statusStr;
    public String pointStr;
    public String infoStr;
    public MyPoint position;
    public int id;

    public InfoLine(String lineStr) {
        statusStr = lineStr.substring(0, lineStr.indexOf("$填充$") + 4);
        pointStr = MyStatus.subString(lineStr, "$点开始$", "$点结束$");
        infoStr = MyStatus.subString(lineStr, "$备注开始$", "$备注结束$");
        // 可能效率过低而废弃的功能，如果再给点时间或者机会可以实现，详见文档
        position=new MyPoint(MyStatus.subString(pointStr,"(",")"));
    }

    public InfoLine(String sStr, String pStr, String iStr) {
        statusStr = sStr;
        pointStr = pStr;
        infoStr = iStr;
    }

    public String getLineStr() {
        return statusStr + "$点开始$" + pointStr + "$点结束$$备注开始$" + infoStr + "$备注结束$";
    }
}
