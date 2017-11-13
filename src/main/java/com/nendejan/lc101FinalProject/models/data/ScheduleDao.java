package com.nendejan.lc101FinalProject.models.data;

import com.nendejan.lc101FinalProject.models.Schedule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import javax.transaction.Transactional;

/**
 * Created by nico on 7/1/2017.
 */
@Repository
@Transactional
public interface ScheduleDao extends CrudRepository<Schedule, Integer> {}