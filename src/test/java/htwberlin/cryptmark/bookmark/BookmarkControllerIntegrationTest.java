package htwberlin.cryptmark.bookmark;

import htwberlin.cryptmark.user.Role;
import htwberlin.cryptmark.user.User;
import htwberlin.cryptmark.user.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class BookmarkControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private JacksonTester<Bookmark> jsonBookmark;

    @Autowired
    private JacksonTester<List<Bookmark>> jsonBookmarkList;

    static private User admina, user1, user2;
    private Bookmark user1Bookmark1, user1Bookmark2, user2Bookmark1;

    @BeforeAll
    static void setUpUsers(@Autowired UserRepository userRepository) {
        userRepository.deleteAll();
        admina = userRepository.save(User.builder().username("admina").role(Role.ADMIN).build());
        user1 = userRepository.save(User.builder().username("user1").role(Role.USER).build());
        user2 = userRepository.save(User.builder().username("user2").role(Role.USER).build());
    }

    @BeforeEach
    void setUp() {
        bookmarkRepository.deleteAll();
        var bookmarkBuilder = Bookmark.builder()
                .encryptedItem("encryptedItem").encryptedItemKey("encryptedItemKey");
        this.user2Bookmark1 = bookmarkRepository.save(bookmarkBuilder.user(user2).build());
        this.user1Bookmark1 = bookmarkRepository.save(bookmarkBuilder.user(user1).build());
        this.user1Bookmark2 = bookmarkRepository.save(bookmarkBuilder.user(user1).build());
    }

    @WithUserDetails("user1")
    @Test
    void canGetAllBookmarkOfUser() throws Exception {
        var res = mvc.perform(
                get("/bookmarks/").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertThat(res.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(res.getContentAsString()).isEqualTo(
                jsonBookmarkList.write(List.of(user1Bookmark1, user1Bookmark2)).getJson());
    }

    @WithUserDetails("user1")
    @Test
    void canGetSingleBookmarkOfUser() throws Exception {
        var res = mvc.perform(
                get("/bookmarks/"+user1Bookmark1.getId()).accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(res.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(res.getContentAsString()).isEqualTo(
                jsonBookmark.write(user1Bookmark1).getJson());
    }

    @WithUserDetails("user1")
    @Test
    void canNotGetBookmarkOfOtherUser() throws Exception {
        var res = mvc.perform(
                get("/bookmarks/"+user2Bookmark1.getId()).accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(res.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(res.getContentAsString()).isNotEqualTo(
                jsonBookmark.write(user2Bookmark1).getJson());
    }

    @WithUserDetails("user1")
    @Test
    void canDeleteOwnBookmark() throws Exception {
        var res = mvc.perform(
                delete("/bookmarks/"+user1Bookmark1.getId())).andReturn().getResponse();

        assertThat(res.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(bookmarkRepository.findById(user1Bookmark1.getId())).isNotPresent();
    }

    @WithUserDetails("user1")
    @Test
    void canNotDeleteBookmarkOfOtherUser() throws Exception {
        var res = mvc.perform(
                delete("/bookmarks/"+user2Bookmark1.getId())).andReturn().getResponse();

        assertThat(res.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(bookmarkRepository.findById(user2Bookmark1.getId())).isPresent();
    }

    @WithUserDetails("user1")
    @Test
    void canChangeSingleOwnedBookmark() throws Exception {
        Bookmark newBookmark = Bookmark.builder()
                .id(user1Bookmark1.getId())
                .encryptedItem("new").encryptedItemKey("new")
                .build();
        var res = mvc.perform(
                put("/bookmarks/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBookmark.write(newBookmark).getJson())
        ).andReturn().getResponse();

        assertThat(res.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(bookmarkRepository.findById(user1Bookmark1.getId()).get().getEncryptedItem())
                .isEqualTo("new");
    }

    @WithUserDetails("user1")
    @Test
    void canNotChangeOwnershipOfBookmark() throws Exception {
        Bookmark newBookmark = Bookmark.builder()
                .id(user1Bookmark1.getId())
                .user(user2)
                .encryptedItem("new").encryptedItemKey("new")
                .build();
        var res = mvc.perform(
                put("/bookmarks/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBookmark.write(newBookmark).getJson())
        ).andReturn().getResponse();

        // User field in json is simply ignored, therefore result is OK
        assertThat(res.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(bookmarkRepository.findById(user1Bookmark1.getId()).get().getUser()).isEqualTo(user1);
    }

    @WithUserDetails("user1")
    @Test
    void canNotChangeBookmarkOfOtherUser() throws Exception {
        Bookmark newBookmark = Bookmark.builder()
                .id(user2Bookmark1.getId())
                .encryptedItem("new").encryptedItemKey("new")
                .build();
        var res = mvc.perform(
                put("/bookmarks/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBookmark.write(newBookmark).getJson())
        ).andReturn().getResponse();

        assertThat(res.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(bookmarkRepository.findById(user2Bookmark1.getId()).get().getEncryptedItem()).isNotEqualTo("new");
    }

    @WithUserDetails("user1")
    @Test
    void canCreateNewBookmark() throws Exception {
        assertThat(bookmarkRepository.findByUser(user1).size()).isEqualTo(2);

        Bookmark newBookmark = Bookmark.builder()
                .encryptedItem("new").encryptedItemKey("new")
                .build();
        var res = mvc.perform(
                post("/bookmarks/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBookmark.write(newBookmark).getJson())
        ).andReturn().getResponse();

        assertThat(res.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(bookmarkRepository.findByUser(user1).size()).isEqualTo(3);
    }
}