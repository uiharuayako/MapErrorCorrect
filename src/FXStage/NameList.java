package FXStage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ayako
 */
public class NameList {

    public static List<People> myNameList=new ArrayList<>();
    public static void update(){
        try {
            myNameList.clear();
            PreparedStatement ps=MyStatus.myCon.prepareStatement("select name,online from user");
            ResultSet rs= ps.executeQuery();
            while (rs.next()){
                myNameList.add(new People(rs.getString("name"),rs.getBoolean("online")));
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }
}
