package com.nendejan.lc101FinalProject.models;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nico on 6/30/2017.
 */

@Entity
public class Schedule {

    @Id
    @GeneratedValue
    @NotNull
    @Column(name="id", unique=true)
    private int id;

    private String scheduleDates;

    @ManyToMany
    private List<Shift> shiftsOfWeek = new ArrayList<>();

    public void addShift(Shift thisShift){
        this.shiftsOfWeek.add(thisShift);}

    public void removeShift(Shift thisShift){
        this.shiftsOfWeek.remove(thisShift);}

    @ManyToOne
    public workplace workplace;

    @ManyToMany
    private List<Employee> scheduledEmployees = new ArrayList<>();



    public void addEmployee (Employee thisEmployee){
        this.scheduledEmployees.add(thisEmployee);}

    public void removeEmployee (Employee thisEmployee){
        this.scheduledEmployees.remove(thisEmployee);}




    public Schedule() {
    }
    public Schedule(String scheduleDates, List<Shift> shiftsOfWeek) {
        this.scheduleDates = scheduleDates;
    }



    public int getId() {
        return id;
    }

    public String getScheduleDates() {
        return scheduleDates;
    }

    public List<Shift> getShiftsOfWeek() {
        return shiftsOfWeek;
    }

    public void setShiftsOfWeek(List<Shift> shiftsOfWeek) {
        this.shiftsOfWeek = shiftsOfWeek;
    }

    public void setScheduleDates(String scheduleDates) {
        this.scheduleDates = scheduleDates;
    }

    public com.nendejan.lc101FinalProject.models.workplace getWorkplace() {
        return workplace;
    }

    public void setWorkplace(com.nendejan.lc101FinalProject.models.workplace workplace) {
        this.workplace = workplace;
    }

    public List<Employee> getScheduledEmployees() {
        return scheduledEmployees;
    }

    public void setScheduledEmployees(List<Employee> scheduledEmployees) {
        this.scheduledEmployees = scheduledEmployees;
    }
}