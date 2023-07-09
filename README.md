# Cryptmark

This app allows users to store, read, and search bookmarks end-to-end encrypted in the cloud.

Bookmarks are encrypted with `bookmarksKey`. From the users password the `rootKey` is derived using Argon2. 
The first half of `rootKey` is used to encrypt `bookmarksKey`. That way if the user changes their password only `bookmarksKey` has to be reencrypted with the new `rootKey`.

You can find the frontend at [cryptmark-frontend](https://github.com/amilch/cryptmark-frontend)

## Live Demo

You can find a running demo version at <https://amilch.uber.space>

**It's only a development preview!**\
Please don't use it to save anything of relevance. The server will be shut down at any time and the security of the code ist not tested.

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
