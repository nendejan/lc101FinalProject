package com.nendejan.lc101FinalProject.controllers;

import com.nendejan.lc101FinalProject.models.Schedule;
import com.nendejan.lc101FinalProject.models.Shift;
import com.nendejan.lc101FinalProject.models.data.EmployeeCategoryDao;
import com.nendejan.lc101FinalProject.models.data.EmployeeDao;
import com.nendejan.lc101FinalProject.models.data.ScheduleDao;
import com.nendejan.lc101FinalProject.models.data.ShiftDao;
import com.nendejan.lc101FinalProject.models.forms.AddScheduleShiftForm;
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
@RequestMapping("schedules")
public class ScheduleController {

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private EmployeeCategoryDao employeeCategoryDao;

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private ShiftDao shiftDao;

    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("schedules", scheduleDao.findAll());
        model.addAttribute("title", "Schedules");

        return "schedules/index";

    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddScheduleForm(Model model, Schedule schedule) {
        Schedule newSchedule;

        model.addAttribute ("title", "Create New Schedule");
        model.addAttribute("schedule", new Schedule());
        return "schedules/add";
    }
    @RequestMapping(value = "view/{id}", method = RequestMethod.GET)
    public String viewSchedule(@PathVariable int id, Model model){
        Schedule schedule =scheduleDao.findOne(id);
        model.addAttribute("title", schedule.getScheduleDates());
        model.addAttribute("schedule", schedule);

        return "schedules/view";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddScheduleForm(Model model, @ModelAttribute @Valid Schedule schedule, Errors errors){

        if(errors.hasErrors()){
            model.addAttribute("title", "Add Schedule");
            return "schedules/add";
        }

        scheduleDao.save(schedule);
        return "redirect:view/" + schedule.getId();
    }

    @RequestMapping(value="add-shift/{scheduleId}", method = RequestMethod.GET)
    public String addShift(Model model, @PathVariable int scheduleId){
        Schedule schedule =scheduleDao.findOne(scheduleId);
        AddScheduleShiftForm shiftForm = new AddScheduleShiftForm(schedule, shiftDao.findAll());

        model.addAttribute("title", "Add Shift to Schedule: " + schedule.getScheduleDates());
        model.addAttribute("forms", shiftForm);

        return "schedules/add-shift";
    }

    @RequestMapping(value="add-shift/{scheduleId}", method= RequestMethod.POST)
    public String addShift(Model model, @ModelAttribute @Valid AddScheduleShiftForm shiftForm, Errors errors, @PathVariable int scheduleId){

        if(errors.hasErrors()){
            model.addAttribute("title", "Add Shift to Schedule");
            return "schedules/add-shift" + scheduleId;
        }

        Schedule schedule = scheduleDao.findOne(shiftForm.getScheduleId());
        Shift shift = shiftDao.findOne(shiftForm.getShiftId());

        schedule.addShift(shift);
        scheduleDao.save(schedule);

        return "redirect:../view/" + schedule.getId();

    }

    @RequestMapping(value="remove", method= RequestMethod.GET)
    public String displayRemoveScheduleForm(Model model) {

        model.addAttribute("schedules", scheduleDao.findAll());
        model.addAttribute("title", "Remove Schedules");
        return "schedules/remove";

    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveScheduleForm(@RequestParam int[] scheduleIds){
        for (int scheduleId : scheduleIds){
            scheduleDao.delete(scheduleId);

        }
        return "redirect:";

    }
}