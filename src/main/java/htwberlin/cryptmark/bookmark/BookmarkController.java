package htwberlin.cryptmark.bookmark;

import htwberlin.cryptmark.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkRepository repository;

    @GetMapping("/bookmarks/{id}")
    Bookmark one(@PathVariable Long id){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var principal = authentication.getPrincipal();
        var user = (User) principal;
        return repository.findByUserAndId(user, id)
                .orElseThrow(() -> new BookmarkNotFoundException(id));
    }

    @PostMapping("/bookmarks")
    Bookmark replaceOrCreateBookmark(@RequestBody Bookmark newBookmark) {
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
    void deleteBookmark(@PathVariable Long id, Principal principal) {
        repository.deleteByUserAndId((User) principal, id);
    }
}
