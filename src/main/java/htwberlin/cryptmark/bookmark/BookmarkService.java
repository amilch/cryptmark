package htwberlin.cryptmark.bookmark;

import htwberlin.cryptmark.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository repo;

    public Bookmark createBookmark(Bookmark bookmark) {
        return repo.save(bookmark);
    }

    public Optional<Bookmark> updateBookmark(Bookmark newBookmark) {
        return repo.findById(newBookmark.getId())
                .map(bookmark -> {
                    bookmark.setEncryptedItemKey(newBookmark.getEncryptedItemKey());
                    bookmark.setEncryptedItem(newBookmark.getEncryptedItem());
                    return repo.save(bookmark);
                });
    }

    public Optional<Bookmark> getBookmark(Long id) {
        return repo.findById(id);
    }

    public List<Bookmark> getAll() {
        return repo.findAll();
    }

    public List<Bookmark> getAll(User user) {
       return repo.findByUser(user);
    }

    public void deleteBookmark(Long id) {
        repo.deleteById(id);
    }

    public void deleteBookmark(Bookmark bookmark) {
        repo.delete(bookmark);
    }
}
