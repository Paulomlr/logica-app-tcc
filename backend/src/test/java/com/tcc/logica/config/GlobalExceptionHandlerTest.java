package com.tcc.logica.config;

import com.tcc.logica.exercise.ExerciseAttemptService;
import com.tcc.logica.exercise.ExerciseController;
import com.tcc.logica.exercise.ExerciseGenerationService;
import com.tcc.logica.exercise.LogicExerciseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Confirms ResponseStatusException gets a clean {status, message} JSON body with
 * no leaked stack trace field. Note: this cannot reproduce the 403-masking bug
 * this handler was written to avoid (Tomcat's /error redispatch never happens
 * inside MockMvc) — that was only observable against the real running server.
 */
@WebMvcTest(ExerciseController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LogicExerciseRepository exerciseRepository;

    @MockitoBean
    private ExerciseAttemptService attemptService;

    @MockitoBean
    private ExerciseGenerationService generationService;

    @Test
    void responseStatusExceptionBodyHasNoStackTrace() throws Exception {
        when(attemptService.getPlayView(anyLong()))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercício não encontrado: 99999"));

        mockMvc.perform(get("/api/exercises/99999/play"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Exercício não encontrado: 99999"))
                .andExpect(jsonPath("$.trace").doesNotExist());
    }

    @Test
    void beanValidationFailureUsesTheSameApiErrorShape() throws Exception {
        mockMvc.perform(post("/api/exercises/1/attempts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"answers\": [], \"timeSpentSeconds\": 5}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.trace").doesNotExist())
                .andExpect(jsonPath("$.errors").doesNotExist());
    }

    @Test
    void malformedJsonBodyReturns400NotInternalServerError() throws Exception {
        mockMvc.perform(post("/api/exercises/1/attempts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ this is not valid json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists());
    }
}
