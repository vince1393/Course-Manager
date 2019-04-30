package CourseInventory;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

////////////////////////////////////////////////////////////////////////////////
// SearchFormController.java
// ============
// Controller class for the search form.
//
//
// AUTHOR: Vincent Romani (vromani@outlook.com)
// CREATED: 2018-03-1
// UPDATED: 2018-03-27
////////////////////////////////////////////////////////////////////////////////

public class SearchFormController implements Initializable {

    //class members
    private Stage stage;
    private CourseInventoryModel model;
    
    @FXML
    private TextField textSearch;
    @FXML
    private TableColumn<Course, String> columnId;
    @FXML
    private TableColumn<Course, String> columnTitle;
    @FXML
    private TableColumn<Course, Integer> columnCredit;
    @FXML
    private TableColumn<Course, String> columnCategory;
    @FXML
    private Button buttonSearch;
    @FXML
    private Button buttonSelect;
    @FXML
    private Button buttonCancel;
    @FXML
    private TableView<Course> tableCourse;
    @FXML
    private RadioButton toggleAll;
    @FXML
    private ToggleGroup searchGroup;
    @FXML
    private RadioButton toggleCategory;
    @FXML
    private RadioButton toggleCourseId;
    @FXML
    private RadioButton toggleTitle;
    @FXML
    private RadioButton toggleCredit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //creates change listener to be used on the seatch textbox and searchgroup.
        ChangeListener listener = (ChangeListener) (observable, oldValue, newValue) -> {search();};
        
        //adds the listener to the textbox and the toggle buttons.
        //this will make it search if a toggle button is changed or anything is typed in the textbox
        textSearch.textProperty().addListener(listener);
        searchGroup.selectedToggleProperty().addListener(listener);

        //set table contraints        
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnCredit.setCellValueFactory(new PropertyValueFactory<>("credit"));
        columnCategory.setCellValueFactory(new PropertyValueFactory<>("cat"));
        
        //set user data for toggle buttons
        toggleAll.setUserData("All");
        toggleCategory.setUserData("Category");
        toggleCourseId.setUserData("Course ID");
        toggleTitle.setUserData("Title");
        toggleCredit.setUserData("Credit");
        
        tableCourse.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }

    // set methods
    void setStage(Stage stage) {                  this.stage = stage;}
    void setModel(CourseInventoryModel model) {   this.model = model;}

    //search button that searches as well
    @FXML
    private void handleSearch(ActionEvent event) {
        search();
    }

    //select button method, used to pass information to the model object and close the window.
    @FXML
    private void handleSelect(ActionEvent event) {
        model.setSelectedCourse(tableCourse.getSelectionModel().getSelectedItem());
        stage.close();
    }

    //cancel button that clears selected course and closes window
    @FXML
    private void handleCancel(ActionEvent event) {
        model.setSelectedCourse(null);
        stage.close();
    }

    //updates the table.
    void updateTableCourse() {
        tableCourse.setItems(FXCollections.observableList(model.getCourses()));
    }

    //search method that searches based on selected toggle. Uses switch for more streamlined code.
    void search() {
        switch (searchGroup.getSelectedToggle().getUserData().toString()) {
            case "Course ID":
                tableCourse.setItems(FXCollections.observableList(model.findCoursesByID(textSearch.getText())));
                break;
            case "Category":
                tableCourse.setItems(FXCollections.observableList(model.findCoursesByCategory(textSearch.getText())));
                break;
            case "All":
                tableCourse.setItems(FXCollections.observableList(model.findCourses(textSearch.getText())));
                break;
            case "Credit":
                tableCourse.setItems(FXCollections.observableList(model.findCoursesByCredit(textSearch.getText())));
                break;
            case "Title":
                tableCourse.setItems(FXCollections.observableList(model.findCoursesByTitle(textSearch.getText())));
                break;
            default:
                break;
        }
    }

}
