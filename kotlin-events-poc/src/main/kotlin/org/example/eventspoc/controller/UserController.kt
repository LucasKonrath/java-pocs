package org.example.eventspoc.controller

import org.example.eventspoc.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

data class CreateUserRequest(val username: String, val email: String)

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody request: CreateUserRequest) {
        userService.createUser(request.username, request.email)
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable username: String) {
        userService.deleteUser(username)
    }
}
