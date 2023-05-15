package htwberlin.cryptmark.bookmark;

import htwberlin.cryptmark.user.User;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var principal = authentication.getPrincipal();

        return repository.findByUserAndId((User) principal, newBookmark.getId())
                .map(bookmark -> {
                    bookmark.setEncryptedKey(newBookmark.getEncryptedKey());
                    bookmark.setEncryptedContent(newBookmark.getEncryptedContent());
                    return repository.save(bookmark);
                })
                .orElseGet(() -> {
                    newBookmark.setUser((User) principal);
                    return repository.save(newBookmark);
                });
    }

    @DeleteMapping("/bookmarks/{id}")
    void deleteBookmark(@PathVariable Long id, Principal principal) {
        repository.deleteByUserAndId((User) principal, id);
    }
}
