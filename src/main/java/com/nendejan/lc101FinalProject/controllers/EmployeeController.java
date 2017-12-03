package com.nendejan.lc101FinalProject.controllers;
import com.nendejan.lc101FinalProject.models.Employee;
import com.nendejan.lc101FinalProject.models.EmployeeCategory;
import com.nendejan.lc101FinalProject.models.User;
import com.nendejan.lc101FinalProject.models.data.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;


/**
 * Created by nico on 6/30/2017.
 */

@Controller
@RequestMapping("login/employee")
public class EmployeeController {

    @Autowired
    private workplaceDao workplaceDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private ShiftDao shiftDao;

    @Autowired
    private EmployeeCategoryDao employeeCategoryDao;

    @RequestMapping(value="", method = RequestMethod.GET)
    public String index(Model model, HttpServletRequest request, @CookieValue(value="loggedInCookie") String cookieValue) {

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);

        boolean hasEmployees = true;
        if (loggedInUser.getWorkplace().getEmployeeRoster().isEmpty()== true){

            hasEmployees = false;
            model.addAttribute("hasEmployees", false);

            model.addAttribute("employees", loggedInUser.getWorkplace().getEmployeeRoles());
            model.addAttribute("title", "No Employees");

            return "employee/index";

        }

        model.addAttribute("hasEmployees", true);
        model.addAttribute("employees", loggedInUser.getWorkplace().getEmployeeRoster());
        model.addAttribute("title", "Employee List");

        return "employee/index";
    }

    @RequestMapping(value="add", method= RequestMethod.GET)
    public String displayAddEmployeeForm(Model model, HttpServletRequest request, @CookieValue(value="loggedInCookie") String cookieValue){

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);

        model.addAttribute("title", "Add Employee");
        model.addAttribute(new Employee());
        model.addAttribute("employeeRoleSelection", "Choose Employee Role");
        model.addAttribute("employeeCategories", loggedInUser.getWorkplace().getEmployeeRoles());
        model.addAttribute("shifts", shiftDao.findAll());
/*TODO Fix shifts here!!*/
        return "employee/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddEmployeeForm(@ModelAttribute @Valid Employee newEmployee, Errors errors, @RequestParam String employeeCategoryName, Model model, HttpServletRequest request, @CookieValue(value="loggedInCookie") String cookieValue) {

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Employee");
            return "employee/add";
        }

        EmployeeCategory cat = employeeCategoryDao.findByNameAndWorkplace(employeeCategoryName, loggedInUser.getWorkplace());

        loggedInUser.getWorkplace().addEmployee(newEmployee);


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