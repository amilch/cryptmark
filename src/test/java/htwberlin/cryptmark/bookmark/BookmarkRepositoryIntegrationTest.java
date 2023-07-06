package htwberlin.cryptmark.bookmark;

import htwberlin.cryptmark.user.Role;
import htwberlin.cryptmark.user.User;
import htwberlin.cryptmark.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookmarkRepositoryIntegrationTest {
    @Autowired
    private BookmarkRepository subject;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldReturnOnlyBookmarksOfUser() throws Exception {
        User user1 = userRepository.save(User.builder()
                .username("user1").role(Role.USER).build());
        User user2 = userRepository.save(User.builder()
                .username("user2").role(Role.USER).build());
        var bookmarkBuilder = Bookmark.builder()
                .encryptedItem("encryptedItem").encryptedItemKey("encryptedItemKey");
        Bookmark user1Bookmark1 = subject.save(bookmarkBuilder
                .user(user1).build());
        Bookmark user1Bookmark2 = subject.save(bookmarkBuilder
                .user(user1).build());
        Bookmark user2Bookmark1 = subject.save(bookmarkBuilder
                .user(user2).build());

        List<Bookmark> res = subject.findByUser(user1);

        assertThat(res).containsExactlyInAnyOrder(user1Bookmark1, user1Bookmark2);
    }
}