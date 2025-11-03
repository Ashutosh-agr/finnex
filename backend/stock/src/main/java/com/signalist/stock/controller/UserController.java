package com.signalist.stock.controller;

import com.signalist.stock.dto.AuthResponse;
import com.signalist.stock.dto.LoginRequest;
import com.signalist.stock.entity.User;
import com.signalist.stock.repository.UserRepository;
import com.signalist.stock.security.CustomUserDetailsService;
import com.signalist.stock.services.UserService;
import com.signalist.stock.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public UserController(UserRepository userRepository,UserService userService,AuthenticationManager authenticationManager,CustomUserDetailsService userDetailsService,JwtUtil jwtUtil){
        this.userRepository = userRepository;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody User user) throws Exception {
        userService.saveEntry(user);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody LoginRequest request){
       try{
           UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword());

           authenticationManager.authenticate(authenticationToken);
           UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

           String token = jwtUtil.generateToken(userDetails);
           return ResponseEntity.ok(new AuthResponse(token,"Bearer"));
       }catch (AuthenticationException ex) {
           User existingUser = userRepository.findByEmail(request.getEmail());
           if (existingUser == null) {
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not exist");
           } else {
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect Password");
           }
       }
    }

}
