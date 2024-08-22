package com.springbootweek2.springbootweek2.services;


import com.springbootweek2.springbootweek2.dto.EmployeeDTO;
import com.springbootweek2.springbootweek2.entities.EmployeeEntity;
import com.springbootweek2.springbootweek2.exceptions.ResourceNotFoundException;
import com.springbootweek2.springbootweek2.repositories.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;


import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    public Optional<EmployeeDTO> getEmployeeById(Long id)
    {
      //  Optional<EmployeeEntity> employeeEntity= employeeRepository.findById(id);
        return  employeeRepository.findById(id).map(employeeEntity1 -> modelMapper.map(employeeEntity1,EmployeeDTO.class));

    }

    public List<EmployeeDTO> getAllEmployees() {

        List<EmployeeEntity> employeeEntities= employeeRepository.findAll();
        return employeeEntities
                .stream()
                .map(employeeEntity -> modelMapper.map(employeeEntity, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    public EmployeeDTO createNewEmployee(EmployeeDTO inputEmployee) {
        EmployeeEntity toSaveEmployee=modelMapper.map(inputEmployee,EmployeeEntity.class);
        EmployeeEntity savedEmployee=employeeRepository.save(toSaveEmployee);
        return modelMapper.map(savedEmployee,EmployeeDTO.class);
    }

    public EmployeeDTO updateEmployeeById(EmployeeDTO employeeDTO,Long id) {
        isExistsByEmployeeId(id);
        EmployeeEntity employeeEntity=modelMapper.map(employeeDTO,EmployeeEntity.class);
        employeeEntity.setId(id);
        EmployeeEntity savedEmployee=employeeRepository.save(employeeEntity);
        return modelMapper.map(savedEmployee,EmployeeDTO.class);
    }

    public boolean deleteEmployeeById(Long id) {
        isExistsByEmployeeId(id);
        employeeRepository.deleteById(id);
        return true;
    }
    
    public EmployeeDTO updatePartialEmployeeById( Map<String,Object> updates, Long id)
    {
        isExistsByEmployeeId(id);
        EmployeeEntity employeeEntity= employeeRepository.findById(id).get();
        updates.forEach((field, value)->{
            Field fieldToBeUpdated= ReflectionUtils.findRequiredField(EmployeeEntity.class, field);
            fieldToBeUpdated.setAccessible(true);
            ReflectionUtils.setField(fieldToBeUpdated, employeeEntity, value);
        });
        return modelMapper.map(employeeRepository.save(employeeEntity), EmployeeDTO.class);
    }

    private void isExistsByEmployeeId(Long id) {
        boolean exists= employeeRepository.existsById(id);
        if(!exists) throw new ResourceNotFoundException("Employee not found with id " +id);
    }
}
