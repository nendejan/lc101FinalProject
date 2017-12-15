package com.nendejan.lc101FinalProject.models.data;

import com.nendejan.lc101FinalProject.models.Shift;
import com.nendejan.lc101FinalProject.models.workplace;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import javax.transaction.Transactional;

/**
 * Created by nico on 7/1/2017.
 */
@Repository
@Transactional
public interface ShiftDao extends CrudRepository<Shift, Integer> {

    Shift findByDayAndWorkplace(String day, workplace workplace);
    Shift findByIdAndWorkplace(int id, workplace workplace);
}