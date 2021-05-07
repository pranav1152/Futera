package com.app.tution.items;

import java.util.ArrayList;

public class StudentClass {
    String UID;
    String name;
    ArrayList<String> strOptedCourses;
    ArrayList<String> strSubmittedAssignments;

    ArrayList<CourseClass> optedCourses;
    ArrayList<AssignmentClass> submittedAssignments;

    public StudentClass(String UID, String name, ArrayList<String> strOptedCourses, ArrayList<String> strSubmittedAssignments) {
        this.UID = UID;
        this.name = name;
        this.strOptedCourses = strOptedCourses;
        this.strSubmittedAssignments = strSubmittedAssignments;
    }

    public StudentClass() {
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

    public ArrayList<String> getStrOptedCourses() {
        return strOptedCourses;
    }

    public void setStrOptedCourses(ArrayList<String> strOptedCourses) {
        this.strOptedCourses = strOptedCourses;
    }

    public ArrayList<String> getStrSubmittedAssignments() {
        return strSubmittedAssignments;
    }

    public void setStrSubmittedAssignments(ArrayList<String> strSubmittedAssignments) {
        this.strSubmittedAssignments = strSubmittedAssignments;
    }

    public ArrayList<CourseClass> getOptedCourses() {
        return optedCourses;
    }

    public void setOptedCourses(ArrayList<CourseClass> optedCourses) {
        this.optedCourses = optedCourses;
    }

    public ArrayList<AssignmentClass> getSubmittedAssignments() {
        return submittedAssignments;
    }

    public void setSubmittedAssignments(ArrayList<AssignmentClass> submittedAssignments) {
        this.submittedAssignments = submittedAssignments;
    }
}
