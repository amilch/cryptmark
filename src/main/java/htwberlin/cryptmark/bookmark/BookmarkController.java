package htwberlin.cryptmark.bookmark;

import htwberlin.cryptmark.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkRepository repository;

    @GetMapping("/bookmarks/{id}")
    public Bookmark one(@PathVariable Long id){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var principal = authentication.getPrincipal();
        var user = (User) principal;
        return repository.findByUserAndId(user, id)
                .orElseThrow(() -> new BookmarkNotFoundException(id));
    }

    @GetMapping("/bookmarks")
    public List<Bookmark> getAll() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var principal = authentication.getPrincipal();

        return repository.findByUser((User) principal);
    }

    @PostMapping("/bookmarks")
    @Transactional
    public Bookmark replaceOrCreateBookmark(@RequestBody Bookmark newBookmark) {
        System.out.println(newBookmark);
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var principal = authentication.getPrincipal();

        return repository.findByUserAndId((User) principal, newBookmark.getId())
                .map(bookmark -> {
                    bookmark.setEncryptedItemKey(newBookmark.getEncryptedItemKey());
                    bookmark.setEncryptedItem(newBookmark.getEncryptedItem());
                    return repository.save(bookmark);
                })
                .orElseGet(() -> {
                    newBookmark.setUser((User) principal);
                    System.out.println(newBookmark);
                    return repository.save(newBookmark);
                });
    }

    @DeleteMapping("/bookmarks/{id}")
    @Transactional
    public void deleteBookmark(@PathVariable Long id) {
        System.out.println("Deleting with id " + id.toString());
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var principal = authentication.getPrincipal();

        repository.deleteByUserAndId((User) principal, id);
    }
}
