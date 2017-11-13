package com.nendejan.lc101FinalProject.models.forms;

import com.nendejan.lc101FinalProject.models.Schedule;
import com.nendejan.lc101FinalProject.models.Shift;

public class AddScheduleShiftForm {
    private Schedule schedule;
    private Iterable<Shift> shifts;
    private int scheduleId;
    private int shiftId;

    public AddScheduleShiftForm(){};

    public AddScheduleShiftForm(Schedule schedule, Iterable<Shift> shifts){

        this.schedule = schedule;
        this.shifts = shifts;


    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Iterable<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(Iterable<Shift> shifts) {
        this.shifts = shifts;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }
}