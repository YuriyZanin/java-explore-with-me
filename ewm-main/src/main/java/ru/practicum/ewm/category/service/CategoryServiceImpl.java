package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.Collection;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public CategoryDto create(CategoryDto categoryDetails) {
        Category category = CategoryMapper.MAPPER.toCategory(categoryDetails);
        return CategoryMapper.MAPPER.toDto(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public CategoryDto update(Long categoryId, CategoryDto categoryDetails) {
        Category category = getCategory(categoryId);
        category.setName(categoryDetails.getName());
        return CategoryMapper.MAPPER.toDto(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public void delete(Long categoryId) {
        Category category = getCategory(categoryId);
        categoryRepository.delete(category);
    }

    @Override
    public Collection<CategoryDto> getAll(Integer from, Integer size) {
        PageRequest page = PageRequest.of(from / size, size);
        return CategoryMapper.MAPPER.toDtos(categoryRepository.findAll(page).getContent());
    }

    @Override
    public CategoryDto get(Long categoryId) {
        return CategoryMapper.MAPPER.toDto(getCategory(categoryId));
    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
    }
}

