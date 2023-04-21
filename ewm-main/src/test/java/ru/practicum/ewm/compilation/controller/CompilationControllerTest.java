package ru.practicum.ewm.compilation.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.compilation.CompilationAdminController;
import ru.practicum.ewm.compilation.service.CompilationService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompilationAdminController.class)
public class CompilationControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CompilationService compilationService;

    @SneakyThrows
    @Test
    void shouldReturn400StatusWhenBodyIsEmpty() {
        mvc.perform(post("/admin/compilations")).andExpect(status().isBadRequest());
    }
}
