package ru.practicum.ewm.user.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.user.model.User;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UserRepositoryTest {
    @Autowired
    protected TestEntityManager entityManager;
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldBeFailedWhenDuplicatedMail() {
        User user = User.builder().name("test").email("test@mail.com").build();
        User doublingUser = User.builder().name("double").email("test@mail.com").build();

        userRepository.save(user);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(doublingUser));
    }
}
