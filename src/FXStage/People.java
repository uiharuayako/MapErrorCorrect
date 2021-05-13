package FXStage;

import javafx.beans.property.SimpleStringProperty;

import java.sql.Timestamp;

/**
 * @author Ayako
 */
public class People {
    public SimpleStringProperty name;
    public SimpleStringProperty isOnline;


    public void setIsOnline(String id){
        isOnline.set(id);
    }
    public String getIsOnline(){
        return isOnline.get();
    }
    public void setName(String name){
        this.name.set(name);
    }
    public String getName(){
        return name.get();
    }
    People(String n, boolean is){
        name = new SimpleStringProperty(n);
        isOnline = new SimpleStringProperty(is?"在线":"离线");
    }
}
