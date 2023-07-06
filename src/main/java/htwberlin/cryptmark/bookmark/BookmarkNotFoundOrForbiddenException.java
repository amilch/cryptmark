package htwberlin.cryptmark.bookmark;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BookmarkNotFoundOrForbiddenException extends RuntimeException {
    public BookmarkNotFoundOrForbiddenException() {
        super("Could not find bookmark or operation forbidden");
    }
}
