package htwberlin.cryptmark.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryIntegrationTest {
    @Autowired
    UserRepository subject;

    @Test
    void canFindUserByUsername() {
        User user = subject.save(User.builder()
                .username("user").role(Role.USER).serverPassword("pass").build());

        Optional<User> res = subject.findByUsername("user");

        assertThat(res).isPresent().hasValue(user);
    }
}