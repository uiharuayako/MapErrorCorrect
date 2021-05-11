package FXStage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;


public class SavePic {

    String url = "jdbc:mysql://localhost:3306/imagedb";
    Connection con = null;
    Statement stm = null;

    public SavePic() {
        System.out.println(123);
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/?serverTimezone=UTC",
                    "root",
                    "Test123456");
            stm = con.createStatement();
            stm.execute("USE imagedb");
            System.out.println(123);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void savePicNow() {
        File file = new File("D:/Images/Result/DFTWHU.jpg");
        try {
            Timestamp time1 = new Timestamp(System.currentTimeMillis());
            System.out.println(time1);
            InputStream is = new FileInputStream(file);
            PreparedStatement ps = con.prepareStatement("insert into image(img,description) values(?,?)");
            ps.setBinaryStream(1, is, (int) file.length());
            ps.setString(2, "this is a pic test");
            ps.executeUpdate();
            ps.clearParameters();
            is.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public void getPicNow() {
        try {
            OutputStream out = new FileOutputStream("D:/cc.jpg");

            Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = statement.executeQuery("select * from image LIMIT 1");
            while (rs.next()) {
                String description = rs.getString("description");
                Blob blob = rs.getBlob("img");
                out.write(blob.getBytes(1, (int) blob.length()));
            }
            out.close();
            con.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void getDBMetaInfo() {
        try {
            DatabaseMetaData dmd = con.getMetaData();
            System.out.println("数据库支持结果集滚动,非敏感的：" + dmd.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE));
            System.out.println("数据库支持结果集更新吗" + dmd.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE));
            con.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SavePic sp = new SavePic();
        sp.savePicNow();
        sp.getPicNow();
    }

}