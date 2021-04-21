package FXStage;

import javafx.scene.control.Button;

/**
 * @author Ayako
 */
public class ColorButton extends Button {
    String name;
    public ColorButton(String n){
        super();
        this.name = n;
    }
    public String getName(){
        return name;
    }
}
