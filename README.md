#### Running locally
```
./gradlew run
```

#### Running unit test
```
./gradlew test
```

## API docs

* `GET /users/{userId}/posts`
    
    Can be used to retrieve posts created by user with specified id (i.e. the Wall).
    
    Response codes:
    * `200` - when posts are retrieved successfully
    * `500` - in case of unexpected errors
    
    Exemplary curl:  
    ```
    curl http://localhost:8080/users/123/posts
    ```
    
* `POST /users/{userId}/posts`

    Creates the new message (post). Accepts the json body with a single
    field containing the actual message text:
    
    ```
    {
        "message": "some test message"
    }
    ```
  
    Response codes:
    * `201` - when post was created successfully
    * `400` - in case of invalid input (e.g. message longer than 140 characters)
    * `500` - in case of unexpected errors
    
    Exemplary curl:  
    ```
    curl -X POST http://localhost:8080/users/123/posts \
         -d '{"message": "some test message"}'
    ```

* `PUT /users/{userId}/followees/{followeeId}`
    
    Creates a follower-followee relationship between users.

    Response codes:
    * `200` - when post was created successfully
    * `500` - in case of unexpected errors
    
    Exemplary curl:  
    ```
    curl -X PUT http://localhost:8080/users/124/followees/123
    ```
  
* `GET /users/{userId}/followees/posts`
    
    Can be used to retrieve posts created by all users followed by user with 
    specified id (i.e. the Timeline).

    Response codes:
    * `200` - when posts are retrieved successfully
    * `500` - in case of unexpected errors
    
    Exemplary curl:  
    ```
    curl http://localhost:8080/users/124/followees/posts
    ```
