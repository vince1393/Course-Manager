
package CourseInventory;

import java.util.Objects;

////////////////////////////////////////////////////////////////////////////////
// Course.java
// ============
// Course object used to represent a course
// Stores course infomation and offers sorting, and comparability.
//
// AUTHOR: Vincent Romani (vromani@outlook.com)
// CREATED: 2018-03-1
// UPDATED: 2018-03-27
////////////////////////////////////////////////////////////////////////////////

public class Course {
    //object properties
    String id;
    String title;
    int credit;
    String cat;

    //default constructor
    public Course() {}

    //secondary constructor
    public Course(String id, String title, int credit, String cat) {
        this.id = id;
        this.title = title;
        this.credit = credit;
        this.cat = cat;
    }

    //get methods
    public String getId() {                 return id;}
    public String getTitle() {              return title;}
    public int getCredit() {                return credit;}
    public String getCat() {                return cat;}

    //set methods
    public void setId(String id) {          this.id = id;}
    public void setTitle(String title) {    this.title = title;}
    public void setCredit(int credit) {     this.credit = credit;}
    public void setCat(String cat) {        this.cat = cat;}
    
    public void set(String id, String title, int credit, String category) {
        this.id=id;
        this.title=title;
        this.credit=credit;
        this.cat=category;
    }
    
    //method used for writing to database. Formats properly.
    public String getCourseInfo(){          return id + " ; " + title + " ; " + credit + " ; " + cat;}

    //toString method overridden for testing purposes.
    @Override
    public String toString() {
        return "Course{" + "id=" + id + ", title=" + title + ", credit=" + credit + ", cat=" + cat + '}';
    }
    
    //hashcode override
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.title);
        hash = 23 * hash + this.credit;
        hash = 23 * hash + Objects.hashCode(this.cat);
        return hash;
    }

    //.equals method to check if passed object is equal to current object
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Course other = (Course) obj;
        if (this.credit != other.credit) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.cat, other.cat)) {
            return false;
        }
        return true;
    }
    
    //method for sorting, is not used because tableview has built in sorting functionality.
    public int compareTo(Course course){
        if(Integer.parseInt(this.id)==Integer.parseInt(course.id)) return 0;
        else if(Integer.parseInt(this.id)>Integer.parseInt(course.id)) return -1;
        else return 1;
    }
}
