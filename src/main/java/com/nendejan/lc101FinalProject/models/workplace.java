package com.nendejan.lc101FinalProject.models;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="workplace")
public class workplace {


    @Id
    @GeneratedValue
    @NotNull
    @Column(name="id", unique=true)
    private int id;

    @NotNull
    @Size(min=3, max=15, message= "Workplace name must be between 3 and 15 characters long.")
    private String workplaceName;


    @OneToMany
    @JoinColumn(name="workplace_id")
    private List<User> users = new ArrayList<>();

/*

TODO Implement later
    private List<Employee> employeeRoster = new ArrayList<>();

    private List<Schedule> workplaceSchedules = new ArrayList<>();

    private List<Shift> workplaceShifts = new ArrayList<>();

    private List<User> workplaceAdmin = new ArrayList<>();
    */

    public int getId() {
        return id;
    }


    public String getWorkplaceName() {
        return workplaceName;
    }

    public void setWorkplaceName(String workplaceName) {
        this.workplaceName = workplaceName;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUsers(User thisUser){
        this.users.add(thisUser);

    }
}
