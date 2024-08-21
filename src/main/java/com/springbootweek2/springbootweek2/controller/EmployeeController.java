package com.springbootweek2.springbootweek2.controller;

import com.springbootweek2.springbootweek2.dto.EmployeeDTO;

import com.springbootweek2.springbootweek2.entities.EmployeeEntity;
import com.springbootweek2.springbootweek2.services.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping(path="/employees")
public class EmployeeController {


    private final EmployeeService employeeService;

    public EmployeeController( EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(path="/getSecretMessage")
    public String getMySecretMessage(){
        return "My secret message is : bdjjnffnfndnf";
    }

    @GetMapping(path="/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable(name="employeeId") Long id)
    {
        Optional<EmployeeDTO> employeeDTO = employeeService.getEmployeeById(id);
        return employeeDTO
                .map(employeeDTO1 -> ResponseEntity.ok(employeeDTO1))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(@RequestParam(required = false, name="inputAge") Integer age,
                                                @RequestParam(required = false) String sortBy)
    {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createNewEmployee(@RequestBody EmployeeDTO inputEmployee){
        EmployeeDTO savedEmployee=employeeService.createNewEmployee(inputEmployee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @PutMapping(path="/{employeeId}")
    public ResponseEntity<EmployeeDTO> updateEmployeeById(@RequestBody EmployeeDTO inputEmployee, @PathVariable(name="employeeId") Long id){
        return ResponseEntity.ok(employeeService.updateEmployeeById(inputEmployee,id));
    }

    @DeleteMapping(path="/{employeeId}")
    public ResponseEntity<Boolean> deleteEmployeeById(@PathVariable(name="employeeId") Long id)
    {
       boolean gotDeleted= employeeService.deleteEmployeeById(id);
       if(gotDeleted) return ResponseEntity.ok(true);
       else return ResponseEntity.notFound().build();
    }

    @PatchMapping(path="/{employeeId}")
    public ResponseEntity<EmployeeDTO> updatePartialEmployeeById(@RequestBody Map<String, Object> updates, @PathVariable(name="employeeId") Long id)
    {
       EmployeeDTO employeeDTO= employeeService.updatePartialEmployeeById(updates,id);
       if(employeeDTO==null) return ResponseEntity.notFound().build();
       return ResponseEntity.ok(employeeDTO);
    }
}
