package com.nendejan.lc101FinalProject.models.data;

import com.nendejan.lc101FinalProject.models.EmployeeCategory;
import com.nendejan.lc101FinalProject.models.workplace;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import javax.transaction.Transactional;

/**
 * Created by nico on 7/1/2017.
 */
@Repository
@Transactional
public interface EmployeeCategoryDao extends CrudRepository<EmployeeCategory, Integer> {
/*todo: find syntax for creation of custom dao methods*/
    EmployeeCategory findByName(String name);
    EmployeeCategory findByNameAndWorkplace(String name, workplace workplace);
}