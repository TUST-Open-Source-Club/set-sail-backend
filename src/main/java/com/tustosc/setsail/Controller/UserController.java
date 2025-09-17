package com.tustosc.setsail.Controller;

import com.tustosc.setsail.Entity.Response;
import com.tustosc.setsail.Entity.User;
import com.tustosc.setsail.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userinfo")
    public ResponseEntity<Response<User>> getUserInfo(@RequestParam String id) {
        Optional<User> userOptional=userService.getUserInfo(id);
        return userOptional.map(
                user -> ResponseEntity.ok(new Response<>(user))).orElseGet(()
                -> ResponseEntity.ok(new Response<>(null)));
    }
}
