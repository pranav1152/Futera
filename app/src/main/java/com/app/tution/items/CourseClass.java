package com.app.tution.items;

import java.util.ArrayList;

public class CourseClass {
    String courseUID;
    String name;
    String instructorName;
    ArrayList<String> optedStudentsUids;
    ArrayList<String> assignmentsUids;
    int maxCount;
    float rating;
    ArrayList<AnnouncementClass> announcements;

    public CourseClass() {
    }

    public CourseClass(String courseUID, String name, ArrayList<String> optedStudentsUids, ArrayList<String> assignmentsUids, int maxCount, String instructorName) {
        this.courseUID = courseUID;
        this.name = name;
        this.optedStudentsUids = optedStudentsUids;
        this.assignmentsUids = assignmentsUids;
        this.maxCount = maxCount;
        this.instructorName = instructorName;
        rating = 0;
        announcements = new ArrayList<>();


    }

    public ArrayList<AnnouncementClass> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(ArrayList<AnnouncementClass> announcements) {
        this.announcements = announcements;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public ArrayList<String> getAssignmentsUids() {
        return assignmentsUids;
    }

    public void setAssignmentsUids(ArrayList<String> assignmentsUids) {
        this.assignmentsUids = assignmentsUids;
    }

    public String getCourseUID() {
        return courseUID;
    }

    public void setCourseUID(String courseUID) {
        this.courseUID = courseUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getOptedStudentsUids() {
        return optedStudentsUids;
    }

    public void setOptedStudentsUids(ArrayList<String> optedStudentsUids) {
        this.optedStudentsUids = optedStudentsUids;
    }
}

