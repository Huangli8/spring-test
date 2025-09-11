package org.example.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.spring.entity.Company;
import org.example.spring.entity.Employee;
import org.example.spring.repository.CompanyRepository;
import org.example.spring.repository.EmployeeRepository;
import org.example.spring.repository.EmployeeRepositoryDBImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.param;

@SpringBootTest
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    void setUp(){
        employeeRepository.deleteAll();
        companyRepository.deleteAll();
    }
    @Test
    void should_create_employee_when_given_a_valid_body() throws Exception {
        String requestBody = """
                {
                   "name": "John",
                   "age":30,
                   "gender":"MALE",
                   "salary":5000
                }
                """;
        mockMvc.perform(post("/employees1").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.salary").value(5000))
                .andExpect(jsonPath("$.activeStatus").value(1));

    }

    @Test
    void should_get_employee_when_given_a_valid_id() throws Exception {
        String requestBody = """
                {
                   "name": "John",
                   "age":30,
                   "gender":"MALE",
                   "salary":5000
                }
                """;
       // mockMvc.perform(post("/employees1").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isCreated());
        mockMvc.perform(get("/employees/{id}", createEmployee(requestBody)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.salary").value(5000));
    }

    @Test
    void should_get_employees_when_given_gender() throws Exception {
        String requestBody1 = """
                {
                   "name": "John",
                   "age":30,
                   "gender":"MALE",
                   "salary":5000
                }
                """;
        String requestBody2 = """
                {
                   "name": "Amy",
                   "age":30,
                   "gender":"FEMALE",
                   "salary":8000
                }
                """;
        String requestBody3 = """
                {
                   "name": "Tim",
                   "age":20,
                   "gender":"MALE",
                   "salary":4000
                }
                """;
        mockMvc.perform(post("/employees1").contentType(MediaType.APPLICATION_JSON).content(requestBody1)).andExpect(status().isCreated());
        mockMvc.perform(post("/employees1").contentType(MediaType.APPLICATION_JSON).content(requestBody2)).andExpect(status().isCreated());
        mockMvc.perform(post("/employees1").contentType(MediaType.APPLICATION_JSON).content(requestBody3)).andExpect(status().isCreated());
        mockMvc.perform(get("/employees").param("gender", "MALE").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].age").value(30))
                .andExpect(jsonPath("$[0].gender").value("MALE"))
                .andExpect(jsonPath("$[0].salary").value(5000))
                .andExpect(jsonPath("$[1].name").value("Tim"))
                .andExpect(jsonPath("$[1].age").value(20))
                .andExpect(jsonPath("$[1].gender").value("MALE"))
                .andExpect(jsonPath("$[1].salary").value(4000));
    }

    @Test
    void should_update_employee_when_given_a_valid_id() throws Exception {
        Company company = new Company();
        company.setName("Google");
        companyRepository.save(company);

        Employee employee = new Employee();
        employee.setName("Kate");
        employee.setSalary(6000d);
        employee.setAge(24);
        employee.setGender("Female");
        employee.setCompanyId(company.getId());
        employee.setActiveStatus(1);
        employeeRepository.save(employee);

        String requestBody = """
                {
                   "name": "Kato",
                   "age":26,
                   "salary":7000
                }
                """;

        mockMvc.perform(put("/employees/{id}", employee.getId()).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Kato"))
                .andExpect(jsonPath("$.age").value(26))
                .andExpect(jsonPath("$.salary").value(7000));
    }

    @Test
    void should_delete_employee_when_given_a_valid_id() throws Exception {
        String requestBody1 = """
                {
                   "name": "John",
                   "age":20,
                   "gender":"MALE",
                   "salary":5000
                }
                """;
        ;
       // mockMvc.perform(post("/employees1").contentType(MediaType.APPLICATION_JSON).content(requestBody1)).andExpect(status().isCreated());
//        mockMvc.perform(delete("/employees/{id}", 1).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
//        mockMvc.perform(delete("/employees/{id}", 1).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
        long id = createEmployee(requestBody1);
        mockMvc.perform(delete("/employees/{id}", id ).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.age").value(20))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.salary").value(5000));
               // .andExpect(jsonPath("$.activeStatus").value(false));
    }

    @Test
    void should_get_all_employees_when_no_filter_given() throws Exception {
        String requestBody1 = """
                {
                   "name": "John",
                   "age":30,
                   "gender":"MALE",
                   "salary":5000
                }
                """;
        ;
        String requestBody2 = """
                {
                   "name": "May",
                   "age":25,
                   "gender":"FEMALE",
                   "salary":7000
                }
                """;
        ;
        String requestBody3 = """
                {
                   "name": "Tim",
                   "age":29,
                   "gender":"MALE",
                   "salary":8000
                }
                """;
        ;
        mockMvc.perform(post("/employees1").contentType(MediaType.APPLICATION_JSON).content(requestBody1)).andExpect(status().isCreated());
        mockMvc.perform(post("/employees1").contentType(MediaType.APPLICATION_JSON).content(requestBody2)).andExpect(status().isCreated());
        mockMvc.perform(post("/employees1").contentType(MediaType.APPLICATION_JSON).content(requestBody3)).andExpect(status().isCreated());
        mockMvc.perform(get("/employees").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].age").value(30))
                .andExpect(jsonPath("$[0].gender").value("MALE"))
                .andExpect(jsonPath("$[0].salary").value(5000))
                .andExpect(jsonPath("$[1].name").value("May"))
                .andExpect(jsonPath("$[1].age").value(25))
                .andExpect(jsonPath("$[1].gender").value("FEMALE"))
                .andExpect(jsonPath("$[1].salary").value(7000))
                .andExpect(jsonPath("$[2].name").value("Tim"))
                .andExpect(jsonPath("$[2].age").value(29))
                .andExpect(jsonPath("$[2].gender").value("MALE"))
                .andExpect(jsonPath("$[2].salary").value(8000));

    }

    @Test
    void should_get_employees_with_pagination_when_given_page_and_size() throws Exception {
        for (int i = 0; i < 6; i++) {
            String requestBody = """
                    {"name":"Employee%1$d","age":%2$d,"gender":"MALE","salary":%3$d}""".formatted(i + 1, 20 + i, 5000 + i * 1000);
            mockMvc.perform(post("/employees1").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isCreated());
        }
        //返回第二页的内容，即第三第四条
        mockMvc.perform(get("/employees")
                .param("page", "2")
                .param("size", "2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Employee3"))
                .andExpect(jsonPath("$[0].age").value(22))
                .andExpect(jsonPath("$[0].gender").value("MALE"))
                .andExpect(jsonPath("$[0].salary").value(7000))
                .andExpect(jsonPath("$[1].name").value("Employee4"))
                .andExpect(jsonPath("$[1].age").value(23))
                .andExpect(jsonPath("$[1].gender").value("MALE"))
                .andExpect(jsonPath("$[1].salary").value(8000));
    }

    @Test
    void should_get_bad_request_when_create_given_employee_under_18() throws Exception {
        String requestBody1 = """
                {
                   "name": "John",
                   "age":17,
                   "gender":"MALE",
                   "salary":5000
                }
                """;
        mockMvc.perform(post("/employees1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody1)).andExpect(status().isBadRequest());
    }
    @Test
    void should_get_bad_request_when_create_given_employee_upper_65() throws Exception {
        String requestBody1 = """
                {
                   "name": "John",
                   "age":66,
                   "gender":"MALE",
                   "salary":5000
                }
                """;
        mockMvc.perform(post("/employees1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody1)).andExpect(status().isBadRequest());
    }
    @Test
    void should_get_not_found_when_given_a_invalid_id() throws Exception {
        mockMvc.perform(get("/employees/{id}", 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_get_bad_request_when_create_given_employee_over_30_salary_under_20000() throws Exception {
        String requestBody1 = """
                {
                   "name": "John",
                   "age":31,
                   "gender":"MALE",
                   "salary":19999
                }
                """;
        mockMvc.perform(post("/employees1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody1)).andExpect(status().isBadRequest());
    }


    @Test
    void should_get_bad_request_when_create_given_employee_exist() throws Exception {
        String requestBody1 = """
                {
                   "name": "John",
                   "age":31,
                   "gender":"MALE",
                   "salary":19999
                }
                """;
        mockMvc.perform(post("/employees1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody1)).andExpect(status().isBadRequest());
    }

    @Test
  //  void should_get_bad_request_when_delete_employee_already_delete() throws Exception {
    void should_get_not_found_when_delete_employee_already_delete() throws Exception {
        String requestBody = """
                {
                   "name": "John",
                   "age":30,
                   "gender":"MALE",
                   "salary":5000
                }
                """;
//        mockMvc.perform(post("/employees1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestBody1))
//                .andExpect(status().isCreated());
//
        long id = createEmployee(requestBody);
        mockMvc.perform(delete("/employees/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).andExpect(status().isOk());
        mockMvc.perform(delete("/employees/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).andExpect(status().isNotFound());
    }
    @Test
    void should_get_bad_request_when_update_employee_inactive() throws Exception {
        Company company = new Company();
        company.setName("Google");
        companyRepository.save(company);

        Employee employee = new Employee();
        employee.setName("Kate");
        employee.setSalary(6000d);
        employee.setAge(24);
        employee.setGender("Female");
        employee.setCompanyId(company.getId());
        employee.setActiveStatus(0);
        employeeRepository.save(employee);

        String requestBody = """
                {
                   "name": "Kato",
                   "age":26,
                   "salary":7000
                }
                """;

        mockMvc.perform(put("/employees/{id}", employee.getId()).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    private long createEmployee(String requestBody)throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody));

        MvcResult mvcResult = resultActions.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        return new ObjectMapper().readTree(contentAsString).get("id").asLong();
    }

}
