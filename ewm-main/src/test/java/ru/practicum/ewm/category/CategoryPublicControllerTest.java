package ru.practicum.ewm.category;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.exception.NotFoundException;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryPublicController.class)
public class CategoryPublicControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CategoryService categoryService;

    @SneakyThrows
    @Test
    void shouldReturnStatus200WhenFindCategoryById() {
        CategoryDto categoryDto = CategoryDto.builder().id(1L).name("test").build();

        Mockito.when(categoryService.get(Mockito.anyLong())).thenReturn(categoryDto);

        mvc.perform(get("/categories/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(categoryDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(categoryDto.getName())));
    }

    @SneakyThrows
    @Test
    void shouldReturnStatus404WhenNotFound() {
        Mockito.when(categoryService.get(Mockito.anyLong())).thenThrow(NotFoundException.class);

        mvc.perform(get("/categories/999").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
