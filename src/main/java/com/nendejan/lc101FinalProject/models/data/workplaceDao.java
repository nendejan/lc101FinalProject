package com.nendejan.lc101FinalProject.models.data;


import com.nendejan.lc101FinalProject.models.workplace;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;


@Repository
@Transactional
public interface workplaceDao extends CrudRepository<workplace, Integer> {

    workplace findByWorkplaceName(String workplaceName);
}
