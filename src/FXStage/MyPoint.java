package FXStage;

import java.awt.*;

/**
 * @author Ayako
 */
// 给系统的point类添加一个新功能，使得point可以用字符串x,y初始化，同时支持三种初始化方式
public class MyPoint extends Point {
    // 字符串形如"x,y"
    public MyPoint(String poiStr) {
        super();
        this.x = Integer.parseInt(poiStr.substring(0, poiStr.indexOf(',')));
        this.y = Integer.parseInt(poiStr.substring(poiStr.indexOf(',') + 1));
    }

    public MyPoint(Point poi) {
        super();
        this.x = poi.x;
        this.y = poi.y;
    }

    public MyPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String getPos() {
        return "(" + this.x + "," + this.y + ")";
    }
}
