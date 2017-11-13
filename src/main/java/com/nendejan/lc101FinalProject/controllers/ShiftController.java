package com.nendejan.lc101FinalProject.controllers;

import com.nendejan.lc101FinalProject.models.Employee;
import com.nendejan.lc101FinalProject.models.Shift;
import com.nendejan.lc101FinalProject.models.data.EmployeeCategoryDao;
import com.nendejan.lc101FinalProject.models.data.EmployeeDao;
import com.nendejan.lc101FinalProject.models.data.ScheduleDao;
import com.nendejan.lc101FinalProject.models.data.ShiftDao;
import com.nendejan.lc101FinalProject.models.forms.AddShiftEmployeeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by nico on 6/30/2017.
 */
@Controller
@RequestMapping("shifts")
public class ShiftController {

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private EmployeeCategoryDao employeeCategoryDao;

    @Autowired
    private ShiftDao shiftDao;

    @Autowired
    private ScheduleDao scheduleDao;

    @RequestMapping(value="")
    public String index(Model model){

        model.addAttribute("shifts", shiftDao.findAll());
        model.addAttribute("title", "Shifts for Schedule");


        return "shifts/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddShiftForm(Model model, Shift shift) {
        Shift newShift;

        model.addAttribute("title", "Add Shift");
        model.addAttribute("shift", new Shift());


        return "shifts/add";
    }

    @RequestMapping(value = "view/{id}", method = RequestMethod.GET)
    public String viewShift(@PathVariable int id, Model model) {
        Shift shift =shiftDao.findOne(id);
        model.addAttribute("title", shift.getDay());
        model.addAttribute("shift", shift);
        return "shifts/view";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddShiftForm(Model model, @ModelAttribute @Valid Shift shift, Errors errors) {

        if(errors.hasErrors()) {
            model.addAttribute("title", "Add Shift");
            return "shifts/add";
        }

        shiftDao.save(shift);
        return "redirect:view/" + shift.getId();
    }

    @RequestMapping(value="add-employee/{shiftId}", method = RequestMethod.GET)
    public String addEmployee(Model model, @PathVariable int shiftId){
        Shift shift = shiftDao.findOne(shiftId);
        AddShiftEmployeeForm employeeForm = new AddShiftEmployeeForm(shift, employeeDao.findAll());

        model.addAttribute("title", "Add Employee to Shift: " + shift.getDay());
        model.addAttribute("form", employeeForm);

        return "shifts/add-employee";

    }

    @RequestMapping(value="add-employee/{shiftId}", method = RequestMethod.POST)
    public String addEmployee(Model model, @ModelAttribute @Valid AddShiftEmployeeForm employeeForm, Errors errors, @PathVariable int shiftId){
        if(errors.hasErrors()){
            model.addAttribute("title", "Add Employee");
            return "shifts/add-employee" + shiftId;
        }

        Shift shift = shiftDao.findOne(employeeForm.getShiftId());
        Employee employee = employeeDao.findOne(employeeForm.getEmployeeId());

        shift.addEmployee(employee);
        shiftDao.save(shift);

        return "redirect:../view/" + shift.getId();
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveShiftForm(Model model) {

        model.addAttribute("shifts", shiftDao.findAll());
        model.addAttribute("title", "Remove Shifts");
        return "shifts/remove";

    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveShiftForm(@RequestParam int[] shiftIds) {

        for (int shiftId : shiftIds) {
            shiftDao.delete(shiftId);
        }

        return "redirect:";
    }



}