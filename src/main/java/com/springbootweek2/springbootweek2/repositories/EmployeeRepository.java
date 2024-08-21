package com.springbootweek2.springbootweek2.repositories;

import com.springbootweek2.springbootweek2.dto.EmployeeDTO;
import com.springbootweek2.springbootweek2.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
 List<EmployeeEntity> findByName(String name);
}
