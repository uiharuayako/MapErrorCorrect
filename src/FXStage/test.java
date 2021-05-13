package FXStage;


import javafx.scene.paint.Color;

import java.awt.*;
import java.io.*;

import java.io.File;
import java.io.FileWriter;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;


public class test {
    public static void main(String[] args) {
        try{
            new File(("12345.txt")).mkdir();
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

            MyStatus.myCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/?serverTimezone=UTC",
                    "root",
                    "Test123456");
            MyStatus.myStm = MyStatus.myCon.createStatement();
            MyStatus.myStm.execute("use maperrordb");
//            ResultSet rs=null;
//            PreparedStatement ps=null;
//            ps=MyStatus.myCon.prepareStatement("select pwMD5 from maperrordb.user where name = '哒哒哒'");
//            rs= ps.executeQuery();
//            while (rs.next()){
//                System.out.println(123);
//                System.out.println(rs.getRow());
//            }
//            System.out.println(1);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
