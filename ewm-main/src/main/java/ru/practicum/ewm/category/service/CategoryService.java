package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;

import java.util.Collection;

public interface CategoryService {
    CategoryDto create(CategoryDto categoryDetails);

    CategoryDto update(Long categoryId, CategoryDto categoryDetails);

    void delete(Long categoryId);

    Collection<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto get(Long categoryId);
}
