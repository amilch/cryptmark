console.log("Register user1")
let resp = await fetch("http://localhost:8080/auth/register",
      {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username: "user1", password: "1234" })
      },
);
if (resp.status != 200) {
    console.log("Registration error, maybe user is already registered")
    console.log("Trying to authenticate with existing user")
    resp = await fetch("http://localhost:8080/auth/authenticate",
	{
	    method: "POST",
	    headers: { "Content-Type": "application/json" },
	    body: JSON.stringify({ username: "user1", password: "1234" })
	},
    );
}
const token = JSON.parse(await resp.text()).token
console.log("Got JWT Token: " + token)

console.log("Get user info")
resp = await fetch("http://localhost:8080/users/user1",
      {
        method: "GET",
        headers: {
		 "Content-Type": "application/json",
		 "Authorization": "Bearer " + token
	}
      }
);
console.log(await resp.text())

console.log("Get user bookmarks")
resp = await fetch("http://localhost:8080/users/user1/bookmarks",
      {
        method: "GET",
        headers: {
		 "Content-Type": "application/json",
		 "Authorization": "Bearer " + token
	}
      }
);
console.log(await resp.text())

console.log("Create new bookmark")
resp = await fetch("http://localhost:8080/bookmarks",
      {
        method: "POST",
        headers: {
		 "Content-Type": "application/json",
		 "Authorization": "Bearer " + token
	},
	body: JSON.stringify({
		"encryptedKey": "key",
		"encryptedContent": "content"
	})
      }
);
console.log(resp.status)

console.log("Get user bookmarks")
resp = await fetch("http://localhost:8080/users/user1/bookmarks",
      {
        method: "GET",
        headers: {
		 "Content-Type": "application/json",
		 "Authorization": "Bearer " + token
	}
      }
);
console.log(resp.status + "\n" + await resp.text())

console.log("Get bookmark with id=1")
resp = await fetch("http://localhost:8080/bookmarks/1",
      {
        method: "GET",
        headers: {
		 "Content-Type": "application/json",
		 "Authorization": "Bearer " + token
	}
      }
);
console.log(resp.status + "\n" + await resp.text())