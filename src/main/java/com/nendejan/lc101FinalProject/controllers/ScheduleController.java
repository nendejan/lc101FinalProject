package com.nendejan.lc101FinalProject.controllers;

import com.nendejan.lc101FinalProject.models.Employee;
import com.nendejan.lc101FinalProject.models.Schedule;
import com.nendejan.lc101FinalProject.models.Shift;
import com.nendejan.lc101FinalProject.models.User;
import com.nendejan.lc101FinalProject.models.data.*;
import com.nendejan.lc101FinalProject.models.forms.AddScheduleShiftForm;
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
@RequestMapping("login/schedules")
public class ScheduleController {

    @Autowired
    private workplaceDao workplaceDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private EmployeeCategoryDao employeeCategoryDao;

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private ShiftDao shiftDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, HttpServletRequest request, @CookieValue(value="loggedInCookie") String cookieValue) {

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);

        if(loggedInUser.getWorkplace().getWorkplaceSchedules().isEmpty()== true){
            model.addAttribute("hasSchedules", false);

            model.addAttribute("schedules", loggedInUser.getWorkplace().getWorkplaceSchedules());



            /*TODO LOGIC: this is returning the workplace's shift template, not a specific schedules shifts, the template cannot have employees like a schedules shift list can*/
            model.addAttribute("title", "Schedules");

            return "schedules/index";

        }


        model.addAttribute("employees", loggedInUser.getWorkplace().getEmployeeRoster());
        model.addAttribute("hasSchedules", true);
        model.addAttribute("schedules", loggedInUser.getWorkplace().getWorkplaceSchedules());




        model.addAttribute("title", "Schedules");

        return "schedules/index";

    }


    @RequestMapping(value = "", method = RequestMethod.POST)
    public String processIndex(Model model, HttpServletRequest request, @CookieValue(value="loggedInCookie") String cookieValue, @RequestParam String addEmployeeToShift, @RequestParam  int shiftId, @RequestParam int scheduleId ) {

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);



        if(loggedInUser.getWorkplace().getWorkplaceSchedules().isEmpty()== true){
            model.addAttribute("hasSchedules", false);

            model.addAttribute("schedules", loggedInUser.getWorkplace().getWorkplaceSchedules());

            /*TODO LOGIC: this is returning the workplace's shift template, not a specific schedules shifts, the template cannot have employees like a schedules shift list can*/
            model.addAttribute("title", "Schedules");

            return "schedules/index";
        }



        /*uses parameters from form to find objects in reference to current users workplace*/

        Schedule thisSchedule = scheduleDao.findByIdAndWorkplace(scheduleId, loggedInUser.getWorkplace());
        Shift thisShift = shiftDao.findByIdAndWorkplace(shiftId, loggedInUser.getWorkplace());
        Employee thisEmployee = employeeDao.findByNameAndWorkplace(addEmployeeToShift, loggedInUser.getWorkplace());


        




        model.addAttribute("employees", loggedInUser.getWorkplace().getEmployeeRoster());
        model.addAttribute("hasSchedules", true);
        model.addAttribute("schedules", loggedInUser.getWorkplace().getWorkplaceSchedules());
        model.addAttribute("title", "Schedules");

        return"redirect:schedules/index";
    }




    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddScheduleForm(Model model, HttpServletRequest request, @CookieValue(value="loggedInCookie") String cookieValue) {

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);


        model.addAttribute ("title", "Create New Schedule");
        model.addAttribute("schedule", new Schedule());
        return "schedules/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddScheduleForm(Model model, @ModelAttribute @Valid Schedule schedule, Errors errors,  HttpServletRequest request, @CookieValue(value="loggedInCookie") String cookieValue){

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);

        if(errors.hasErrors()){
            model.addAttribute("title", "Add Schedule");
            return "schedules/add";
        }


        for(Shift shift : loggedInUser.getWorkplace().getWorkplaceShifts()){
            schedule.addShift(shift);}


        loggedInUser.getWorkplace().addSchedule(schedule);
        scheduleDao.save(schedule);
        return "redirect:/login/schedules";
    }
/*
    @RequestMapping(value = "view", method = RequestMethod.GET)
    public String viewSchedule(@PathVariable int id, Model model,  HttpServletRequest request, @CookieValue(value="loggedInCookie") String cookieValue) {

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);


        Schedule schedule = scheduleDao.findOne(id);
        model.addAttribute("title", schedule.getScheduleDates());
        model.addAttribute("schedule", schedule);

        return "schedules/view";
    }*/


    @RequestMapping(value="remove", method= RequestMethod.GET)
    public String displayRemoveScheduleForm(Model model, HttpServletRequest request, HttpServletResponse response, @CookieValue(value="loggedInCookie") String cookieValue) {


        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);

        if(loggedInUser.getWorkplace().getWorkplaceSchedules().isEmpty() ==true){

            model.addAttribute("hasSchedules", false);
            model.addAttribute("schedules", loggedInUser.getWorkplace().getWorkplaceSchedules());
            model.addAttribute("title", "Remove Schedules");
            return "schedules/index";
        }

        model.addAttribute("hasSchedules", true);
        model.addAttribute("schedules", loggedInUser.getWorkplace().getWorkplaceSchedules());
        model.addAttribute("title", "Remove Schedules");

        return "schedules/remove";

    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveScheduleForm(@RequestParam List<Integer> schedulesToRemove,  HttpServletRequest request, HttpServletResponse response, @CookieValue(value="loggedInCookie") String cookieValue){

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);

        for(Integer removeMeId : schedulesToRemove){
            Schedule removeMe = scheduleDao.findOne(removeMeId);
            loggedInUser.getWorkplace().removeSchedule(removeMe);



          scheduleDao.delete(removeMeId);

        }



        return "redirect:/login/schedules/remove";

    }
}