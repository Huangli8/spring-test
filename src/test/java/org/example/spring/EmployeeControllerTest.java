package org.example.spring;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_create_employee_when_given_a_valid_body() throws Exception{
        String requestBody = """
        {
           "name": "John",
           "age":30,
           "gender":"MALE",
           "salary":5000
        }
        """
              ;
        mockMvc.perform(post("/employees1").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.salary").value(5000));

    }

    @Test
    void should_get_employee_when_given_a_valid_id() throws Exception{
        String requestBody = """
        {
           "name": "John",
           "age":30,
           "gender":"MALE",
           "salary":5000
        }
        """
                ;
        mockMvc.perform(post("/employees1").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isCreated());
        mockMvc.perform(get("/employees/{id}",1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.salary").value(5000));
    }

    @Test
    void should_get_employees_when_given_gender() throws Exception{
        String requestBody1 = """
        {
           "name": "John",
           "age":30,
           "gender":"MALE",
           "salary":5000
        }
        """
                ;
        String requestBody2 = """
        {
           "name": "Amy",
           "age":30,
           "gender":"FEMALE",
           "salary":8000
        }
        """
                ;
        String requestBody3 = """
        {
           "name": "Tim",
           "age":20,
           "gender":"MALE",
           "salary":4000
        }
        """
                ;
        mockMvc.perform(post("/employees1").contentType(MediaType.APPLICATION_JSON).content(requestBody1)).andExpect(status().isCreated());
        mockMvc.perform(post("/employees1").contentType(MediaType.APPLICATION_JSON).content(requestBody2)).andExpect(status().isCreated());
        mockMvc.perform(post("/employees1").contentType(MediaType.APPLICATION_JSON).content(requestBody3)).andExpect(status().isCreated());
        mockMvc.perform(get("/employees").param("gender","MALE").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].age").value(30))
                .andExpect(jsonPath("$[0].gender").value("MALE"))
                .andExpect(jsonPath("$[0].salary").value(5000))
                .andExpect(jsonPath("$[1].name").value("Tim"))
                .andExpect(jsonPath("$[1].age").value(20))
                .andExpect(jsonPath("$[1].gender").value("MALE"))
                .andExpect(jsonPath("$[1].salary").value(4000));
    }

}
