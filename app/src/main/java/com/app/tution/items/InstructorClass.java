package com.app.tution.items;


import java.util.ArrayList;

public class InstructorClass {
    String UID;
    String name;
    ArrayList<String> courseUids;
    ArrayList<CourseClass> courses;

    public InstructorClass() {
    }

    public InstructorClass(String UID, String name, ArrayList<String> courseUids) {
        this.UID = UID;
        this.name = name;
        this.courseUids = courseUids;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getCourseUids() {
        return courseUids;
    }

    public void setCourseUids(ArrayList<String> courseUids) {
        this.courseUids = courseUids;
    }

    public ArrayList<CourseClass> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<CourseClass> courses) {
        this.courses = courses;
    }

}
