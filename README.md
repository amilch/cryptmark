# Cryptmark

This app allows users to store, read, and search bookmarks end-to-end encrypted in the cloud.

- UI similar to the old google search, text-based with good readability
- Bookmarks are encrypted with `bookmarksKey`. From the users password the `rootKey` is derived using Argon2. The first half of `rootKey` is used to encrypt `bookmarksKey`. That way if the user changes their password only `bookmarksKey` has to be reencrypted with the new `rootKey`.

## Running

Set environment variable SECRET_JWT_KEY. 

You can generate the JWT Key with the following python code:

```python
import base64
import secrets
base64.b64encode(secrets.token_bytes(32))
```

## Inspiration

### Crypto

- https://standardnotes.com/help/security/encryption
- https://cheatsheetseries.owasp.org/cheatsheets/Cryptographic_Storage_Cheat_Sheet.html
- https://github.com/standardnotes/app/blob/main/packages/snjs/specification.md
- https://libsodium.gitbook.io/doc/
- https://developer.mozilla.org/en-US/docs/Web/API/Crypto
- https://www.crypto101.io/

### Other projects

Closed source:

- https://pinboard.in/

Open source:

- https://demo.linkace.org/guest/links
