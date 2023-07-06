package htwberlin.cryptmark.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JacksonTester<User> jsonUser;

    static private User admina, user1, user2;

    @BeforeAll
    static void setUpUsers(@Autowired UserRepository userRepository) {
        userRepository.deleteAll();
        admina = userRepository.save(User.builder().username("admina").seed("seed").role(Role.ADMIN).build());
        user1 = userRepository.save(User.builder().username("user1").seed("seed").role(Role.USER).build());
        user2 = userRepository.save(User.builder().username("user2").seed("seed").role(Role.USER).build());
    }

    @Test
    void canGetUserSeedWithoutAuthentication() throws Exception {
        var res = mvc.perform(
                get("/users/user2/seed")
                        .accept(MediaType.APPLICATION_JSON)
                ).andReturn().getResponse();

        assertThat(res.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(res.getContentAsString()).isEqualTo(
                userRepository.findByUsername("user2").get().getSeed());
    }

    @Test
    void tryingToGetSeedForNonExistingUserThrowsNotFound() throws Exception {
        var res = mvc.perform(
                get("/users/user9/seed")
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertThat(res.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @WithUserDetails("user1")
    @Test
    void canGetInfoOfAuthenticatedUser() throws Exception {
        var res = mvc.perform(
                get("/users/user1")
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        var user = jsonUser.parse(res.getContentAsString()).getObject();

        assertThat(res.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(user.getUsername()).isEqualTo("user1");
    }

    @WithUserDetails("user1")
    @Test
    void canNotGetInfoOfOtherUser() throws Exception {
        var res = mvc.perform(
                get("/users/user2")
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertThat(res.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
}