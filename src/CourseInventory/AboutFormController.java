
package CourseInventory;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

////////////////////////////////////////////////////////////////////////////////
// AboutFormController.java
// ============
// Controller class for the About form.
//
// AUTHOR: Vincent Romani (vromani@outlook.com)
// CREATED: 2018-03-1
// UPDATED: 2018-03-27
////////////////////////////////////////////////////////////////////////////////

public class AboutFormController implements Initializable {

    @FXML
    private Button buttonClose;
    private Stage stage;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}    

    //close button
    @FXML
    private void handleButtonClose(ActionEvent event) {
        if(stage != null){
            stage.close();
        }
        
    }
    //sets stage
    public void setStage(Stage stage){
        this.stage = stage;
    }
    
}
