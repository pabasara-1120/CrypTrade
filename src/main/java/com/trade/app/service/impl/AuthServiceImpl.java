package com.trade.app.service.impl;

import com.trade.app.config.JwtProvider;
import com.trade.app.dto.LoginDTO;
import com.trade.app.dto.UserDTO;
import com.trade.app.entity.TwoFactorOTP;
import com.trade.app.entity.User;
import com.trade.app.repository.TwoFactorOTPRepository;
import com.trade.app.repository.UserRepository;
import com.trade.app.response.AuthResponse;
import com.trade.app.service.AuthService;
import com.trade.app.service.TwoFactorOTPService;
import com.trade.app.utility.MailUtil;
import com.trade.app.utility.OTPUtil;
import com.trade.app.utility.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private TwoFactorOTPRepository twoFactorOTPRepository;

    private final TwoFactorOTPService twoFactorOTPService;

    public AuthServiceImpl(TwoFactorOTPService twoFactorOTPService) {
        this.twoFactorOTPService = twoFactorOTPService;
    }


    @Override
    public AuthResponse register(UserDTO userDTO) throws Exception {
        User user = UserMapper.toEntity(userDTO);
        if(userRepository.findByEmail(user.getEmail())!=null){
            throw new Exception("email is already in use");
        }



        User savedUser = userRepository.save(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = JwtProvider.generateToken(auth);



        return AuthResponse.builder()
                .jwt(jwt)
                .status(true)
                .message("Registration Successful").isTwoFactorAuthEnabled(false).build();


        //return toDTO(savedUser);


    }

    @Override
    public AuthResponse login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        Authentication auth = authenticate(username,password);

        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = JwtProvider.generateToken(auth);

        User user = userRepository.findByEmail(username);

        if(user.getTwoFactorAuth().isEnabled()){

            String otp = OTPUtil.generateOTP();

            TwoFactorOTP oldTwoFactorOtp = twoFactorOTPRepository.findByUserId(user.getId());
            if(oldTwoFactorOtp!=null){
                oldTwoFactorOtp.setOtp(otp);
                oldTwoFactorOtp.setJwt(jwt);
                twoFactorOTPRepository.save(oldTwoFactorOtp);
            }
            else {
                TwoFactorOTP twoFactorOTP = TwoFactorOTP.builder()
                        .id(UUID.randomUUID().toString())
                        .jwt(jwt)
                        .user(user)
                        .build();
                twoFactorOTPRepository.save(twoFactorOTP);

            }
            MailUtil.sendOtpEmail(user.getEmail(), otp);

            return AuthResponse.builder()
                    .message("Two factor Auth Enabled. Please verify with OTP")
                    .isTwoFactorAuthEnabled(true)
                    .status(false)
                    .build();


        }
        return AuthResponse.builder()
                .jwt(jwt)
                .status(true)
                .message("Login Successful").isTwoFactorAuthEnabled(false).build();
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
        if(userDetails==null){
            throw new BadCredentialsException("Invalid username");
        }
        if(!password.equals(userDetails.getPassword())){
            throw new BadCredentialsException("Incorrect password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,password,userDetails.getAuthorities());
    }

    public AuthResponse verifySigninOtp(String otp, String id) throws Exception{
        TwoFactorOTP twoFactorOTP = twoFactorOTPService.findById(id);
        if(twoFactorOTPService.verifyTwoFactorOTP(twoFactorOTP,otp)){
            return AuthResponse.builder()
                    .message("Two Factor Authentication Verified")
                    .isTwoFactorAuthEnabled(true)
                    .jwt(twoFactorOTP.getJwt())
                    .status(true)
                    .build();
        }
        else{
            throw new Exception("Invalid otp");

        }



    }




}
