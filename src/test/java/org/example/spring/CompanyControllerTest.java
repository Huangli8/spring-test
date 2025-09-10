package org.example.spring;

import org.example.spring.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CompanyControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    void setUp() throws Exception {
        companyRepository.deleteAll();
    }

    @Test
    void should_create_company_when_given_a_valid_body() throws Exception {
        String requestBody = """
                {
                   "name": "Google"
                }
                """;
        mockMvc.perform(post("/companies").contentType(org.springframework.http.MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Google"));
    }

    @Test
    void should_get_company_when_given_a_valid_id() throws Exception {
        String requestBody = """
                {
                   "name": "Google"
                }
                """;
        mockMvc.perform(post("/companies").contentType(org.springframework.http.MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isCreated());
        mockMvc.perform(get("/companies/{id}", 1).contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Google"));
    }

    @Test
    void should_update_company_when_given_a_valid_id() throws Exception {
        String requestBody = """
                {
                   "name": "Google"
                }
                """;
        String updateBody = """
                {
                   "name": "Alibaba"
                }
                """;
        mockMvc.perform(post("/companies").contentType(org.springframework.http.MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isCreated());
        mockMvc.perform(put("/companies/{id}", 1).contentType(org.springframework.http.MediaType.APPLICATION_JSON).content(updateBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alibaba"));
    }
    @Test
    void should_delete_company_when_given_a_valid_id() throws Exception {
        String requestBody = """
                {
                   "name": "Google"
                }
                """;
        mockMvc.perform(post("/companies").contentType(org.springframework.http.MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isCreated());
        mockMvc.perform(delete("/companies/{id}", 1).contentType(org.springframework.http.MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
        mockMvc.perform(delete("/companies/{id}", 1).contentType(org.springframework.http.MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void should_get_all_companies_when_no_filter_given() throws Exception {
        String requestBody1 = """
                {
                   "name": "Google"
                }
                """;
        String requestBody2 = """
                {
                   "name": "Alibaba"
                }
                """;
        String requestBody3 = """
                {
                   "name": "Tencent"
                }
                """;
        mockMvc.perform(post("/companies").contentType(org.springframework.http.MediaType.APPLICATION_JSON).content(requestBody1)).andExpect(status().isCreated());
        mockMvc.perform(post("/companies").contentType(org.springframework.http.MediaType.APPLICATION_JSON).content(requestBody2)).andExpect(status().isCreated());
        mockMvc.perform(post("/companies").contentType(org.springframework.http.MediaType.APPLICATION_JSON).content(requestBody3)).andExpect(status().isCreated());
        mockMvc.perform(get("/companies").contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].name").value("Google"))
                .andExpect(jsonPath("$[1].name").value("Alibaba"))
                .andExpect(jsonPath("$[2].name").value("Tencent"));
    }

    @Test
    void should_get_companies_with_pagination_when_given_page_and_size() throws Exception {
        for (int i = 0; i < 6; i++) {
            String requestBody = """
                    {"name":"Company%1$d"}""".formatted(i + 1);
            mockMvc.perform(post("/companies").contentType(org.springframework.http.MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isCreated());
        }
        //返回第二页的内容，即第三第四条
        mockMvc.perform(get("/companies")
                .param("page", "2")
                .param("size", "2").contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Company3"))
                .andExpect(jsonPath("$[1].name").value("Company4"));
    }

}
