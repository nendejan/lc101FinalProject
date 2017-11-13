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

    public Schedule() {
    }
    public Schedule(String scheduleDates, List<Shift> shiftsOfWeek) {
        this.scheduleDates = scheduleDates;


    }

    public void addShift(Shift shift){
        shiftsOfWeek.add(shift);}

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


}