package FXStage;

import com.sun.xml.internal.bind.v2.model.core.ID;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ayako
 */
public class NameList {

    public static List<People> myNameList=new ArrayList<>();
    void addPeople(String n, Timestamp i){
        for(People thisPeople:myNameList){
            // 已经存在这个人，改名
            if(thisPeople.getID().equals(i)) {
                thisPeople.setName(n);
                return;
            }
        }
        // 不存在这个人，添加
        myNameList.add(new People(n,i));
    }

    void deletePeople(String id){
        myNameList.removeIf(thisPeople -> thisPeople.ID.equals(id));
    }
}
