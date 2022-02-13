# Introduction
This project will involve creating a simple REST application for serving a blog. It uses *Spring Boot*, *Spring Data JPA* and some other helper libraries.
It is divided into two packages:

- `com.acme.blog.domain` - business model
- `com.acme.blog.boundary` - REST endpoints to interact with application

Currently the application can only serve blog post details by executing a `GET` request at `/post/{id}`, where `{id}` is a post identifier.

# Problem statement

Your task is to add a commenting feature. The application should be able to serve two new kinds of requests:
- `POST` at `/post/{id}/comment` - which should create a new comment for a post with passed `{id}`
- `GET` at `/post/{id}/comments` - which should return all comments for a post with the `{id}` that was passed
The endpoints should return `404` if `{id}` of given post does not exist.

There is an incomplete service `CommentService` that you should implement. View the contents of that service to see detailed
requirements. For each method there is a javadoc comment that describes the expected behavior.
There are some tests that are currently failing and your solution should fulfill these test requirements.

You are free to create new files and modify existing ones. Please follow the conventions used in this project.