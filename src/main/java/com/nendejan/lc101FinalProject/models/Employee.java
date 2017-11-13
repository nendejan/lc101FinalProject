package com.nendejan.lc101FinalProject.models;
import javax.persistence.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Employee {


    @Id
    @GeneratedValue
    @NotNull
    @Column(name="id", unique=true)
    private int id;

    @NotNull
    @Size(min = 2, message = "Employee must have a name.")
    private String name;

    @NotNull
    private int wage;


    @ManyToOne
    private EmployeeCategory employeeCategory;

    @ManyToOne
    public Schedule schedule;

    @ManyToMany (mappedBy = "employees")
    private List<Shift> availability = new ArrayList<>();

    private int phoneNumber;
    private String email;
    private int swagAmount;
    private int hoursWorkedInShift;
    private int hoursWorkedInSchedule;



    public Employee(String name, int wage, int phoneNumber, String email, EmployeeCategory employeeCategory) {

        this.name = name;
        this.wage = wage;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.employeeCategory = employeeCategory;
    }

    public Employee() {
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWage() {
        return wage;
    }

    public void setWage(int wage) {
        this.wage = wage;
    }

    public int getSwagAmount() {
        return swagAmount;
    }

    public void setSwagAmount(int swagAmount) {
        this.swagAmount = swagAmount;
    }

    public int getHoursWorkedInShift() {
        return hoursWorkedInShift;
    }

    public void setHoursWorkedInShift(int hoursWorkedInShift) {
        this.hoursWorkedInShift = hoursWorkedInShift;
    }


    public int getHoursWorkedInSchedule() {
        return hoursWorkedInSchedule;
    }

    public void setHoursWorkedInSchedule(int hoursWorkedInSchedule) {
        this.hoursWorkedInSchedule = hoursWorkedInSchedule;
    }


    public EmployeeCategory getEmployeeCategory() { return employeeCategory; }

    public void setEmployeeCategory(EmployeeCategory employeeCategory) {this.employeeCategory = employeeCategory;}

    public int getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(int phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

}

