package htwberlin.cryptmark.bookmark;

import htwberlin.cryptmark.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {
    private final BookmarkService service;

    @GetMapping("/{id}")
    public Bookmark getBookmark(@PathVariable Long id, Authentication auth){
        Bookmark bookmark = service.getBookmark(id)
                .orElseThrow(BookmarkNotFoundOrForbiddenException::new);

        if (!bookmark.getUser().equals(auth.getPrincipal())) {
            throw new BookmarkNotFoundOrForbiddenException();
        }

        return bookmark;
    }

    @GetMapping("/")
    public List<Bookmark> getAll() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return service.getAll(user);
    }

    @PostMapping("/")
    @Transactional
    public Bookmark createBookmark(@RequestBody Bookmark newBookmark) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        newBookmark.setUser(user);
        return service.createBookmark(newBookmark);
    }

    @PutMapping("/")
    @Transactional
    public Optional<Bookmark> updateBookmark(@RequestBody Bookmark newBookmark, Authentication auth) {
        Bookmark bookmark = service.getBookmark(newBookmark.getId())
                .orElseThrow(BookmarkNotFoundOrForbiddenException::new);

        if (!bookmark.getUser().equals(auth.getPrincipal())) {
            throw new BookmarkNotFoundOrForbiddenException();
        }
        return service.updateBookmark(newBookmark);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public void deleteBookmark(@PathVariable Long id, Authentication auth) {
        Bookmark bookmark = service.getBookmark(id)
                .orElseThrow(BookmarkNotFoundOrForbiddenException::new);

        if (!bookmark.getUser().equals(auth.getPrincipal())) {
            throw new BookmarkNotFoundOrForbiddenException();
        }

        service.deleteBookmark(bookmark);
    }
}
