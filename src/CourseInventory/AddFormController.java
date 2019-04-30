
package CourseInventory;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

////////////////////////////////////////////////////////////////////////////////
// AddFormController.java
// ============
// Controller class for the add form.
// Used to add courses to main model
//
// AUTHOR: Vincent Romani (vromani@outlook.com)
// CREATED: 2018-03-1
// UPDATED: 2018-03-27
////////////////////////////////////////////////////////////////////////////////

public class AddFormController implements Initializable {
    private Stage stage;
    private Course course;
    private CourseInventoryModel model;
    
    @FXML
    private TextField textId;
    @FXML
    private TextField textTitle;
    @FXML
    private TextField textCredit;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonCancel;
    @FXML
    private ChoiceBox<String> choiceBoxCategory;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new CourseInventoryModel();
    }
    
    //handles button when add is clicked
    @FXML
    private void handleButtonAdd(ActionEvent event) {
        //creates error dialog
        Alert alert = new Alert(Alert.AlertType.ERROR);
        
        //gets the ID from textbox
        String id = textId.getText().toUpperCase();
        
        //validates ID to ensure proper format
        if(!model.validateId(id)){
            alert.setContentText("ID must be 4 alphabets followed by 5 digits");
            alert.show(); return;
        }
        
        //gets the title from textbox
        String title = textTitle.getText();
        
        //validates title is not empty
        if(title==null || title.length()==0){
            alert.setContentText("Title cannot be empty");
            alert.show(); return;
        }
        
        //initializes credit and sets to 
        int credit = 0;
        
        //checks if the credit entered is a valid integer greater than 0
        try{
            credit = Integer.parseInt(textCredit.getText());
        }
        catch(NumberFormatException e){
            alert.setContentText("Credit must be numeric.");
            alert.show(); return;
        }
        if(credit<=0){
            alert.setContentText("Credit must be greater than 0.");
            alert.show(); return;
        }
        
        //gets selected choice box option 
        //(Used choice box instead of combo box. Same thing but combo is meant for smaller data amounts)
        String category = choiceBoxCategory.getValue();
   
        if(category == null){
            alert.setContentText("Must choose a category");
            alert.show(); return;
        }
        
        //adds course
        model.addCourse(id,title,credit,category);
        //sets the selected course
        model.setSelectedCourse(new Course(id,title,credit,category));
        //closes window
        stage.close();
    }

    //closes window if cancel is pressed
    @FXML
    private void handleButtonCancel(ActionEvent event) {
        stage.close();
    }

    //sets the stage that is passed
    public void setStage(Stage stage) {           this.stage = stage;}
    //sets model that is passed
    void setModel(CourseInventoryModel model) {   this.model = model;}
    //populates the comboBox
    void setChoiceBox(){ choiceBoxCategory.getItems().setAll(this.model.getCategories());}
    
    
    
    
}
