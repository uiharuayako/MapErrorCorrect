package FXStage;

import java.io.*;
import java.sql.*;

// 这是一个用来操作数据库的类
public class MyDBProcess {
    // 保存当前图片信息
    public static void savePic() {
        // 所有操作必须正确登录账户才能进行
        if (MyStatus.rightAccount) {
            // 保存图像有三块，第一块，保存fileName
            // 第二块，保存图片
            // 第三块，保存mec
            // 又有几种情况，有图片就不存图片和名字，这两个不能改，没就三个都存
            // 设定一个变量来判断
            boolean isExist = false;
            try {
                ResultSet rs = MyStatus.myStm.executeQuery("select fileName from pics where fileName = '" + MyStatus.mapName + "'");
                // 如果结果非空
                while (rs.next()) {
                    isExist = true;
                }
                // 如果存在
                if (isExist) {
                    MyStatus.myStm.executeUpdate("update pics set fileErrors = '" + mec2String() + "' where fileName = '" + MyStatus.mapName + "'");
                } else {
                    // 如果不存在
                    PreparedStatement ps = MyStatus.myCon.prepareStatement("insert into pics(fileName,imgData,fileErrors) values(?,?,?)");
                    InputStream is = new FileInputStream(MyStatus.originImg);
                    ps.setString(1, MyStatus.mapName);
                    ps.setBinaryStream(2, is, (int) MyStatus.originImg.length());
                    ps.setString(3, mec2String());
                    ps.executeUpdate();
                    ps.clearParameters();
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
    // 获取当前mapname的图片
    public static void getPicNow() {
        // 所有操作必须正确登录账户才能进行
        if (MyStatus.rightAccount) {
            try {
                OutputStream out = new FileOutputStream("./我的作品/" + MyStatus.mapName + ".png");
                Statement statement = MyStatus.myCon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = statement.executeQuery("select imgData from pics where fileName = '" + MyStatus.mapName + "'");
                while (rs.next()) {
                    Blob blob = rs.getBlob("imgData");
                    out.write(blob.getBytes(1, (int) blob.length()));
                }
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // 添加记录
    public static void addLog(String info){
        // 当然也要检查正确的账户
        if(MyStatus.rightAccount){
            try{
                PreparedStatement ps=MyStatus.myCon.prepareStatement("insert into logs(userName,time,infoLine) values(?,?,?)");
                ps.setString(1,MyStatus.nickName);
                ps.setTimestamp(2,MyStatus.getNowTime());
                ps.setString(3,info);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public static String mec2String() {
        StringBuilder buffer = new StringBuilder();
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new FileReader(MyStatus.getMecPath()));
            String s;
            while ((s = bf.readLine()) != null) {
                //使用readLine方法，一次读一行
                buffer.append(s.trim()).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}