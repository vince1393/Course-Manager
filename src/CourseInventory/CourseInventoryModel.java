
package CourseInventory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

////////////////////////////////////////////////////////////////////////////////
// CourseInventoryModel.java
// ============
// Main model for logic related computations.
// Class also connects all windows and passes information
//
// AUTHOR: Vincent Romani (vromani@outlook.com)
// CREATED: 2018-03-1
// UPDATED: 2018-03-27
////////////////////////////////////////////////////////////////////////////////

public class CourseInventoryModel {

    //Object parameters
    private ArrayList<Course> courses;
    private Course selectedCourse;
    private final Set<String> categories;
    private String newCourseId;

    //default constructor
    public CourseInventoryModel() {
        courses = new ArrayList<>();
        categories = new HashSet<>();
    }

    //method used for opening file and reading file, stores everything to courses array.
    //Set was used to get the categories
    public void readCourseFile(File file) {

        //check for no file, if null, returns
        if (file == null) {
            return;
        }

        //declare arraylist to store lines and line to read line.
        ArrayList lines = new ArrayList<>();
        String line;

        //buffer reader to begin reading
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // read line by line and store to lines
            while ((line = br.readLine()) != null) {
                //adds line that was read to lines array
                lines.add(line);
            }
        } catch (IOException e) {
            //prints error message
            System.err.println("[ERROR] " + e.getMessage());
        }

        //clears courses array before updating.
        courses.clear();
        
        //gets each line in array list, splits them, and creates course object for each.
        for (int z = 0; z < lines.size(); z++) {

            line = (String) lines.get(z);

            String[] tokens = line.split(";");

            if (tokens.length == 4) {

                String id = tokens[0].trim();
                String title = tokens[1].trim();
                int credit = Integer.valueOf(tokens[2].trim());
                String cat = tokens[3].trim();
                categories.add(cat);

                Course c = new Course(id, title, credit, cat);

                courses.add(c);
            }//end of if

        }//end of for
    }//end of method

    public void saveCourseFile(File file) {
        //writes all lines while appending bookmarked line in the middle
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {

            //create new file
            file.createNewFile();

            //loop for the length of the arraylist
            for (Course z : courses) {
                writer.write(z.getCourseInfo());
                writer.newLine();
            }//end of for

        } catch (IOException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
    }//end of method
    
////////////////////////////////////////////////////////////////////////////////
           /////////////////Search methods////////////////
    
    //this method searches the courses arraylist for passed String parameter
    //returns array list with mathing IDs
    public ArrayList<Course> findCoursesByID(String text) {
        if (text == null || text.isEmpty()) {
            return this.courses;
        }
        text = text.toUpperCase();
        ArrayList<Course> courses = new ArrayList<>();
        for (Course c : this.courses) {
            if (c.id.contains(text)) {
                courses.add(c);
            }
        }
        return courses;
    }

    //this method searches the courses arraylist for passed String parameter
    //returns array list with mathing title
    public ArrayList<Course> findCoursesByTitle(String text) {
        if (text == null || text.isEmpty()) {
            return this.courses;
        }
        text = text.toUpperCase();
        ArrayList<Course> courses = new ArrayList<>();
        for (Course c : this.courses) {
            if (c.title.toUpperCase().contains(text)) {
                courses.add(c);
            }
        }
        return courses;
    }
    //this method searches the courses arraylist for passed String parameter
    //returns array list with mathing category
    public ArrayList<Course> findCoursesByCategory(String text) {
        if (text == null || text.isEmpty()) {
            return this.courses;
        }
        text = text.toUpperCase();
        ArrayList<Course> courses = new ArrayList<>();
        for (Course c : this.courses) {
            if (c.cat.contains(text)) {
                courses.add(c);
            }
        }
        return courses;
    }
    //this method searches the courses arraylist for passed String parameter
    //returns array list with mathing credit
    public ArrayList<Course> findCoursesByCredit(String text) {
        if (text == null || text.isEmpty()) {
            return this.courses;
        }
        text = text.toUpperCase();
        ArrayList<Course> courses = new ArrayList<>();
        for (Course c : this.courses) {
            if (String.valueOf(c.credit).contains(text)) {
                courses.add(c);
            }
        }
        return courses;
    }
    //this method searches the courses arraylist for passed String parameter
    //returns array list with any matching parameters
    public ArrayList<Course> findCourses(String text) {
        if (text == null || text.isEmpty()) {
            return this.courses;
        }

        text = text.toUpperCase();
        ArrayList<Course> courses = new ArrayList<>();
        for (Course c : this.courses) {
            if (c.cat.contains(text)
                    | c.id.contains(text)
                    | c.title.contains(text)
                    | String.valueOf(c.credit).contains(text)) {
                courses.add(c);
            }
        }
        return courses;
    }
///////////////////////////////////////////////////////////////////////////////
    
    //method used to validate if an ID is in the correct format
    boolean validateId(String id) {
        //define pattern
        final String PATTERN = "^[A-Za-z]{4}[0-9]{5}$";
        //check if matches patter and isnt null
        if(id != null && id.matches(PATTERN)) 
            return true;
        else 
            return false;
    }

    //method used to add a new course. logs the added course
    void addCourse(String id, String title, int credit, String category) {
        courses.add(new Course(id,title,credit,category));
        log("add ; ",new Course(id,title,credit,category));
        setNewCourseId(id);
    }
    //method used to update a course. logs the old and new changes
    void updateCourse(String id, String title, int credit, String category) {
      Course course = getCourseById(id);
      if(course != null)
      {
          log("update_old ; ",course);
          course.set(id,title,credit,category);
          log("update_new ; ",course);
      }
    }
    //method used to remove a course. logs the deleted course.
    void removeCourse(Course c) {  
        log("delete ; ",c);
        this.courses.remove(c);
    }
    
    //logging method. used for creating log file. appends to log.dat, formats data and adds username of user that performed action
    void log(String type, Course c){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./src/logs/log.dat"), true))) {
            //loop for the length of the arraylist
                writer.write(type+System.getProperty("user.name")+" ; " + c.getCourseInfo());
                writer.newLine();

        } catch (IOException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
    }
    
    //set methods
    void setSelectedCourse(Course selectedCourse) {    this.selectedCourse = selectedCourse;}
    void setNewCourseId(String newCourseId) {          this.newCourseId = newCourseId;}
    
    //get methods
    int getSelectedCourseIndex(){               return this.courses.indexOf(selectedCourse);}
    int getCourseCount() {                      return courses.size();}
    ArrayList<Course> getCourses() {            return courses;}
    Set<String> getCategories() {               return categories;}
    Course getSelectedCourse() {                return selectedCourse;}
    String getNewCourseId() {                   return newCourseId;}
    int getCourseIndex(String id) {             return courses.indexOf(id);}
    
    private Course getCourseById(String id) {
        for (Course c : this.courses) {
            if (c.id.equals(id)) {
                return c;
            }
        }
        return null;
    }
}
