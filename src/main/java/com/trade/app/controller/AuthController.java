package com.trade.app.controller;

import com.trade.app.dto.LoginDTO;
import com.trade.app.dto.UserDTO;
import com.trade.app.response.AuthResponse;
import com.trade.app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/reg")
    public ResponseEntity<AuthResponse> register(@RequestBody UserDTO userDTO) throws Exception {
        AuthResponse response =  authService.register(userDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO loginDTO) throws Exception{
        AuthResponse response = authService.login(loginDTO);
        return ResponseEntity.ok(response);
    }


}
