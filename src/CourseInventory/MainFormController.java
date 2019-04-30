
package CourseInventory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


////////////////////////////////////////////////////////////////////////////////
// MainFormController.java
// ============
// Controller class for the main form.
// Creates all other controllers as well.
//
// AUTHOR: Vincent Romani (vromani@outlook.com)
// CREATED: 2018-03-1
// UPDATED: 2018-03-27
////////////////////////////////////////////////////////////////////////////////

public class MainFormController implements Initializable {

    //member vars
    private Stage stage; // remember its stage reference here
    private CourseInventoryModel model;
    @FXML
    private Button buttonSearch;
    @FXML
    private Button buttonEdit;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonSave;
    @FXML
    private Button buttonCancel;
    @FXML
    private MenuItem menuAbout;
    @FXML
    private ListView<Course> courseIDList;
    @FXML
    private TextField textBoxCredit;
    @FXML
    private TextField textBoxTitle;
    @FXML
    private ChoiceBox<String> choiceBoxCategory;
    @FXML
    private ChoiceBox<String> choiceBoxFilterCat;
    @FXML
    private Label labelSelected;
    @FXML
    private GridPane paneCourseInfo;
    @FXML
    private MenuItem menuDarkTheme;
    @FXML
    private MenuItem menuLightTheme;
    @FXML
    private MenuItem menuGreenTheme;
    
    //file location and CSS url
    File file;
    String css;
    @FXML
    private MenuItem menuDefaultTheme;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //create model object
        model = new CourseInventoryModel();
        
        //changelistener for ListView
        courseIDList.getSelectionModel()
                .selectedItemProperty()
                .addListener((ov, oldv, newv)-> {
                    
                    showCourseInfo(newv);
                }
                );
        
        // Customize Listview
        courseIDList.setCellFactory((listview) -> {

            ListCell<Course> cell = new ListCell<Course>() {
                //override updateItem()
                @Override
                public void updateItem(Course c, boolean empty) {
                    super.updateItem(c, empty);
                    if (empty || c == null) {
                        this.setText(null);
                        this.setGraphic(null);
                    } else {
                        this.setText(c.getId());
                    }
                }
            };
            return cell;
        });

