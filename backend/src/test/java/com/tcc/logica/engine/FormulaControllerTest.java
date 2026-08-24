package com.tcc.logica.engine;

import com.tcc.logica.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FormulaController.class)
@Import(SecurityConfig.class)
class FormulaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsTruthTableForValidFormula() throws Exception {
        mockMvc.perform(post("/api/formulas/table")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"formula\": \"p & q\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.variables").isArray())
                .andExpect(jsonPath("$.variables[0]").value("p"))
                .andExpect(jsonPath("$.variables[1]").value("q"))
                .andExpect(jsonPath("$.rows.length()").value(4));
    }

    @Test
    void returns400ForInvalidFormula() throws Exception {
        mockMvc.perform(post("/api/formulas/table")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"formula\": \"p & \"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void returns400ForBlankFormula() throws Exception {
        mockMvc.perform(post("/api/formulas/table")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"formula\": \"\"}"))
                .andExpect(status().isBadRequest());
    }
}
