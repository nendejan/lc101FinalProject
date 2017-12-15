package com.nendejan.lc101FinalProject.controllers;
import com.nendejan.lc101FinalProject.models.Employee;
import com.nendejan.lc101FinalProject.models.EmployeeCategory;
import com.nendejan.lc101FinalProject.models.Shift;
import com.nendejan.lc101FinalProject.models.User;
import com.nendejan.lc101FinalProject.models.data.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


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
            model.addAttribute("title", "Employee List");

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
        model.addAttribute("shifts", loggedInUser.getWorkplace().getWorkplaceShifts());

        return "employee/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddEmployeeForm(@ModelAttribute @Valid Employee newEmployee, Errors errors, @RequestParam String employeeCategoryName, @RequestParam List<Integer> shiftsAvailable, Model model, HttpServletRequest request, @CookieValue(value="loggedInCookie") String cookieValue) {

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Employee");
            return "employee/add";
        }

        EmployeeCategory cat = employeeCategoryDao.findByNameAndWorkplace(employeeCategoryName, loggedInUser.getWorkplace());
        List<Shift> shiftsToAdd =new ArrayList<>();
        loggedInUser.getWorkplace().addEmployee(newEmployee);
        for (Integer shiftId : shiftsAvailable){
            shiftsToAdd.add(shiftDao.findOne(shiftId));


        }

        newEmployee.setAvailability(shiftsToAdd);
        newEmployee.setEmployeeCategory(cat);

        employeeDao.save(newEmployee);
        List<Shift> shiftCheck = newEmployee.getAvailability();
        return "redirect:";

    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveEmployeeForm(Model model, HttpServletRequest request, HttpServletResponse response, @CookieValue(value="loggedInCookie") String cookieValue) {


        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);



        model.addAttribute("employees", loggedInUser.getWorkplace().getEmployeeRoster());
        model.addAttribute("title", "Remove Employee");
        return "employee/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveEmployeeForm(Model model, HttpServletRequest request, @CookieValue(value="loggedInCookie") String cookieValue, @RequestParam String employeeToRemove) {




        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);

        model.addAttribute("employees", loggedInUser.getWorkplace().getEmployeeRoster());
        model.addAttribute("title", "Remove Employee");

        Employee removeMe = employeeDao.findByNameAndWorkplace(employeeToRemove, loggedInUser.getWorkplace());

        loggedInUser.getWorkplace().removeEmployee(removeMe);
        employeeDao.delete(removeMe);
        workplaceDao.save(loggedInUser.getWorkplace());


        return "employee/remove";
    }


//TODO Create edit paths and methods to alter employee fields.




}