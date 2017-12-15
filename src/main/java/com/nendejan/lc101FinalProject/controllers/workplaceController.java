package com.nendejan.lc101FinalProject.controllers;

import com.nendejan.lc101FinalProject.models.User;
import com.nendejan.lc101FinalProject.models.data.workplaceDao;
import com.nendejan.lc101FinalProject.models.workplace;
import com.nendejan.lc101FinalProject.models.data.UserDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("login/workplace")
public class workplaceController {

    @Autowired
    private workplaceDao workplaceDao;

    @Autowired
    private UserDao userDao;

   /*
   This functionality got moved to dashboard template/usercontroller

   @RequestMapping(value="", method = RequestMethod.GET)
    public String displayWorkplaceIndex(Model model){

        model.addAttribute("title", "Workplace Setup:");
        model.addAttribute("searchFail", null);

        return "workplace/index";

    }

    @RequestMapping(value="", method = RequestMethod.POST)
    public String processWorkPlaceIndex(Model model, @RequestParam String workplaceName){

        boolean workplaceExists = false;
        workplace usersWorkplace;

        for (workplace thisWorkplace : workplaceDao.findAll()){
            if(thisWorkplace.getWorkPlaceName().equals(workplaceName)){

                workplaceExists = true;   }
        }
        if (workplaceExists = true){
        }
    }*/

    @RequestMapping(value="/add", method = RequestMethod.GET)
    public String displayWorkplaceCreationForm(Model model){
        model.addAttribute("title", "Enter Workplace Name:");
        model.addAttribute(new workplace());

        return "workplace/add";
    }

    @RequestMapping(value="/add", method = RequestMethod.POST)
    public String processWorkplaceCreationForm(@Valid workplace newWorkplace, @RequestParam String workplaceName, Model model){

        newWorkplace.setWorkplaceName(workplaceName);
        workplaceDao.save(newWorkplace);

        return "login/dashboard";
    }

    @RequestMapping(value="/view", method = RequestMethod.GET)
    public String displaceWorkplaceViewForm(Model model, HttpServletRequest request, @CookieValue(value="loggedInCookie") String cookieValue){


        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);

        model.addAttribute("title", loggedInUser.getWorkplace().getWorkplaceName());





        return "workplace/view";}

    @RequestMapping(value="/view", method = RequestMethod.POST)
    public String processWorkplaceViewForm(Model model, HttpServletRequest request, @CookieValue(value="loggedInCookie") String cookieValue) {



        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);

        model.addAttribute("title", loggedInUser.getWorkplace().getWorkplaceName());

        return "workplace/view";

    }

    }






