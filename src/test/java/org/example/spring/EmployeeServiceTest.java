package org.example.spring;

import org.example.spring.Exception.*;
import org.example.spring.entity.Employee;
import org.example.spring.repository.EmployeeRepository;
import org.example.spring.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.awt.event.InputEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void should_not_create_when_given_employee_under_18(){
        Employee employee = new Employee();
        employee.setAge(17);
        employee.setGender("MALE");
        employee.setName("Tim");
        employee.setSalary(3000);
        assertThrows(EmployeeNotAmongLegalAgeException.class,()->employeeService.createEmployee(employee));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void should_not_create_when_given_employee_upper_65(){
        Employee employee = new Employee();
        employee.setAge(66);
        employee.setGender("MALE");
        employee.setName("Tim");
        employee.setSalary(3000);
        assertThrows(EmployeeNotAmongLegalAgeException.class,()->employeeService.createEmployee(employee));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void should_return_single_employee_given_exist_id(){
        Employee employee = new Employee();
        employee.setId(1);
        employee.setAge(22);
        employee.setGender("MALE");
        employee.setName("Tim");
        employee.setSalary(3000);

        when(employeeRepository.findById(1)).thenReturn(employee);
        Employee returnEmployee = employeeService.getEmployee(1);

        assertEquals(employee.getName(),returnEmployee.getName());
        assertEquals(employee.getAge(),returnEmployee.getAge());
        assertEquals(employee.getGender(),returnEmployee.getGender());
        assertEquals(employee.getSalary(),returnEmployee.getSalary());

        verify(employeeRepository, times(1)).findById(1);
    }

    @Test
    void should_return_not_found_exception_when_given_not_exist_id(){
        Employee employee = new Employee();
        employee.setId(1);
        employee.setAge(22);
        employee.setGender("MALE");
        employee.setName("Tim");
        employee.setSalary(3000);

        assertThrows(EmployeeNotFoundException.class,()->employeeService.getEmployee(1));
        assertThrows(EmployeeNotFoundException.class,()->employeeService.deleteEmployee(1));
        assertThrows(EmployeeNotFoundException.class,()->employeeService.updateEmployee(1,employee));
        verify(employeeRepository, times(3)).findById(1);
    }

    @Test
    void should_not_create_when_given_employee_over_30_salary_under_20000(){
        Employee employee = new Employee();
        employee.setId(1);
        employee.setAge(31);
        employee.setGender("MALE");
        employee.setName("Tim");
        employee.setSalary(19999);
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
        employee.setId(1);
        employee.setAge(22);
        employee.setGender("MALE");
        employee.setName("Tim");
        employee.setSalary(3000);
        employee.setActiveStatus(false);

        when(employeeRepository.findById(1)).thenReturn(employee);
        assertThrows(EmployeeAlreadyDeletedException.class,()->employeeService.deleteEmployee(1));

    }

    @Test
    void should_not_update_when_given_employee_inactive(){
        Employee employee = new Employee();
        employee.setId(1);
        employee.setAge(22);
        employee.setGender("MALE");
        employee.setName("Tim");
        employee.setSalary(3000);

        Employee employeeToUpdated = new Employee();
        employeeToUpdated.setId(1);
        employeeToUpdated.setAge(23);
        employeeToUpdated.setGender("MALE");
        employeeToUpdated.setName("Tim");
        employeeToUpdated.setSalary(4000);
        employee.setActiveStatus(false);

        when(employeeRepository.findById(1)).thenReturn(employeeToUpdated);
        assertThrows(InactiveEmployeeUpdateException.class,()->employeeService.updateEmployee(1,employee));
        verify(employeeRepository, never()).update(1,employee);

    }

}