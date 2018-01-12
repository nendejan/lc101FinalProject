package com.nendejan.lc101FinalProject.controllers;

import com.nendejan.lc101FinalProject.models.Employee;
import com.nendejan.lc101FinalProject.models.Schedule;
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
@RequestMapping("login/shifts")
public class ShiftController {

    @Autowired
    private workplaceDao workplaceDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private EmployeeCategoryDao employeeCategoryDao;

    @Autowired
    private ShiftDao shiftDao;

    @Autowired
    private ScheduleDao scheduleDao;

    @RequestMapping(value="", method=RequestMethod.GET)
    public String index(Model model, HttpServletRequest request, @CookieValue(value="loggedInCookie") String cookieValue){

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);
        ArrayList<Shift> workPlacePrimaryShifts =  new ArrayList<>();

        for(Shift shift : loggedInUser.getWorkplace().getWorkplaceShifts()){
            if(shift.isPrimaryShift() == true){
                workPlacePrimaryShifts.add(shift);
            }

        }

        model.addAttribute("shifts", workPlacePrimaryShifts);
        model.addAttribute("title", "Shifts for Schedule");


        return "shifts/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddShiftForm(Model model) {


         model.addAttribute("title", "Add Shift");
        model.addAttribute("shift", new Shift());


        return "shifts/add";
    }


    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddShiftForm(Model model, HttpServletRequest request, @CookieValue(value="loggedInCookie") String cookieValue, @ModelAttribute @Valid Shift shift, Errors errors) {

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);

        if(errors.hasErrors()) {
            model.addAttribute("title", "Add Shift");
            return "shifts/add";
        }
        shift.setPrimaryShift(true);
        loggedInUser.getWorkplace().addShift(shift);
        shiftDao.save(shift);
        return "redirect:";
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveShiftForm(Model model, HttpServletRequest request, HttpServletResponse response, @CookieValue(value="loggedInCookie") String cookieValue) {

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);


        if (loggedInUser.getWorkplace().getWorkplaceShifts().isEmpty()==true){


            model.addAttribute("hasShifts", false);
            model.addAttribute("shifts", loggedInUser.getWorkplace().getWorkplaceShifts());
            model.addAttribute("title", "Remove Shifts");


            return "shifts/index";
        }




        model.addAttribute("hasShifts", true);
        model.addAttribute("shifts", loggedInUser.getWorkplace().getWorkplacePrimaryShifts(loggedInUser.getWorkplace()));
        model.addAttribute("title", "Remove Shifts");
        return "shifts/remove";

    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveShiftForm(Model model, HttpServletRequest request, @CookieValue(value="loggedInCookie") String cookieValue, @RequestParam(required = false) List<Integer> shiftsToRemove) {

        int loggedInUserId = Integer.parseInt(cookieValue);

        User loggedInUser = userDao.findOne(loggedInUserId);

        model.addAttribute("shifts", loggedInUser.getWorkplace().getWorkplacePrimaryShifts(loggedInUser.getWorkplace()));
        model.addAttribute("title", "Remove Shift");


        if(shiftsToRemove==null){
            model.addAttribute("hasShifts", true);
            model.addAttribute("shifts", loggedInUser.getWorkplace().getWorkplacePrimaryShifts(loggedInUser.getWorkplace()));
            model.addAttribute("title", "Remove Shift");

            model.addAttribute("noShiftSelected", "You must select a shift to remove.");

            return "shifts/remove";

        }
        if(shiftsToRemove !=null){
        for (Integer removeMeId : shiftsToRemove){
            Shift removeMe = shiftDao.findOne(removeMeId);
            loggedInUser.getWorkplace().removeShift(removeMe);

            for(Schedule schedule : loggedInUser.getWorkplace().getWorkplaceSchedules()){
                schedule.removeShift(removeMe);
            }
            for(Employee employee : loggedInUser.getWorkplace().getEmployeeRoster()){
                employee.removeShiftFromScheduledShifts(removeMe);
                employee.removeShiftAvailability(removeMe);
            }


            shiftDao.delete(removeMeId);
            return "redirect:/login/shifts/remove";

        }}
        return "redirect:/login/shifts/remove";
    }



}