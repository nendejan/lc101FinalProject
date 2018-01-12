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

    public void addUsers(User thisUser){
        this.users.add(thisUser);}


    @OneToMany
    @JoinColumn(name="workplace_id")
    private List<Employee> employeeRoster = new ArrayList<>();

    public void addEmployee(Employee thisEmployee){
        this.employeeRoster.add(thisEmployee);}

    public void removeEmployee(Employee thisEmployee){
        this.employeeRoster.remove(thisEmployee);}


    @OneToMany
    @JoinColumn(name="workplace_id")
    private List<EmployeeCategory> employeeRoles
 = new ArrayList<>();

    public void addEmployeeCategory (EmployeeCategory thisCategory){
        this.employeeRoles.add(thisCategory);}

    public void removeEmployeeCategory (EmployeeCategory thisCategory){
        this.employeeRoles.remove(thisCategory);}

    @OneToMany
    @JoinColumn(name="workplace_id")
    private List<Shift> workplaceShifts = new ArrayList<>();

    public void addShift (Shift thisShift){
        this.workplaceShifts.add(thisShift);}

    public void removeShift (Shift thisShift){
        this.workplaceShifts.remove(thisShift);}

    @OneToMany
    @JoinColumn(name="workplace_id")
    private List<Schedule> workplaceSchedules = new ArrayList<>();

    public void addSchedule (Schedule thisSchedule){
        this.workplaceSchedules.add(thisSchedule);}

    public void removeSchedule(Schedule thisSchedule){
        this.workplaceSchedules.remove(thisSchedule);}

    public List<Shift> getWorkplacePrimaryShifts (workplace thisWorkplace){
        List<Shift> workplacesPrimaryShifts= new ArrayList<>();

        for(Shift shift : thisWorkplace.getWorkplaceShifts()){
            if(shift.isPrimaryShift()==true){
                workplacesPrimaryShifts.add(shift);

            }

        }
        return workplacesPrimaryShifts;
    }



    /*

TODO Implement later
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

    public List<Employee> getEmployeeRoster() {
        return employeeRoster;
    }

    public void setEmployeeRoster(List<Employee> employeeRoster) {
        this.employeeRoster = employeeRoster;
    }

    public List<EmployeeCategory> getEmployeeRoles() {
        return employeeRoles;
    }

    public void setEmployeeRoles(List<EmployeeCategory> employeeRoles) {
        this.employeeRoles = employeeRoles;
    }

    public List<Shift> getWorkplaceShifts() {
        return workplaceShifts;
    }

    public void setWorkplaceShifts(List<Shift> workplaceShifts) {
        this.workplaceShifts = workplaceShifts;
    }

    public List<Schedule> getWorkplaceSchedules() {
        return workplaceSchedules;
    }

    public void setWorkplaceSchedules(List<Schedule> workplaceSchedules) {
        this.workplaceSchedules = workplaceSchedules;
    }
}

