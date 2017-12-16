package com.nendejan.lc101FinalProject.controllers;

import com.nendejan.lc101FinalProject.models.*;
import com.nendejan.lc101FinalProject.models.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
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

            model.addAttribute("title", "Schedules");

            return "schedules/index";

        }




        model.addAttribute("hasSchedules", true);
        model.addAttribute("schedules", loggedInUser.getWorkplace().getWorkplaceSchedules());




        model.addAttribute("title", "Schedules");

        return "schedules/index";

    }


    @RequestMapping(value = "", method = RequestMethod.POST)
    public String processIndex(HttpServletResponse response, @CookieValue(value="loggedInCookie") String cookieValue, @RequestParam String selectedSchedule, Model model ) {

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);



        if(loggedInUser.getWorkplace().getWorkplaceSchedules().isEmpty()== true){
            model.addAttribute("hasSchedules", false);

            model.addAttribute("schedules", loggedInUser.getWorkplace().getWorkplaceSchedules());

            model.addAttribute("title", "Schedules");

            return "schedules/index";
        }


        model.addAttribute("hasSchedules", true);
        model.addAttribute("schedules", loggedInUser.getWorkplace().getWorkplaceSchedules());
        model.addAttribute("title", "Schedules");



        Schedule thisSchedule = scheduleDao.findByScheduleDatesAndWorkplace(selectedSchedule, loggedInUser.getWorkplace());



        String scheduleCookieValueString = Integer.toString(thisSchedule.getId());
        Cookie thisScheduleCookie = new Cookie("thisScheduleCookie", scheduleCookieValueString);
        thisScheduleCookie.setMaxAge(24 * 60 * 60);

        response.addCookie(thisScheduleCookie);


        return"redirect:schedules/view";
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
    public String processAddScheduleForm(Model model, @ModelAttribute @Valid Schedule schedule, Errors errors,  HttpServletResponse response, @CookieValue(value="loggedInCookie") String cookieValue){

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);

        if(errors.hasErrors()){
            model.addAttribute("title", "Add Schedule");
            return "schedules/add";
        }




        for(Shift shift : loggedInUser.getWorkplace().getWorkplaceShifts()){
            Shift shiftClone = new Shift();

            shiftClone.setDay(shift.getDay());
            shiftClone.setStartTimeHour(shift.getStartTimeHour());
            shiftClone.setStartTimeMinute(shift.getStartTimeMinute());
            shiftClone.setStartTimeAMPM(shift.getStartTimeAMPM());
            shiftClone.setEndTimeHour(shift.getEndTimeHour());
            shiftClone.setEndTimeMinute(shift.getEndTimeMinute());
            shiftClone.setEndTimeAMPM(shift.getEndTimeAMPM());
            shiftClone.setWorkplace(shift.getWorkplace());


            shiftDao.save(shiftClone);
            schedule.addShift(shiftClone);
            scheduleDao.save(schedule);
        }


        loggedInUser.getWorkplace().addSchedule(schedule);
        scheduleDao.save(schedule);





        String scheduleCreationCookieValueString = Integer.toString(schedule.getId());
        Cookie newScheduleCookie = new Cookie("newScheduleCookie", scheduleCreationCookieValueString);
        newScheduleCookie.setMaxAge(24 * 60 * 60);

        response.addCookie(newScheduleCookie);


        return "redirect:/login/schedules/addEmployee";
    }

    @RequestMapping(value = "addEmployee", method = RequestMethod.GET)
    public String displayAddEmployeeToShiftOnScheduleForm(Model model, HttpServletRequest request, @CookieValue(value="loggedInCookie") String cookieValue, @CookieValue(value="newScheduleCookie") String newScheduleCookieValue, @RequestParam(required = false) Integer shiftId, @RequestParam(required = false) String addEmployeeToShift, @RequestParam(required= false) String action) {

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);

        int newScheduleId = Integer.parseInt(newScheduleCookieValue);

        Schedule newSchedule = scheduleDao.findOne(newScheduleId);



        if(loggedInUser.getWorkplace().getWorkplaceSchedules().isEmpty()== true){
            model.addAttribute("hasSchedules", false);
            model.addAttribute("displayMessage", "This Workplace doesn't seem to have any schedules setup... How did you get here?????");
            model.addAttribute("newSchedule", null);
            model.addAttribute("title", "Employee Setup");
            model.addAttribute("shiftSelected", false);

            return "schedules/addEmployee";

        }

        if(shiftId == null){
            model.addAttribute("displayMessage", "Choose a shift to start adding employees to: ");
            model.addAttribute("shiftSelected", false);
            model.addAttribute("employees", loggedInUser.getWorkplace().getEmployeeRoster());
            model.addAttribute("hasSchedules", true);
            model.addAttribute("newSchedule", newSchedule);
            model.addAttribute("title", "Employee Setup");

        return "schedules/addEmployee";}

        if(shiftId != null){
            model.addAttribute("thisShift", shiftDao.findByIdAndWorkplace(shiftId, loggedInUser.getWorkplace()));
            model.addAttribute("displayMessage", "Choose which employee to add to shift: ");
            model.addAttribute("shiftSelected", true);
            model.addAttribute("employees", loggedInUser.getWorkplace().getEmployeeRoster());
            model.addAttribute("hasSchedules", true);
            model.addAttribute("newSchedule", newSchedule);
            model.addAttribute("title", "Employee Setup");


            return "redirect:";

        }
        return "schedules/addEmployee";
    }

    @RequestMapping(value="addEmployee", method= RequestMethod.POST)
    public String processAddEmployeeToShiftOnScheduleForm(Model model, HttpServletResponse response, @CookieValue(value="loggedInCookie") String cookieValue, @CookieValue(value="newScheduleCookie") String newScheduleCookieValue, @RequestParam(required = false) Integer shiftId, @RequestParam(required = false) String addEmployeeToShift, @RequestParam(required= false)String action) {

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);

        int newScheduleId = Integer.parseInt(newScheduleCookieValue);

        Schedule newSchedule = scheduleDao.findOne(newScheduleId);



        if(loggedInUser.getWorkplace().getWorkplaceSchedules().isEmpty()== true){
            model.addAttribute("hasSchedules", false);
            model.addAttribute("displayMessage", "This Workplace doesn't seem to have any schedules setup...How did you get here?????");
            model.addAttribute("newSchedule", null);
            model.addAttribute("title", "Employee Setup");
            model.addAttribute("shiftSelected", false);

            return "schedules/addEmployee";

        }
        if(shiftId == null && addEmployeeToShift == null){
            model.addAttribute("displayMessage", "Choose a shift to start adding employees to: ");
            model.addAttribute("shiftSelected", false);
            model.addAttribute("employees", loggedInUser.getWorkplace().getEmployeeRoster());
            model.addAttribute("hasSchedules", true);
            model.addAttribute("newSchedule", newSchedule);
            model.addAttribute("title", "Employee Setup");

            return "schedules/addEmployee";}

        if(shiftId != null && addEmployeeToShift ==null) {
            model.addAttribute("thisShift", shiftDao.findByIdAndWorkplace(shiftId, loggedInUser.getWorkplace()));
            model.addAttribute("displayMessage", "Choose which employee to add to shift: ");
            model.addAttribute("shiftSelected", true);
            model.addAttribute("employees", loggedInUser.getWorkplace().getEmployeeRoster());
            model.addAttribute("hasSchedules", true);
            model.addAttribute("newSchedule", newSchedule);
            model.addAttribute("title", "Employee Setup");


            return "schedules/addEmployee";
        }

        if(shiftId != null && addEmployeeToShift !=null){
            model.addAttribute("thisShift", shiftDao.findByIdAndWorkplace(shiftId, loggedInUser.getWorkplace()));
            model.addAttribute("displayMessage", "Choose which employee to add to shift: ");
            model.addAttribute("shiftSelected", true);
            model.addAttribute("employees", loggedInUser.getWorkplace().getEmployeeRoster());
            model.addAttribute("hasSchedules", true);
            model.addAttribute("newSchedule", newSchedule);
            model.addAttribute("title", "Employee Setup");

            Shift thisShift = shiftDao.findByIdAndWorkplace(shiftId, loggedInUser.getWorkplace());
            Employee thisEmployee = employeeDao.findByNameAndWorkplace(addEmployeeToShift, loggedInUser.getWorkplace());


            /* TODO this isnt working
            if(action == "Add"){
                thisEmployee.addShiftToScheduledShifts(thisShift);
                employeeDao.save(thisEmployee);
                thisShift.addEmployee(thisEmployee);
                shiftDao.save(thisShift);}

            if(action == "Remove"){
                thisEmployee.removeShiftFromScheduledShifts(thisShift);
                employeeDao.save(thisEmployee);
                thisShift.removeEmployee(thisEmployee);
                shiftDao.save(thisShift);}*/

            }



            String scheduleCookieValueString = Integer.toString(newSchedule.getId());
            Cookie thisScheduleCookie = new Cookie("thisScheduleCookie", scheduleCookieValueString);
            thisScheduleCookie.setMaxAge(24 * 60 * 60);

            response.addCookie(thisScheduleCookie);




            return "schedules/addEmployee";}


    @RequestMapping(value = "view", method = RequestMethod.GET)
    public String displayViewScheduleForm(Model model, HttpServletRequest request, @CookieValue(value="loggedInCookie") String cookieValue, @CookieValue(value="thisScheduleCookie") String scheduleCookieValue) {

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);

        int thisScheduleCookieId = Integer.parseInt(scheduleCookieValue);

        Schedule thisSchedule = scheduleDao.findOne(thisScheduleCookieId);

        if(loggedInUser.getWorkplace().getWorkplaceSchedules().isEmpty()== true){
            model.addAttribute("hasSchedules", false);

            model.addAttribute("thisSchedule", null);



            /*TODO LOGIC: this is returning the workplace's shift template, not a specific schedules shifts, the template cannot have employees like a schedules shift list can*/
            model.addAttribute("title", "Schedules");

            return "schedules/view";

        }



        model.addAttribute("hasSchedules", true);
        model.addAttribute("thisSchedule", thisSchedule);




        model.addAttribute("title", "Schedules");



        return "schedules/view";
    }

    @RequestMapping(value = "view", method = RequestMethod.POST)
    public String processViewScheduleForm(Model model, HttpServletRequest request, @CookieValue(value="loggedInCookie") String cookieValue, @CookieValue(value="thisScheduleCookie") String scheduleCookieValue) {

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);

        int thisScheduleCookieId = Integer.parseInt(scheduleCookieValue);

        Schedule thisSchedule = scheduleDao.findOne(thisScheduleCookieId);


        if (loggedInUser.getWorkplace().getWorkplaceSchedules().isEmpty() == true) {
            model.addAttribute("hasSchedules", false);

            model.addAttribute("thisSchedule", null);

            /*TODO LOGIC: this is returning the workplace's shift template, not a specific schedules shifts, the template cannot have employees like a schedules shift list can*/
            model.addAttribute("title", "Schedules");

            return "schedules/view";
        }



        model.addAttribute("hasSchedules", true);
        model.addAttribute("thisSchedule", thisSchedule);
        model.addAttribute("title", "Schedules");

        return "schedules/view";

    }


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
    public String processRemoveScheduleForm(@RequestParam int scheduleToRemove,  HttpServletRequest request, HttpServletResponse response, @CookieValue(value="loggedInCookie") String cookieValue){

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);

        workplace thisWorkplace = loggedInUser.getWorkplace();

        Schedule removeMe = scheduleDao.findOne(scheduleToRemove);




        for(Shift deleteThisShift : removeMe.getShiftsOfWeek()){
            thisWorkplace.removeShift(deleteThisShift);

        }
        removeMe.getShiftsOfWeek().clear();

        for(Shift deleteThisShift : removeMe.getShiftsOfWeek()){
        shiftDao.delete(deleteThisShift);}



        loggedInUser.getWorkplace().removeSchedule(removeMe);
        scheduleDao.delete(removeMe);





        return "redirect:/login/schedules/remove";

    }
}