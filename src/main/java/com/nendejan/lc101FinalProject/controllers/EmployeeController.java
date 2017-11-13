package com.nendejan.lc101FinalProject.controllers;
import com.nendejan.lc101FinalProject.models.Employee;
import com.nendejan.lc101FinalProject.models.EmployeeCategory;
import com.nendejan.lc101FinalProject.models.data.EmployeeCategoryDao;
import com.nendejan.lc101FinalProject.models.data.EmployeeDao;
import com.nendejan.lc101FinalProject.models.data.ScheduleDao;
import com.nendejan.lc101FinalProject.models.data.ShiftDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;


/**
 * Created by nico on 6/30/2017.
 */

@Controller
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private ShiftDao shiftDao;

    @Autowired
    private EmployeeCategoryDao employeeCategoryDao;

    @RequestMapping(value="")
    public String index(Model model) {

        model.addAttribute("employees", employeeDao.findAll());
        model.addAttribute("title", "Employee List");

        return "employee/index";
    }

    @RequestMapping(value="add", method= RequestMethod.GET)
    public String displayAddEmployeeForm(Model model){

        model.addAttribute("title", "Add Employee");
        model.addAttribute(new Employee());
        model.addAttribute("employeeRoleSelection", "Choose Employee Role");
        model.addAttribute("employeeCategories", employeeCategoryDao.findAll());
        model.addAttribute("shifts", shiftDao.findAll());

        return "employee/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddEmployeeForm(@ModelAttribute @Valid Employee newEmployee, Errors errors, @RequestParam int employeeCategoryId, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Employee");
            return "employee/add";
        }
        EmployeeCategory cat = employeeCategoryDao.findOne(employeeCategoryId);
        newEmployee.setEmployeeCategory(cat);
        employeeDao.save(newEmployee);

        return "redirect:";

    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveEmployeeForm(Model model) {
        model.addAttribute("employees", employeeDao.findAll());
        model.addAttribute("title", "Remove Employee");
        return "employee/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveEmployeeForm(@RequestParam int[] employeeIds) {

        for (int employeeId : employeeIds) {
            employeeDao.delete(employeeId);
        }

        return "redirect:";
    }


//TODO Create edit paths and methods to alter employee fields.




}