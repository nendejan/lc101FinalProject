package com.nendejan.lc101FinalProject.controllers;

import com.nendejan.lc101FinalProject.models.EmployeeCategory;
import com.nendejan.lc101FinalProject.models.User;
import com.nendejan.lc101FinalProject.models.data.EmployeeCategoryDao;
import com.nendejan.lc101FinalProject.models.data.workplaceDao;
import com.nendejan.lc101FinalProject.models.data.EmployeeDao;
import com.nendejan.lc101FinalProject.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by nico on 7/1/2017.
 */
@Controller
@RequestMapping("login/employeeCategory")
public class EmployeeCategoryController {

    @Autowired
    private workplaceDao workplaceDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private EmployeeCategoryDao employeeCategoryDao;


    @RequestMapping(value="", method = RequestMethod.GET)
    public String index(Model model, HttpServletRequest request, @CookieValue(value="loggedInCookie") String cookieValue){

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);


        /*TODO These should link to more detailed information about that cata*/
        model.addAttribute("categories", loggedInUser.getWorkplace().getEmployeeRoles());
        model.addAttribute("title", "Employee Categories");

        return "employeeCategory/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCategoryForm(Model model) {

        model.addAttribute("title", " Add Category");
        model.addAttribute("employeeCategory", new EmployeeCategory());
        return "employeeCategory/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCategoryForm(Model model, HttpServletRequest request, @CookieValue(value="loggedInCookie") String cookieValue, @ModelAttribute @Valid EmployeeCategory employeeCategory, Errors errors){

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Category");
            return "employeeCategory/add";
        }
        loggedInUser.getWorkplace().addEmployeeCategory(employeeCategory);
        employeeCategoryDao.save(employeeCategory);


        return "redirect:";
    }



    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCategoryForm(Model model, HttpServletRequest request, HttpServletResponse response, @CookieValue(value="loggedInCookie") String cookieValue) {

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);



        model.addAttribute("categories", loggedInUser.getWorkplace().getEmployeeRoles());
        model.addAttribute("title", "Remove Employee Category");




        return "employeeCategory/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCategoryForm(Model model, HttpServletRequest request, @CookieValue(value="loggedInCookie") String cookieValue, @RequestParam String cataToRemove) {



        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);

        model.addAttribute("categories", loggedInUser.getWorkplace().getEmployeeRoles());
        model.addAttribute("title", "Remove Employee Category");


        EmployeeCategory removeMe = employeeCategoryDao.findByNameAndWorkplace(cataToRemove, loggedInUser.getWorkplace());





        loggedInUser.getWorkplace().removeEmployeeCategory(removeMe);
        employeeCategoryDao.delete(removeMe);
        workplaceDao.save(loggedInUser.getWorkplace());


        return "employeeCategory/remove";
    }
}