        //change listener for choicebox
        choiceBoxFilterCat.getSelectionModel().selectedItemProperty().addListener((ov, oldv, newv) -> {
            populateCourses();
        });
    }

    //used to show course info of selected course in ListView.
    //enables and disables necessary controls
    private void showCourseInfo(Course course) {
        if (course == null) {
            return;
        }
        textBoxCredit.setText(String.valueOf(course.getCredit()));
        textBoxTitle.setText(course.getTitle());
        choiceBoxCategory.getItems().setAll(model.getCategories());
        choiceBoxCategory.getSelectionModel().select(course.getCat());

        paneCourseInfo.setDisable(true);
        buttonCancel.setDisable(true);
        buttonSave.setDisable(true);
        
        buttonDelete.setDisable(false);
        buttonEdit.setDisable(false);

        //change selected label
        if(course.getId()!=null) labelSelected.setText("Selected " + course.getId());
        else labelSelected.setText("");
    }

    //remember stage reference
    public void setStage(Stage stage) {
        this.stage = stage;

        //pop-up message for on-close request
        stage.setOnCloseRequest((WindowEvent e)
                -> {
            Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to quit? All unsaved progress will be lost.");
            Optional<ButtonType> answer = alert.showAndWait();
            if (answer.get() == ButtonType.OK) {
                Platform.exit();
            } else {
                e.consume();
            }
        }
        );
    }

    //method for handling the open menu button
    @FXML
    private void handleMenuOpen(ActionEvent event) {
        //open file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Course List File...");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("All Files", "*.*"),
                new ExtensionFilter("DAT", "*.dat"),
                new ExtensionFilter("TXT", "*.txt"));

        file = fileChooser.showOpenDialog(stage);

        //breaks if file is not selected
        if (file == null) {
            return;
        }

        try {
            //Open the file
            model.readCourseFile(file);
        } catch (RuntimeException e) {
            Alert alert = new Alert(AlertType.ERROR, "Incorrect File type");
        }
        
        //display courses to the listview
        if (model.getCourseCount() > 0) {
            choiceBoxFilterCat.getItems().setAll(model.getCategories());
            choiceBoxFilterCat.getItems().add(0, "All Categories".toUpperCase());
            choiceBoxFilterCat.getSelectionModel().selectFirst();
            populateCourses();
            enableButtons();

        }
    }

    //method for handling the save button in menu
    @FXML
    private void handleMenuSave(ActionEvent event) {
        //show file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Course List File as...");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("All Files", "*.*"),
                new ExtensionFilter("DAT", "*.dat"),
                new ExtensionFilter("TXT", "*.txt"));

        File file = fileChooser.showSaveDialog(stage);

        //breaks if file is not selected
        if (file == null) {
            return;
        }

        //Save the file
        model.saveCourseFile(file);

    }

    //handles close button in menu, alerts user to ensure they want to exit.
    @FXML
    private void handleMenuClose(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to quit? All unsaved progress will be lost.");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {
            Platform.exit();
        } else {
            event.consume();
        }
    }

    //method for creating search stage.
    @FXML
    private void handleButtonSearch(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchForm.fxml"));
        Parent searchRoot = (Parent) loader.load();

        Scene scene = new Scene(searchRoot);
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Search Courses");
        stage.setScene(scene);

        SearchFormController controllerSearch = (SearchFormController) loader.getController();
        controllerSearch.setModel(model);
        controllerSearch.setStage(stage);
        controllerSearch.updateTableCourse();
        
        //adds CSS to the scene
        if(css!=null){
        scene.getStylesheets().add(css);
        }

        stage.showAndWait();

        //gets the selected course from model created by search form controller
        Course c = model.getSelectedCourse();
        //gets the index of the selected course.
        int index = model.getSelectedCourseIndex();
        if (c != null) { //Selected
            choiceBoxFilterCat.getSelectionModel().selectFirst();
            //programatically select an item in the list view
            courseIDList.getSelectionModel().select(c);
            //make it focused
            courseIDList.getFocusModel().focus(index);
            courseIDList.scrollTo(index);
        } else {// no selection
            courseIDList.getSelectionModel().clearSelection();
        }
    }

    //method to handle edit button. enables editing GUI controls
    @FXML
    private void handleButtonEdit(ActionEvent event) {
        paneCourseInfo.setDisable(false);
        buttonCancel.setDisable(false);
        buttonSave.setDisable(false);

    }

    //method to handle deleting
    @FXML
    private void handleButtonDelete(ActionEvent event) {
        //gets course to delete
        Course c = courseIDList.getSelectionModel().getSelectedItem();
        
        //makes sure it is not null
        if(c == null) return;
        
        //asks user for confirmation
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete " + c.getId() + "?");
        Optional<ButtonType> response = alert.showAndWait();
        
        //removes course and saves file. Resets the edit area to blank
        if(response.isPresent()&response.get()==ButtonType.OK){
            model.removeCourse(c);
            model.saveCourseFile(file);
            showCourseInfo(new Course());
            
            populateCourses();
            courseIDList.getSelectionModel().clearSelection();
            buttonEdit.setDisable(true);
        }
        
    }

    //method for handling buttonAdd. Creates stage for adding new course
    @FXML
    private void handleButtonAdd(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddForm.fxml"));
        Parent addRoot = (Parent) loader.load();

        Scene scene = new Scene(addRoot);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add New Course");
        stage.setResizable(false);
        stage.setScene(scene);

        AddFormController controllerAdd = (AddFormController) loader.getController();
        controllerAdd.setStage(stage);
        controllerAdd.setModel(this.model);
        controllerAdd.setChoiceBox();
        
        //adds CSS to the scene
        if(css!=null){
        scene.getStylesheets().add(css);
        }

        stage.showAndWait();
        
        //gets new course info
        String id = model.getNewCourseId();
        Course c = model.getSelectedCourse();
        int index = model.getSelectedCourseIndex();
        
        //checks to make sure is not null
        if(id==null) return;
        
        //refreshes the ListView
        populateCourses();
        
        if (c != null) { //Selected
            choiceBoxFilterCat.getSelectionModel().selectFirst();
            //programatically select an item in the list view
            courseIDList.getSelectionModel().select(c);
            //make it focused
            courseIDList.getFocusModel().focus(index);
            courseIDList.scrollTo(index);
        } else {// no selection
            courseIDList.getSelectionModel().clearSelection();
        }
        
        //saves file
        model.saveCourseFile(file);
        
    }

    //handles save button while editing a course
    @FXML
    private void handleButtonSave(ActionEvent event) {
        //gets the selected course from Listview
        Course c = courseIDList.getSelectionModel().getSelectedItem();
        
        //makes sure it is not null
        if(c==null)return;
        
        //confirms changes
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to save the changes?");
        Optional<ButtonType> response = alert.showAndWait();
        
        if(response.isPresent()&response.get()!=ButtonType.OK){
            return;
        }
        
        //creates error alert
        Alert error = new Alert(AlertType.ERROR);
        
        //checks if title is valid, if not, pops up error
        String title = textBoxTitle.getText();
        if(title==null || title.length()==0){
            error.setContentText("Title cannot be empty.");
            error.show(); return;
        }
        
        //checks if credit is valid and >0, if not, pops up error.
        int credit;
        try{
            credit = Integer.parseInt(textBoxCredit.getText());
        }
        catch(NumberFormatException e){
            error.setContentText("Credit must be an integer");
            error.show(); return;
        }
        if(credit<0){
            error.setContentText("Credit must be greater than 0");
            error.show(); return;
        }
        
        //checks if category is valid, if not, pops up error.
        String category = choiceBoxCategory.getValue();
        if(category==null){
            error.setContentText("Category cannot be empty.");
            error.show(); return;
        }
        
        //if everything is valid, updates the model witht the edit
        model.updateCourse(c.getId(),title,credit,category);
        //saves the update
        model.saveCourseFile(file);
        //shows the course info of the edited course
        showCourseInfo(c);
    }

    //handles cancel button
    @FXML
    private void handleButtonCancel(ActionEvent event) {
        Course c = courseIDList.getSelectionModel().getSelectedItem();
        courseIDList.getSelectionModel().select(c);
        showCourseInfo(c);
    }

    //handles the about button in menu creates about window.
    @FXML
    private void handleMenuAbout(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AboutForm.fxml"));
        Parent aboutRoot = (Parent) loader.load();

        Scene scene = new Scene(aboutRoot);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("About");
        stage.setResizable(false);
        stage.setScene(scene);

        //adds CSS to the scene
        if(css!=null){
        scene.getStylesheets().add(css);
        }

        AboutFormController controllerAbout = (AboutFormController) loader.getController();
        controllerAbout.setStage(stage);

        stage.show();

    }

    //used by the choicebox to filter categories in the listview.
    private void populateCourses() {
        //gets the current selection
        String cat = choiceBoxFilterCat.getSelectionModel().getSelectedItem();
        //filters if all, if not filters based on selection
        if (cat == null || cat.equals("All Categories".toUpperCase())) {
            courseIDList.setItems(FXCollections.observableList(model.getCourses()));
        } else {
            courseIDList.setItems(FXCollections.observableList(model.findCoursesByCategory(cat)));
        }
    }

    //method for getting model.
    public CourseInventoryModel getModel() {
        return model;
    }

    //enables buttons after file is opened.
    private void enableButtons() {
        buttonSearch.setDisable(false);
        buttonAdd.setDisable(false);
        choiceBoxFilterCat.setDisable(false);

    }

    //sets CSS for mainFormController
    public void setCss(String css) {
        this.css = css;
    }

    ////////////////////////////////////////////////////////////////////////////
         /////////////////////////Themes///////////////////////////////
    
    //handles for the 3 theme buttons.
    @FXML
    private void handleThemeDark(ActionEvent event) {
        changeTheme("Theme;Dark_Theme");
    }

    @FXML
    private void handleThemeLight(ActionEvent event) {
        changeTheme("Theme;Light_Theme");
    }

    @FXML
    private void handleThemeGreen(ActionEvent event) {
        changeTheme("Theme;Green_Theme");
    }
    @FXML
    private void handleThemeDefault(ActionEvent event) {
        changeTheme("Theme;Default_Theme");
    }

    //method for changing theme file.
    private void changeTheme(String theme) {
        //writes all lines while appending bookmarked line in the middle
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./src/logs/Theme.dat"), false))) {
            writer.write(theme);
        } catch (IOException e) {
            System.err.println("Error. Unable to change theme.");
            return;
        }
        Alert alert = new Alert(AlertType.CONFIRMATION, "Application must be restarted in order to change theme. Please close and re-open to change theme.");
        alert.show();
    }

}
