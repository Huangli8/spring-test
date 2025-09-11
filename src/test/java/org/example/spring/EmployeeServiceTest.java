package org.example.spring;

import org.example.spring.Exception.*;
import org.example.spring.controller.UpdateEmployeeReq;
import org.example.spring.entity.Employee;
import org.example.spring.repository.EmployeeRepository;
import org.example.spring.repository.EmployeeRepositoryDBImpl;
import org.example.spring.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.awt.event.InputEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class EmployeeServiceTest {
    @Mock
    private EmployeeRepositoryDBImpl employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void should_not_create_when_given_employee_under_18(){
        Employee employee = new Employee();
        employee.setAge(17);
        employee.setGender("MALE");
        employee.setName("Tim");
        employee.setSalary(3000d);
        assertThrows(EmployeeNotAmongLegalAgeException.class,()->employeeService.createEmployee(employee));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void should_not_create_when_given_employee_upper_65(){
        Employee employee = new Employee();
        employee.setAge(66);
        employee.setGender("MALE");
        employee.setName("Tim");
        employee.setSalary(3000d);
        assertThrows(EmployeeNotAmongLegalAgeException.class,()->employeeService.createEmployee(employee));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void should_return_single_employee_given_exist_id(){
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setAge(22);
        employee.setGender("MALE");
        employee.setName("Tim");
        employee.setSalary(3000d);

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        Employee returnEmployee = employeeService.getEmployee(1L);

        assertEquals(employee.getName(),returnEmployee.getName());
        assertEquals(employee.getAge(),returnEmployee.getAge());
        assertEquals(employee.getGender(),returnEmployee.getGender());
        assertEquals(employee.getSalary(),returnEmployee.getSalary());

        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void should_return_not_found_exception_when_given_not_exist_id(){

        UpdateEmployeeReq updateEmployeeReq = new UpdateEmployeeReq();
        updateEmployeeReq.setAge(22);
        updateEmployeeReq.setName("Tim");
        updateEmployeeReq.setSalary(3000d);

        assertThrows(EmployeeNotFoundException.class,()->employeeService.getEmployee(1L));
        assertThrows(EmployeeNotFoundException.class,()->employeeService.deleteEmployee(1L));
        assertThrows(EmployeeNotFoundException.class,()->employeeService.updateEmployee(1L,updateEmployeeReq));
        verify(employeeRepository, times(3)).findById(1);
    }

    @Test
    void should_not_create_when_given_employee_over_30_salary_under_20000(){
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setAge(31);
        employee.setGender("MALE");
        employee.setName("Tim");
        employee.setSalary(19999d);
        assertThrows(EmployeeSalaryToLowException.class,()->employeeService.createEmployee(employee));
        verify(employeeRepository, never()).save(any());
    }

//    @Test
//    void should_set_active_status_true_when_create_employee_successfully(){
//        Employee employee = new Employee();
//        employee.setAge(20);
//        employee.setGender("MALE");
//        employee.setName("Tim");
//        employee.setSalary(5000);
//
//        Employee returnEmployee = employeeService.createEmployee(employee);
//        assertEquals(employee.getName(),returnEmployee.getName());
//        assertEquals(employee.getAge(),returnEmployee.getAge());
//        assertEquals(employee.getGender(),returnEmployee.getGender());
//        assertEquals(employee.getSalary(),returnEmployee.getSalary());
//        assertTrue(returnEmployee.isActiveStatus());
//
//        verify(employeeRepository, times(1)).save(any());
//    }

    @Test
    void should_not_delete_when_given_employee_already_inactive(){
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setAge(22);
        employee.setGender("MALE");
        employee.setName("Tim");
        employee.setSalary(3000d);
        employee.setActiveStatus(0);

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        assertThrows(EmployeeAlreadyDeletedException.class,()->employeeService.deleteEmployee(1L));

    }

    @Test
    void should_not_update_when_given_employee_inactive(){
        UpdateEmployeeReq updateEmployeeReq = new UpdateEmployeeReq();
        updateEmployeeReq.setAge(22);
        updateEmployeeReq.setName("Tim");
        updateEmployeeReq.setSalary(3000);

        Employee employeeToUpdated = new Employee();
        employeeToUpdated.setId(1L);
        employeeToUpdated.setAge(23);
        employeeToUpdated.setGender("MALE");
        employeeToUpdated.setName("Tim");
        employeeToUpdated.setSalary(4000d);
        employeeToUpdated.setActiveStatus(0);

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employeeToUpdated));
        assertThrows(InactiveEmployeeUpdateException.class,()->employeeService.updateEmployee(1L,updateEmployeeReq));
        verify(employeeRepository, never()).update(any());

    }

    @Test
    void should_not_create_when_given_employee_already_exist(){
        Employee employee = new Employee();
        employee.setId(2L);
        employee.setAge(20);
        employee.setGender("MALE");
        employee.setName("Tim");
        employee.setSalary(5000d);

        Employee existedEmployee = new Employee();
        existedEmployee.setId(1L);
        existedEmployee.setAge(20);
        existedEmployee.setGender("FEMALE");
        existedEmployee.setName("Tim");
        existedEmployee.setSalary(6000d);
        List<Employee> employees = new ArrayList<>(List.of(existedEmployee));
        when(employeeRepository.findAll()).thenReturn(employees);
        assertThrows(EmployeeAlreadyExistsException.class,()->employeeService.createEmployee(employee));
        verify(employeeRepository, never()).save(any());
    }

}