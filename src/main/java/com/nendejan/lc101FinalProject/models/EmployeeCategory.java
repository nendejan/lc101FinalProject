package com.nendejan.lc101FinalProject.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nico on 7/1/2017.
 */

@Entity
public class EmployeeCategory {

    @Id
    @GeneratedValue
    @NotNull
    @Column(name="id", unique=true)
    private int id;

    @NotNull
    @Size(min = 2, message = "Category must have a name.")
    private String name;

    public EmployeeCategory() {
    }

    public EmployeeCategory(String name) {
        this.name = name;
    }


    
    @ManyToOne
    public workplace workplace;

    @OneToMany
    @JoinColumn(name = "category_id")
    private List<Employee> employees = new ArrayList<>();




    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public com.nendejan.lc101FinalProject.models.workplace getWorkplace() {
        return workplace;
    }

    public void setWorkplace(com.nendejan.lc101FinalProject.models.workplace workplace) {
        this.workplace = workplace;
    }
}
