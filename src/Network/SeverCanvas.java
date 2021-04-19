package Network;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
//这是一个画板，把许多画布合成而来，同时，包括一个状态栏。
public class SeverCanvas {
    //鼠标按下位置
    private double x1;
    private double y1;
    //鼠标松开位置
    private double x2;
    private double y2;
    //绘制多边形使用的特殊鼠标位置
    private double[] x;
    private double[] y;
    private int numPoints;
    //画板相关
    private Group content;
    private VBox vbox;
    private Canvas drawingCanvas;
    private GraphicsContext gc;
    public static int drawingCanvasWidth;
    public static int drawingCanvasHeight;
    private List<Canvas> listCanvas;
    //网络相关
    Socket mySocket;
    private FileInputStream myFIS = null;
    private OutputStream os = null;
    private String command;

    //重做相关
    private Canvas newCanvas;
    public SeverCanvas(){
        //网络声明
        mySocket = null;
        newCanvas = null;
        //状态栏声明
        numPoints=0;
        //以下是画板
        content = new Group();
        vbox = new VBox();
        x=new double[20];
        y=new double[20];
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10, 20, 0, 0));
        vbox.getChildren().add(content);
        drawingCanvas = new Canvas(800,800);
        gc = drawingCanvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 800,800);
        gc.restore();
        content.getChildren().add(drawingCanvas);
        
        setStatus(drawingCanvas, SeverStatus.color, false);
        drawingCanvasWidth = (int) drawingCanvas.getWidth();
        drawingCanvasHeight = (int) drawingCanvas.getHeight();
        listCanvas = new ArrayList<>();
        // 加载默认图片
        try {
            File asImageFile = new File("./服务器/AutoSave.png");
            if (asImageFile.exists()) {
                Image image = new Image(new FileInputStream(asImageFile));
                setImage(image);
            }
        }catch (IOException e){}
    }
    public void handleCommand(){
        //以下是绘图主函数
        //当鼠标按下
        if(SeverStatus.mouse.equals("press")) {
            {
                Canvas c = new Canvas(drawingCanvasWidth, drawingCanvasHeight);
                gc = c.getGraphicsContext2D();
                //获取鼠标位置
                x1 = SeverStatus.mouseX1;
                y1 = SeverStatus.mouseY1;
                if (SeverStatus.toolName.equals("OVAL") || SeverStatus.toolName.equals("RECTANGLEZ") || SeverStatus.toolName.equals("RECTANGLEY")) {
                    if (!SeverStatus.fill) {
                        gc.setLineWidth(SeverStatus.lineSize);
                        setStatus(c, SeverStatus.color, false);
                    } else if (SeverStatus.fill) {
                        gc.setLineWidth(SeverStatus.lineSize);
                        setStatus(c, SeverStatus.color, true);
                    }
                }else if(SeverStatus.toolName.equals("BARREL")){
                    setStatus(c, SeverStatus.color, true);
                }else{
                    gc.setLineWidth(SeverStatus.lineSize);
                    setStatus(c, SeverStatus.color, false);
                }

                if (SeverStatus.toolName.equals("RUBBER"))
                    gc.setStroke(Color.WHITE);

                //这是个特例哈，因为可以直接绘出文字
                if(SeverStatus.toolName.equals("TEXT")){
                    gc.setLineWidth(1);
                    gc.setFont(Font.font(SeverStatus.fontFamily, SeverStatus.fontSize));
                    gc.setStroke(SeverStatus.color);
                    gc.strokeText(SeverStatus.myText, SeverStatus.mouseX1, SeverStatus.mouseY1);
                    autoSave();
                }
                if(SeverStatus.toolName.equals("POLYGON")){
                    if(SeverStatus.drawPoly) {
                        if(SeverStatus.fill){
                            gc.fillPolygon(x, y, numPoints);
                        }
                        else{
                            gc.strokePolygon(x, y, numPoints);
                        }
                        x=new double[20];
                        y=new double[20];
                        numPoints=0;
                        autoSave();
                    }
                    else{
                        x[numPoints]=x1;//放入一个点
                        y[numPoints]=y1;
                        numPoints++;
                    }
                }
                listCanvas.add(c);
                content.getChildren().add(c);
            }
        }
        //当鼠标拖动，触发事件
        if(SeverStatus.mouse.equals("drag")) {
            if (SeverStatus.toolName.equals("PEN") || SeverStatus.toolName.equals("RUBBER")) {
                gc.lineTo(SeverStatus.mouseX1, SeverStatus.mouseY1);
                gc.stroke();
            }
        }
        //当鼠标释放，绘出图形
        if(SeverStatus.mouse.equals("release")) {
            //再次获取鼠标位置
            x2 = SeverStatus.mouseX2;
            y2 = SeverStatus.mouseY2;
            double width = x2 - x1;
            double height = y2 - y1;
            if (SeverStatus.toolName.equals("LINE")) {
                gc.moveTo(x1, y1);
                gc.lineTo(x2, y2);
                gc.stroke();
            } else if (SeverStatus.toolName.equals("OVAL")) {
                if (width < 0) {
                    width = -width;
                    x1 = x1 - width;
                }
                if (height < 0) {
                    height = -height;
                    y1 = y1 - height;
                }
                if(SeverStatus.fill)
                    gc.fillOval(x1, y1, width, height);
                else
                    gc.strokeOval(x1, y1, width, height);
            } else if (SeverStatus.toolName.equals("RECTANGLEZ")) {
                if (width < 0) {
                    width = -width;
                    x1 = x1 - width;
                }
                if (height < 0) {
                    height = -height;
                    y1 = y1 - height;
                }
                if(SeverStatus.fill)
                    gc.fillRect(x1, y1, width, height);
                else
                    gc.strokeRect(x1, y1, width, height);
            } else if (SeverStatus.toolName.equals("RECTANGLEY")) {
                if (width < 0) {
                    width = -width;
                    x1 = x1 - width;
                }
                if (height < 0) {
                    height = -height;
                    y1 = y1 - height;
                }
                if(SeverStatus.fill)
                    gc.fillRoundRect(x1, y1, width, height, 30, 30);
                else
                    gc.strokeRoundRect(x1, y1, width, height, 30, 30);
            } else if (SeverStatus.toolName.equals("BARREL")) {
                gc.fillRect(0,0,drawingCanvasWidth, drawingCanvasHeight);
            }
            gc.stroke();
            autoSave();
        }
    }
    public VBox getCanvas() {
        return vbox;
    }
    public void setImage(Image newImage){
        gc.drawImage(newImage,0,0);
        gc.restore();
        listCanvas.add(drawingCanvas);
        autoSave();
    }
    // 清理门户
    public void clear() {
        Canvas c = new Canvas(drawingCanvasWidth, drawingCanvasHeight);
        gc = c.getGraphicsContext2D();
        // 填一张白色
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 800,800);
        gc.restore();
        listCanvas.add(c);
        content.getChildren().add(c);
    }
    // 这个函数旨在快速填充参数
    void setStatus(Canvas c, Color color, boolean f){
        gc = c.getGraphicsContext2D();
        if(f)
            gc.setFill(color);
        else
            gc.setStroke(color);
    }
    // 这个函数完成了撤销操作，这也是用链表的意义
    void undo() {
        if (!listCanvas.isEmpty()) {
            newCanvas = listCanvas.get(listCanvas.size()-1);
            content.getChildren().remove(newCanvas);
            listCanvas.remove(listCanvas.size()-1);
            autoSave();
        }
    }
    // 重做，与之前相似
    void redo() {
        if (newCanvas != null) {
            listCanvas.add(newCanvas);
            content.getChildren().add(newCanvas);
            newCanvas = null;
            autoSave();
        }
    }
    // 用来判断是否为空

    // 获取当前
    public RenderedImage getNewImage() {
        // 以下代码，旨在合成一个图层
        Canvas oneCanvas=new Canvas(800,800);
        // 新建一个快照
        SnapshotParameters mySP=new SnapshotParameters();
        mySP.setFill(Color.TRANSPARENT);// 设一个透明背景，这是必定
        for(Canvas thisCanvas:listCanvas){
            // 这个在其他地方感觉没啥用的功能难道是专门为了画板设计的吗...
            WritableImage thisImage=thisCanvas.snapshot(mySP,null);
            // 写入主图层
            oneCanvas.getGraphicsContext2D().drawImage(thisImage,0,0);
        }
        WritableImage myWI=new WritableImage(800,800);
        oneCanvas.snapshot(null,myWI);
        return SwingFXUtils.fromFXImage(myWI,null);
    }

    public void autoSave() {
        File imageFile = new File("./服务器/AutoSave.png");
        try {
            ImageIO.write(getNewImage(), "PNG", imageFile);
        } catch (IOException err){}
    }
}
