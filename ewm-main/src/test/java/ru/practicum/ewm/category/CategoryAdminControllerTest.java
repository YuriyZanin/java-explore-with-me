package ru.practicum.ewm.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.exception.NotFoundException;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryAdminController.class)
public class CategoryAdminControllerTest {
    private final CategoryDto categoryDto = CategoryDto.builder().id(1L).name("test").build();
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private CategoryService categoryService;

    @SneakyThrows
    @Test
    void shouldReturnStatus200WhenSaveNewCategory() {
        Mockito.when(categoryService.create(Mockito.any())).thenReturn(categoryDto);

        mvc.perform(post("/admin/categories")
                        .content(mapper.writeValueAsString(categoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(categoryDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(categoryDto.getName())));
    }

    @SneakyThrows
    @Test
    void shouldReturnStatus400WhenNotValidBody() {
        CategoryDto notValid = CategoryDto.builder().name(null).build();

        mvc.perform(post("/admin/categories")
                        .content(mapper.writeValueAsString(notValid))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void shouldReturnStatus409WhenAlreadyExist() {
        Mockito.when(categoryService.create(Mockito.any())).thenThrow(DataIntegrityViolationException.class);

        mvc.perform(post("/admin/categories")
                        .content(mapper.writeValueAsString(categoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @SneakyThrows
    @Test
    void shouldReturnStatus204WhenDeleteCategory() {
        mvc.perform(delete("/admin/categories/1")).andExpect(status().isNoContent());
    }

    @SneakyThrows
    @Test
    void shouldReturnStatus404WhenDeletingNotExist() {
        Mockito.doThrow(NotFoundException.class).when(categoryService).delete(Mockito.anyLong());

        mvc.perform(delete("/admin/categories/1")).andExpect(status().isNotFound());
    }
}
