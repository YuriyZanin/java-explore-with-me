package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> create(@Valid @RequestBody CategoryDto categoryDetails) {
        log.info("Запрос на создание категрии {}", categoryDetails);
        return new ResponseEntity<>(categoryService.create(categoryDetails), HttpStatus.CREATED);
    }

    @PatchMapping("/{catId}")
    public CategoryDto update(@PathVariable Long catId, @Valid @RequestBody CategoryDto categoryDetails) {
        log.info("Запрос на изменение категории с id={} на {}", catId, categoryDetails);
        return categoryService.update(catId, categoryDetails);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<?> delete(@PathVariable Long catId) {
        log.info("Запрос на удаление категории с id={}", catId);
        categoryService.delete(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
