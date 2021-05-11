package FXStage;

import javafx.beans.property.SimpleStringProperty;

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
    People(String n,String i){
        name = new SimpleStringProperty(n);
        ID = new SimpleStringProperty(i);
    }
}
