package FXStage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ayako
 */
public class ImageList {
    public static List<ImageCell> myImageList=new ArrayList<>();
    public static void update(){
        try {
            PreparedStatement ps=MyStatus.myCon.prepareStatement("select fileName from pics");
            ResultSet rs= ps.executeQuery();
            while (rs.next()){
                myImageList.add(new ImageCell(rs.getString(1)));
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }
}
