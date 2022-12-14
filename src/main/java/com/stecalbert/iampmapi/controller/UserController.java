package com.stecalbert.iampmapi.controller;

import com.stecalbert.iampmapi.model.User;
import com.stecalbert.iampmapi.service.UserService;
import com.stecalbert.iampmapi.utils.AllUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    @CrossOrigin
    public ResponseEntity getAll(@RequestHeader("access-token") String accessToken,
                                 @RequestParam(required = false, name = "email") String email,
                                 @RequestParam(required = false, name = "name") String name) {
        if (accessToken == null) {
            throw new UnauthorizedException();
        }

        if (!accessToken.equals(AllUtils.token)) {
            throw new ForbiddenException();
        }

        if (email != null) {
            List<User> users = userService.findAll().stream().filter(e -> e.getEmail().equals(email)).collect(Collectors.toList());
            return ResponseEntity.ok(users);
        }

        if (name != null) {
            List<User> users = userService.findAll().stream().filter(e -> e.getFirstName().equals(name)).collect(Collectors.toList());
            return ResponseEntity.ok(users);
        }

        List<User> users = userService.findAll();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @CrossOrigin
    public ResponseEntity findById(@RequestHeader("access-token") String accessToken, @PathVariable String id) {
        if (accessToken == null) {
            throw new UnauthorizedException();
        }

        if (!accessToken.equals(AllUtils.token)) {
            throw new ForbiddenException();
        }

        Long idL = Long.valueOf(id);
        User user = userService.findAll().stream().filter(e -> e.getId().equals(idL)).findFirst().orElseThrow(UserNotFoundException::new);

        return ResponseEntity.ok(user);
    }

    @PostMapping
    @CrossOrigin
    public ResponseEntity create(@RequestHeader("access-token") String accessToken, @RequestBody User user) {
        if (!accessToken.equals(AllUtils.token)) {
            throw new UnauthorizedException();
        }

        Long idL = Long.valueOf(user.getId());
        List<User> existed = userService.findAll().stream()
                .filter(e -> e.getId().equals(idL))
                .collect(Collectors.toList());

        if (!existed.isEmpty() && existed.get(0) != null) {
            throw new UserExistsException();
        }

        userService.create(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    @CrossOrigin
    @ApiOperation(value = "Delete all", notes = "Delete all - ADMIN only")
    public void deleteAll(@RequestHeader("access-token") String accessToken) {
        if (accessToken == null) {
            throw new UnauthorizedException();
        }

        if (!accessToken.equals("123123123")) {
            throw new ForbiddenException();
        }

        userService.deleteAll();

        ResponseEntity.noContent();
    }


    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public class UnauthorizedException extends RuntimeException {
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public class UserNotFoundException extends RuntimeException {
    }

    @ResponseStatus(code = HttpStatus.CONFLICT)
    public class UserExistsException extends RuntimeException {
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public class ForbiddenException extends RuntimeException {
    }
}
