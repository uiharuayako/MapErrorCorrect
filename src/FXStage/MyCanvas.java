package FXStage;

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
import java.awt.image.RenderedImage;// 这个...不算awt吧
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//这是一个画板，把许多画布合成而来，同时，包括一个状态栏。
//增加新功能，把每一个绘画动作储存起来
public class MyCanvas {
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
    MyEditBar myEditBar;
    //状态栏相关
    private Label curPos;
    private Label info;
    private Label polyLabel;
    private Label infoLabel;

    private HBox statusBar;
    //重做相关
    private Canvas newCanvas;

    public MyCanvas(MyEditBar editBar) {
        myEditBar = editBar;
        //网络声明
        mySocket = null;
        newCanvas = null;
        //状态栏声明
        numPoints = 0;
        statusBar = new HBox();
        polyLabel = new Label("多边形状态栏");
        info = new Label("工具状态栏");
        curPos = new Label("欢迎使用");
        infoLabel = new Label("当前位置没有纠错图形");
        infoLabel.setFont(Font.font("Microsoft YaHei", 16));
        infoLabel.setAlignment(Pos.CENTER_LEFT);
        polyLabel.setFont(Font.font("Microsoft YaHei", 16));
        polyLabel.setAlignment(Pos.CENTER);
        info.setFont(Font.font("Microsoft YaHei", 16));
        info.setAlignment(Pos.CENTER);
        curPos.setFont(Font.font("Microsoft YaHei", 16));
        curPos.setAlignment(Pos.CENTER_RIGHT);
        //以下是画板
        content = new Group();
        vbox = new VBox();
        x = new double[20];
        y = new double[20];
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10, 20, 0, 0));
        vbox.getChildren().add(content);
        drawingCanvas = new Canvas(850, 850);
        gc = drawingCanvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 850, 850);
        gc.restore();

        content.getChildren().add(drawingCanvas);
        setStatus(drawingCanvas, MyStatus.color, false);
        drawingCanvasWidth = (int) drawingCanvas.getWidth();
        drawingCanvasHeight = (int) drawingCanvas.getHeight();
        listCanvas = new ArrayList<>();
        // 加载默认图片
        try {
            File asImageFile = new File("./我的作品/" + MyStatus.mapName + "AutoSave.png");
            if (asImageFile.exists()) {
                Image image = new Image(new FileInputStream(asImageFile));
                setImage(image);
            }
        } catch (IOException e) {
        }
        //以下是绘图主函数
        //当鼠标移动
        drawingCanvas.setOnMouseMoved(event ->
        {
            update();
            //检测当前鼠标附近是否有对象图像
            infoLabel.setText("当前位置没有图形");
            for (int i = 0; i < myEditBar.posOfPois.size(); i++) {
                MyPoint searchPoi = myEditBar.posOfPois.get(i);
                //鼠标x-25<searchPoi.x<鼠标x+25
                //y也同理，反正在里面，不用for in也是为了获取行号
                if (event.getX() - 25 <= searchPoi.getX() && searchPoi.getX() <= event.getX() + 25 && event.getY() - 25 <= searchPoi.getY() && searchPoi.getY() <= event.getY() + 25) {
                    infoLabel.setText("备注：" + myEditBar.myInfo.get(i).infoStr);
                    break;
                }
            }
            if (numPoints <= 9) {
                polyLabel.setText("多边形状态: 边数:0" + numPoints + "/20  " + (MyStatus.drawPoly ? "下次按键绘图" : "下次按键记录"));
            } else {
                polyLabel.setText("多边形状态: 边数:" + numPoints + "/20  " + (MyStatus.drawPoly ? "下次按键绘图" : "下次按键记录"));
            }
            info.setText("当前工具:" + MyStatus.toolName + " 当前文字:" + MyStatus.myText);
            curPos.setText(String.format("(%03d, %03d)", (int) event.getX(), (int) event.getY()));
        });
        drawingCanvas.setOnMouseExited(event -> {
            update();
            curPos.setText("(000, 000)");
        });
        //当鼠标按下
        drawingCanvas.setOnMousePressed(event -> {
            Canvas c = new Canvas(drawingCanvasWidth, drawingCanvasHeight);
            gc = c.getGraphicsContext2D();
            //获取鼠标位置
            x1 = event.getX();
            y1 = event.getY();
            //将新的Canvas和GraphicsContext的鼠标事件同步以实现多次绘图
            c.setOnMousePressed(drawingCanvas.getOnMousePressed());
            c.setOnMouseDragged(drawingCanvas.getOnMouseDragged());
            c.setOnMouseReleased(drawingCanvas.getOnMouseReleased());
            c.setOnMouseMoved(drawingCanvas.getOnMouseMoved());
            c.setOnMouseExited(drawingCanvas.getOnMouseExited());
            if (MyStatus.toolName.equals("OVAL") || MyStatus.toolName.equals("RECTANGLEZ") || MyStatus.toolName.equals("RECTANGLEY")) {
                // 储存一个点
                MyStatus.addPoint((int) x1, (int) y1);
                if (!MyStatus.fill) {
                    gc.setLineWidth(MyStatus.lineSize);
                    setStatus(c, MyStatus.color, false);
                } else if (MyStatus.fill) {
                    gc.setLineWidth(MyStatus.lineSize);
                    setStatus(c, MyStatus.color, true);
                }
            } else if (MyStatus.toolName.equals("PIN")) {
                setStatus(c, MyStatus.color, true);
            } else {
                gc.setLineWidth(MyStatus.lineSize);
                setStatus(c, MyStatus.color, false);
            }

            if (MyStatus.toolName.equals("RUBBER")) {
                gc.setStroke(Color.WHITE);
            }
            if (MyStatus.toolName.equals("LINE")) {
                // 储存一个点
                MyStatus.addPoint((int) x1, (int) y1);
            }
            //这是个特例哈，因为可以直接绘出文字
            //钉子也是个特例
            if (MyStatus.toolName.equals("TEXT")) {
                // 储存一个点
                MyStatus.addPoint((int) x1, (int) y1);
                gc.setLineWidth(1);
                gc.setFont(Font.font(MyStatus.fontFamily, MyStatus.fontSize));
                gc.setStroke(MyStatus.color);
                gc.strokeText(MyStatus.myText, event.getX(), event.getY());
            } else if (MyStatus.toolName.equals("PIN")) {
                // 储存一个点
                MyStatus.addPoint((int) x1, (int) y1);
                gc.fillOval(x1 - 2.5 * MyStatus.lineSize, y1 - 2.5 * MyStatus.lineSize, 5 * MyStatus.lineSize, 5 * MyStatus.lineSize);
            }
            if (MyStatus.toolName.equals("POLYGON")) {
                if (MyStatus.drawPoly) {
                    if (MyStatus.fill) {
                        setStatus(c, MyStatus.color, true);
                        gc.fillPolygon(x, y, numPoints);
                    } else {
                        setStatus(c, MyStatus.color, false);
                        gc.strokePolygon(x, y, numPoints);
                    }
                    x = new double[20];
                    y = new double[20];
                    numPoints = 0;
                    autoSave();
                } else {
                    // 储存一个点
                    MyStatus.addPoint((int) x1, (int) y1);
                    x[numPoints] = x1;//放入一个点
                    y[numPoints] = y1;
                    numPoints++;
                }
            }
            listCanvas.add(c);
            content.getChildren().add(c);
        });
        //当鼠标拖动，触发事件
        drawingCanvas.setOnMouseDragged(event ->

        {
            //提供状态栏的
            curPos.setText(String.format("(%03d, %03d)", (int) event.getX(), (int) event.getY()));
            if ("PEN".equals(MyStatus.toolName) || MyStatus.toolName.equals("RUBBER")) {
                gc.lineTo(event.getX(), event.getY());
                MyStatus.addPoint((int) event.getX(), (int) event.getY());
                gc.stroke();
            }
        });
        //当鼠标释放，绘出图形
        drawingCanvas.setOnMouseReleased(event -> {
            // 再次获取鼠标位置
            x2 = event.getX();
            y2 = event.getY();
            // 储存一个点，且仅有"LINE""OVAL""RECTANGLEZ""RECTANGLEY"这四个状态需要记录
            if (MyStatus.toolName.equals("LINE") || MyStatus.toolName.equals("OVAL") || MyStatus.toolName.equals("RECTANGLEZ") || MyStatus.toolName.equals("RECTANGLEY")) {
                MyStatus.addPoint((int) x2, (int) y2);
            }
            double width = x2 - x1;
            double height = y2 - y1;
            if (MyStatus.toolName.equals("LINE")) {
                gc.moveTo(x1, y1);
                gc.lineTo(x2, y2);

                gc.stroke();
            } else if (MyStatus.toolName.equals("OVAL")) {
                if (width < 0) {
                    width = -width;
                    x1 = x1 - width;
                }
                if (height < 0) {
                    height = -height;
                    y1 = y1 - height;
                }
                if (MyStatus.fill) {
                    gc.fillOval(x1, y1, width, height);
                } else {
                    gc.strokeOval(x1, y1, width, height);
                }
            } else if (MyStatus.toolName.equals("RECTANGLEZ")) {
                if (width < 0) {
                    width = -width;
                    x1 = x1 - width;
                }
                if (height < 0) {
                    height = -height;
                    y1 = y1 - height;
                }
                if (MyStatus.fill) {
                    gc.fillRect(x1, y1, width, height);
                } else {
                    gc.strokeRect(x1, y1, width, height);
                }
            } else if (MyStatus.toolName.equals("RECTANGLEY")) {
                if (width < 0) {
                    width = -width;
                    x1 = x1 - width;
                }
                if (height < 0) {
                    height = -height;
                    y1 = y1 - height;
                }
                if (MyStatus.fill) {
                    gc.fillRoundRect(x1, y1, width, height, 30, 30);
                } else {
                    gc.strokeRoundRect(x1, y1, width, height, 30, 30);
                }
            }
            gc.stroke();
            if (!MyStatus.toolName.equals("POLYGON")) {
                autoSave();
            }
        });
        //以下为最艰难的部分：拿文件内容绘图，一次创建canvas过程中只调用一次这个函数
        //之前就想做这个功能，技术原因一直没做
        File file = new File(MyStatus.mapName + ".mec");
        if (file.exists()) {
            myEditBar.loadInfo();
        }
        drawFromFile();
    }

    public void drawFromFile() {
        for (InfoLine myLine : myEditBar.myInfo) {
            // 遍历myInfo
            loadLine(myLine);
            // 开始模拟一个正常画图
            x1 = MyStatus.points.get(0).getX();
            y1 = MyStatus.points.get(0).getY();
            if (MyStatus.points.size() > 1) {
                x2 = MyStatus.points.get(1).getX();
                y2 = MyStatus.points.get(1).getY();
            }
            double width = x2 - x1;
            double height = y2 - y1;
            Canvas c = new Canvas(drawingCanvasWidth, drawingCanvasHeight);
            gc = c.getGraphicsContext2D();
            //将新的Canvas和GraphicsContext的鼠标事件同步以实现多次绘图
            c.setOnMousePressed(drawingCanvas.getOnMousePressed());
            c.setOnMouseDragged(drawingCanvas.getOnMouseDragged());
            c.setOnMouseReleased(drawingCanvas.getOnMouseReleased());
            c.setOnMouseMoved(drawingCanvas.getOnMouseMoved());
            c.setOnMouseExited(drawingCanvas.getOnMouseExited());
            if (MyStatus.toolName.equals("OVAL") || MyStatus.toolName.equals("RECTANGLEZ") || MyStatus.toolName.equals("RECTANGLEY")) {
                if (!MyStatus.fill) {
                    gc.setLineWidth(MyStatus.lineSize);
                    setStatus(c, MyStatus.color, false);
                } else if (MyStatus.fill) {
                    gc.setLineWidth(MyStatus.lineSize);
                    setStatus(c, MyStatus.color, true);
                }
            } else if (MyStatus.toolName.equals("PIN")) {
                setStatus(c, MyStatus.color, true);
            } else {
                gc.setLineWidth(MyStatus.lineSize);
                setStatus(c, MyStatus.color, false);
            }

            if (MyStatus.toolName.equals("RUBBER")) {
                gc.setStroke(Color.WHITE);
            }
            if (MyStatus.toolName.equals("LINE")) {
                gc.moveTo(x1, y1);
                gc.lineTo(x2, y2);
                gc.stroke();
            }
            //以上是一些设置
            if (MyStatus.toolName.equals("TEXT")) {
                gc.setLineWidth(1);
                gc.setFont(Font.font(MyStatus.fontFamily, MyStatus.fontSize));
                gc.setStroke(MyStatus.color);
                gc.strokeText(MyStatus.myText, x1, y1);
            } else if (MyStatus.toolName.equals("PIN")) {
                gc.fillOval(x1 - 2.5 * MyStatus.lineSize, y1 - 2.5 * MyStatus.lineSize, 5 * MyStatus.lineSize, 5 * MyStatus.lineSize);
            }
            if (MyStatus.toolName.equals("POLYGON")) {
                for (int i = 0; i < MyStatus.points.size(); i++) {
                    x[i] = MyStatus.points.get(i).getX();
                    y[i] = MyStatus.points.get(i).getY();
                }
                if (MyStatus.fill) {
                    setStatus(c, MyStatus.color, true);
                    gc.fillPolygon(x, y, MyStatus.points.size());
                } else {
                    setStatus(c, MyStatus.color, false);
                    gc.strokePolygon(x, y, MyStatus.points.size());
                }
            }
            // 以下为改写的原来是鼠标释放的内容
            if (MyStatus.toolName.equals("OVAL")) {
                if (width < 0) {
                    width = -width;
                    x1 = x1 - width;
                }
                if (height < 0) {
                    height = -height;
                    y1 = y1 - height;
                }
                if (MyStatus.fill) {
                    gc.fillOval(x1, y1, width, height);
                } else {
                    gc.strokeOval(x1, y1, width, height);
                }
            } else if (MyStatus.toolName.equals("RECTANGLEZ")) {
                if (width < 0) {
                    width = -width;
                    x1 = x1 - width;
                }
                if (height < 0) {
                    height = -height;
                    y1 = y1 - height;
                }
                if (MyStatus.fill) {
                    gc.fillRect(x1, y1, width, height);
                } else {
                    gc.strokeRect(x1, y1, width, height);
                }
            } else if (MyStatus.toolName.equals("RECTANGLEY")) {
                if (width < 0) {
                    width = -width;
                    x1 = x1 - width;
                }
                if (height < 0) {
                    height = -height;
                    y1 = y1 - height;
                }
                if (MyStatus.fill) {
                    gc.fillRoundRect(x1, y1, width, height, 30, 30);
                } else {
                    gc.strokeRoundRect(x1, y1, width, height, 30, 30);
                }
            }
            // 笔画情况
            if ("PEN".equals(MyStatus.toolName) || "RUBBER".equals(MyStatus.toolName)) {
                for (int i = 0; i < MyStatus.points.size(); i++) {
                    MyPoint poi = MyStatus.points.get(i);
                    gc.lineTo(poi.getX(), poi.getY());
                }
            }
            listCanvas.add(c);
            content.getChildren().add(c);
            gc.stroke();
            MyStatus.points.clear();
        }
    }

    public void setImage(Image newImage) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 850, 850);
        gc.drawImage(newImage, 0, 0);
        gc.stroke();
        listCanvas.add(drawingCanvas);
        autoSave();
    }

    void update() {
        if (MyStatus.isUpdate) {
            try {
                File asImageFile = new File("./我的作品/" + MyStatus.mapName + "AutoSave.png");
                if (asImageFile.exists()) {
                    Image image = new Image(new FileInputStream(asImageFile));
                    setImage(image);
                }
            } catch (IOException ignored) {
            }

            MyStatus.isUpdate = false;
        }
    }

    // 清理门户
    public void clear() {
        // 填一张白色
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 850, 850);
        gc.restore();
        listCanvas.add(drawingCanvas);
        autoSave();
    }

    // 这个函数旨在快速填充参数
    void setStatus(Canvas c, Color color, boolean f) {
        gc = c.getGraphicsContext2D();
        if (f) {
            gc.setFill(color);
        } else {
            gc.setStroke(color);
        }
    }

    // 这个函数完成了撤销操作，这也是用链表的意义
    void undo() {
        if (!listCanvas.isEmpty()) {
            newCanvas = listCanvas.get(listCanvas.size() - 1);
            content.getChildren().remove(newCanvas);
            listCanvas.remove(listCanvas.size() - 1);
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
    public List<Canvas> getList() {
        return listCanvas;
    }

    public VBox getCanvas() {
        return vbox;
    }

    public HBox getStatusBar() {
        class mySP extends Separator {
            mySP() {
                setOrientation(Orientation.VERTICAL);
            }
        }
        statusBar.getChildren().addAll(infoLabel, new mySP(), info, new mySP(), polyLabel, new mySP(), curPos);
        statusBar.setAlignment(Pos.CENTER);
        statusBar.setSpacing(10);
        statusBar.setPadding(new Insets(5, 30, 5, 100));
        return statusBar;
    }

    // 获取当前
    public RenderedImage getNewImage() {
        // 以下代码，旨在合成一个图层
        Canvas oneCanvas = new Canvas(850, 850);
        // 新建一个快照
        SnapshotParameters mySP = new SnapshotParameters();
        mySP.setFill(Color.TRANSPARENT);
        WritableImage myWI = new WritableImage(850, 850);
        content.snapshot(null, myWI);
        return SwingFXUtils.fromFXImage(myWI, null);
    }

    public void autoSave() {
        File imageFile = new File("./我的作品/" + MyStatus.mapName + "AutoSave.png");
        try {
            ImageIO.write(getNewImage(), "PNG", imageFile);
            // 在逻辑上，地图每有一次更新就多加一行字
            if (!MyStatus.points.isEmpty()) {
                addLine();
            }
        } catch (IOException ignored) {
        }
    }

    // 向本地文件中增加指定内容
    public static void addContent(String filepath, String content) {
        FileWriter fw = null;
        try {
            //如果文件存在，则追加内容；如果文件不存在，则创建文件
            File f = new File(filepath);
            fw = new FileWriter(f, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fw);
        pw.println(content);
        pw.flush();
        try {
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 向同名mec（MapErrorCorrect）文本文件中写入当前操作，直接写入点
    public void addLine() {
        // 将文本框内信息写入Status
        MyStatus.infoText = myEditBar.infoArea.getText();
        // points字符串的结构应被统一规制为(x1,x2,x3...)(y1,y2,y3...)
        // 上一句话无效，考虑到初始化的方便，还是规制为(x,y)(x1,y1)形式，且这些操作完全由MyStatus完成
        addContent(MyStatus.mapName + ".mec", MyStatus.status2Str() + "$点开始$" + MyStatus.points2Str() + "$点结束$" + "$备注开始$" + MyStatus.infoText + "$备注结束$");
        // 储存之后就清空掉
        MyStatus.points.clear();
    }

    // 这个函数旨在把thisLine里的两种信息load进MyStatus里
    public void loadLine(InfoLine thisLine) {
        MyStatus.setStatus(thisLine.statusStr);
        MyStatus.str2Points(thisLine.pointStr);
    }
    // 如上所述，必须完成2个功能，一个是把x，y列表转换为points串结构，第二个是把points串结构还原为points
}
