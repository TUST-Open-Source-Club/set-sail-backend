package com.tustosc.setsail.Controller;

import com.tustosc.setsail.Entiy.Response;
import com.tustosc.setsail.Entiy.User;
import com.tustosc.setsail.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/auth/userinfo")
    public ResponseEntity<Response<User>> getUserInfo(@RequestParam String id) {
        Optional<User> userOptional=userService.getUserInfo(id);
        return userOptional.map(
                user -> ResponseEntity.ok(new Response<>(user))).orElseGet(()
                -> ResponseEntity.ok(new Response<>(null)));
    }

    private static class UserLogin {
        String username;
        String password;
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLogin userLogin) {
        String token = userService.userLogin(userLogin.username, userLogin.password);
        if(token != null && !token.isBlank()){
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.badRequest().build();
    }

}
