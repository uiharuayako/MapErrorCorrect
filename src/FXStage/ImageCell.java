package FXStage;

import javafx.beans.property.SimpleStringProperty;

import java.sql.Timestamp;

/**
 * @author Ayako
 */
public class ImageCell {
    public SimpleStringProperty name;
    public void setName(String name){
        this.name.set(name);
    }
    public String getName(){
        return name.get();
    }
    ImageCell(String n){
        name = new SimpleStringProperty(n);
    }
}
