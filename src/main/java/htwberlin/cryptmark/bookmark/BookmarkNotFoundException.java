package htwberlin.cryptmark.bookmark;

public class BookmarkNotFoundException extends RuntimeException {
    public BookmarkNotFoundException(Long id) {
        super("Could not find bookmark " + id);
    }
}
