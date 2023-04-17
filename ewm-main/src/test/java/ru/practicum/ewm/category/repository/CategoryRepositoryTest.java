package ru.practicum.ewm.category.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.model.Category;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class CategoryRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldBeFailedWhenDuplicateName() {
        Category category = new Category();
        category.setName("name");
        Category notUnique = new Category();
        notUnique.setName("name");

        categoryRepository.save(category);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(notUnique));
    }
}
