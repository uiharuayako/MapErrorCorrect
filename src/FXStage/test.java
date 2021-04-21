package FXStage;


import javafx.scene.paint.Color;

import java.awt.*;
import java.io.*;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

class SearchUpdateTxt {
    public static void main(String[] args) throws Exception {
        File file = new File("F:\\runoob.txt");

        String indexstr = "/192.168.1.104";//根据该字符串，查找其在txt所在的一行，然后替换成新的一行，其它行数据不变
        String newstr = "/192.168.1.104:21514 data: +RC0120000444444444444444444444444444000009+25.16";//新的一行

        BufferedReader br = new BufferedReader(new FileReader(file));//读文件
        StringBuffer bf = new StringBuffer();
        String rl = null;//临时的每行数据

        while ((rl = br.readLine()) != null) {
            System.out.println(rl.indexOf(indexstr));//打印该字符串是否在此行，是则输出0，否则输出-1
            if (rl.indexOf(indexstr) == 0) { //或者!r1.startsWith(indexstr)
                bf.append(newstr+"\r\n");
            }else {
                bf.append(rl+"\r\n");
            }
        }
        br.close();

        BufferedWriter out = new BufferedWriter(new FileWriter(file));//写入文件
        out.write(bf.toString());//把bf写入文件
        out.flush();//一定要flush才能写入完成
        out.close();//关闭
    }
}
public class test {
    public static void main(String[] args) {
        String s1="第一句话";
        String s2="第二句话";
        String s3="第三句话";
        String s4="第四句话";
        ArrayList<String> s=new ArrayList<>();
        s.add(s1);
        s.add(s2);
        s.add(s3);
        s.add(s4);

        System.out.println(s.size());
        System.out.println(s.get(0));
        String lineStr="$开始$PEN$工具名$1.0$粗细$12.0$字体大小$AIGDT$字体类型$0x000000ff$颜色$Uiharu$文字$false$填充$$点开始$$点结束$$备注开始$123$备注结束$";
        // 截取状态信息
        try {
            BufferedReader br = new BufferedReader(new FileReader(MyStatus.mapName + ".mec"));//读文件
            String bf;
            bf=br.readLine();
            System.out.println(bf);
            System.out.println(bf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String rl = null;//临时的每行数据
        System.out.println(lineStr.substring(0,lineStr.indexOf("$填充$")+4));
        System.out.println(MyStatus.subString(lineStr,"$点开始$","$点结束$"));
        String mys="(123,4567)(123,999)(456.788)";
        System.out.println(mys.substring(mys.indexOf(')')+1));
        MyPoint point = new MyPoint(1,2);
        MyPoint po2=new MyPoint(3,4);
        MyPoint[] pos={point,po2};
        System.out.println(countCharFake("an apple a day",'a'));
        MyPoint[] resPois = new MyPoint[10];
        resPois[0]=new MyPoint(1,2);
        /*
        System.out.println("123");
        Color c=Color.web(("FDA098"));
        Color d=Color.BLUE;
        System.out.println(d);
        Color f=Color.web(d.toString());
        System.out.println(f);
        */
        System.out.println(subString("(1,2)(3,4)","(",")"));
        addContent("./1.txt","999\n888");
    }
    public static String subString(String str, String strStart, String strEnd) {
        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);
        /* 开始截取 */
        return str.substring(strStartIndex, strEndIndex).substring(strStart.length());
    }
    public static int countCharFake(String str,char ch){
        int freq=0;
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)==ch){
                freq++;
            }
        }
        return freq;
    }
    public static void addContent(String filepath , String content) {
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
}
