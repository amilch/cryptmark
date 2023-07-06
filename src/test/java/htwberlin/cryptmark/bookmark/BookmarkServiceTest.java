package htwberlin.cryptmark.bookmark;

import htwberlin.cryptmark.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {
    @Mock
    private BookmarkRepository repo;

    private BookmarkService subject;

    @BeforeEach
    void setUp() {
        subject = new BookmarkService(repo);
    }

    @Test
    void changingAnBookmarkDoesNotUpdateIndexOrUser() {
        Bookmark oldBookmark = mock(Bookmark.class);
        when(repo.findById(1L)).thenReturn(Optional.of(oldBookmark));

        subject.updateBookmark(Bookmark.builder()
                .user(mock(User.class))
                .id(1L)
                .encryptedItem("e")
                .encryptedItemKey("k")
                .build());

        verify(oldBookmark, never()).setId(any());
        verify(oldBookmark, never()).setUser(any());
        verify(oldBookmark).setEncryptedItem("e");
        verify(oldBookmark).setEncryptedItemKey("k");
        var arg = ArgumentCaptor.forClass(Bookmark.class);
        verify(repo).save(arg.capture());
        assertThat(arg.getValue()).isEqualTo(oldBookmark);
    }
}