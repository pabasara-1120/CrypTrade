package com.trade.app.controller;

import com.trade.app.dto.LoginDTO;
import com.trade.app.dto.UserDTO;
import com.trade.app.entity.User;
import com.trade.app.response.AuthResponse;
import com.trade.app.service.AuthService;
import com.trade.app.service.WatchListService;
import com.trade.app.utility.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private WatchListService watchListService;

    @PostMapping("/reg")
    public ResponseEntity<AuthResponse> register(@RequestBody UserDTO userDTO) throws Exception {
        AuthResponse response =  authService.register(userDTO);
        User user = UserMapper.toEntity(userDTO); //change this later
        watchListService.createWatchList(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO loginDTO) throws Exception{
        AuthResponse response = authService.login(loginDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/two-factor-otp/{otp}")
    public ResponseEntity<AuthResponse> verifySingInOtp(@PathVariable String otp, @RequestParam String id) throws Exception {
        AuthResponse authResponse = authService.verifySigninOtp(otp,id);
        return ResponseEntity.ok(authResponse);

    }




}
