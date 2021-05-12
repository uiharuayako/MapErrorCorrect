package FXStage;

import javafx.beans.property.SimpleStringProperty;

import java.sql.Timestamp;

/**
 * @author Ayako
 */
public class People {
    public SimpleStringProperty name;
    public SimpleStringProperty ID;
    public void setID(String id){
        ID.set(id);
    }
    public String getID(){
        return ID.get();
    }
    public void setName(String name){
        this.name.set(name);
    }
    public String getName(){
        return name.get();
    }
    People(String n, Timestamp i){
        name = new SimpleStringProperty(n);
        ID = new SimpleStringProperty(i.toString());
    }
}
