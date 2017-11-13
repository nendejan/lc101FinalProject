package com.nendejan.lc101FinalProject.models.forms;

import com.nendejan.lc101FinalProject.models.Employee;
import com.nendejan.lc101FinalProject.models.Shift;

public class AddShiftEmployeeForm {

    private Shift shift;
    private Iterable<Employee> employees;
    private int shiftId;
    private int employeeId;

    public AddShiftEmployeeForm(){}

    public AddShiftEmployeeForm(Shift shift, Iterable<Employee> employees){

        this.shift = shift;
        this.employees = employees;

    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Iterable<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Iterable<Employee> employees) {
        this.employees = employees;
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}