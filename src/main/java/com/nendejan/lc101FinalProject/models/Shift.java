package com.nendejan.lc101FinalProject.models;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nico on 6/30/2017.
 */
@Entity

public class Shift {

    @Id
    @GeneratedValue
    @NotNull
    @Column(name="id", unique=true)
    private int id;

    @NotNull
    private String day;

    private String startTimeAMPM;
    private String endTimeAMPM;

    private int startTimeHour;
    private String startTimeMinute;

    private int endTimeHour;
    private String endTimeMinute;

    private boolean isPrimaryShift;

    @ManyToOne
    public workplace workplace;


    @ManyToMany
    @JoinColumn(name="shift_id")
    private List <Employee> employees = new ArrayList<>();

    public void addEmployee(Employee employee){
        employees.add(employee);}

    public void removeEmployee(Employee employee){
        employees.remove(employee);}

    public Shift() {
    }

    public Shift( String day, String startTimeAMPM, String endTimeAMPM, int startTimeHour, String startTimeMinute, int endTimeHour, String endTimeMinute ) {

        this.day = day;
        this.startTimeHour = startTimeHour;
        this.startTimeMinute = startTimeMinute;
        this.endTimeHour = endTimeHour;
        this.endTimeMinute = endTimeMinute;
        this.startTimeAMPM = startTimeAMPM;
        this.endTimeAMPM = endTimeAMPM;

    }

    public boolean shiftCompare(Shift otherShift){
        if(this.getDay().equals(otherShift.getDay()) &&
                this.getStartTimeHour() == otherShift.getStartTimeHour() &&
                this.getStartTimeMinute().equals(otherShift.getStartTimeMinute()) &&
                this.getStartTimeAMPM().equals(otherShift.getStartTimeAMPM()) &&
                this.getEndTimeHour() == otherShift.getEndTimeHour() &&
                this.getEndTimeMinute().equals(otherShift.getEndTimeMinute()) &&
                this.getEndTimeAMPM().equals(otherShift.getEndTimeAMPM())){
            return true;}
        else{
            return false;
        }
    }

    public List<Employee> getEmployees() {
        return employees;
    }


    public int getId() {
        return id;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }


    public String getStartTimeAMPM() {
        return startTimeAMPM;
    }

    public void setStartTimeAMPM(String startTimeAMPM) {
        this.startTimeAMPM = startTimeAMPM;
    }

    public String getEndTimeAMPM() {
        return endTimeAMPM;
    }

    public void setEndTimeAMPM(String endTimeAMPM) {
        this.endTimeAMPM = endTimeAMPM;
    }

    public int getStartTimeHour() {
        return startTimeHour;
    }

    public void setStartTimeHour(int startTimeHour) {
        this.startTimeHour = startTimeHour;
    }

    public String getStartTimeMinute() {
        return startTimeMinute;
    }

    public void setStartTimeMinute(String startTimeMinute) {
        this.startTimeMinute = startTimeMinute;
    }

    public int getEndTimeHour() {
        return endTimeHour;
    }

    public void setEndTimeHour(int endTimeHour) {
        this.endTimeHour = endTimeHour;
    }

    public String getEndTimeMinute() {
        return endTimeMinute;
    }

    public void setEndTimeMinute(String endTimeMinute) {
        this.endTimeMinute = endTimeMinute;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;}

    public com.nendejan.lc101FinalProject.models.workplace getWorkplace() {
        return workplace;
    }

    public void setWorkplace(com.nendejan.lc101FinalProject.models.workplace workplace) {
        this.workplace = workplace;
    }

    public boolean isPrimaryShift() {
        return isPrimaryShift;
    }

    public void setPrimaryShift(boolean primaryShift) {
        isPrimaryShift = primaryShift;
    }
}