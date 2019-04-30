package CourseInventory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

////////////////////////////////////////////////////////////////////////////////
// CourseInventory.java
// ============
// Class used for initiating the Main form and setting theme
//
// AUTHOR: Vincent Romani (vromani@outlook.com)
// CREATED: 2018-03-1
// UPDATED: 2018-03-27
////////////////////////////////////////////////////////////////////////////////

public class CourseInventory extends Application {
  public static void main(String args[]){
    launch(args);
  }
    //start method for main form controller
    @Override
    public void start(Stage stage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainForm.fxml"));
        Parent root = (Parent)loader.load();

        Scene scene = new Scene(root);
        
        //attempts to load css file
        String css=null;
        try{
            css = this.getClass().getResource(getTheme()).toExternalForm();
        }
        catch(NullPointerException e){
            System.err.println("Unable to read Theme.dat. Program will have limited functionality.");
        }
        
        //set CSS if there is a CSS file
        if(css!=null){
        scene.getStylesheets().add(css);
        }
        
        //set stage
        stage.setScene(scene);
        
        //assign stage to the controller
        MainFormController controller = (MainFormController)loader.getController();
        controller.setCss(css);
        controller.setStage(stage);
        
        //show window
        stage.show();
    }
    
    //this method reads the themes file to check what the theme is set to and passes the url of the appropriate theme css
    private String getTheme() {

        ArrayList lines = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader("./src/logs/Theme.dat"))) {
            // read line by line and store to lines
            while ((line = br.readLine()) != null) {
                //adds line that was read to lines array
                lines.add(line);
            }
        } catch (Exception e) {
            //prints error message
            System.err.println("Unable to read Theme.dat");
        }
        for (int z = 0; z < lines.size(); z++) {

            line = (String) lines.get(z);

            String[] tokens = line.split(";");
            if(tokens.length==2){
                
            if(tokens[1].equals("Dark_Theme")){
                return "/CSS/DarkTheme.css";
            }
            else if(tokens[1].equals("Light_Theme")){
                return "/CSS/LightTheme.css";
            }
            else if(tokens[1].equals("Green_Theme")){
                return "/CSS/GreenTheme.css";
            }
            else if(tokens[1].equals("Default_Theme")){
                return "/CSS/DefaultTheme.css";
            }
            }
        }//end of for
             return null;
    }
}
