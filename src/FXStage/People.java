package FXStage;

import javafx.beans.property.SimpleStringProperty;

public class People {
    public SimpleStringProperty name;
    public SimpleStringProperty ID;
    public SimpleStringProperty toolName;
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
    public void setToolName(String toolName){
        this.toolName.set(toolName);
    }
    public String getToolName(){
        return toolName.get();
    }
    People(String n,String i){
        name = new SimpleStringProperty(n);
        ID = new SimpleStringProperty(i);
        toolName = new SimpleStringProperty("PEN");
    }
}
