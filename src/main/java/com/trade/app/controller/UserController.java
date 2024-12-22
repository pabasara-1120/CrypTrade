package com.trade.app.controller;

import com.trade.app.dto.UserDTO;
import com.trade.app.entity.User;
import com.trade.app.enums.VerificationType;
import com.trade.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO userDTO = userService.findUserProfileDtoByJwt(jwt);
        return ResponseEntity.ok(userDTO);

    }

    @PatchMapping("/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<UserDTO> enableTwoFactorAuthentication(@RequestHeader("Authorization") String jwt, @PathVariable VerificationType type) throws Exception{
       User user = userService.findUserProfileByJwt(jwt);
       UserDTO userDTO=  userService.enableTwoFactorAuthentication(type,user);
       return ResponseEntity.ok(userDTO);

    }





}
