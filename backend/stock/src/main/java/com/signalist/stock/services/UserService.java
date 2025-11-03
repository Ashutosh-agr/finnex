package com.signalist.stock.services;

import com.signalist.stock.entity.User;
import com.signalist.stock.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final PersonalizedEmailService personalizedEmailService;

    public UserService (UserRepository userRepository, PersonalizedEmailService personalizedEmailService){
        this.userRepository = userRepository;
        this.personalizedEmailService = personalizedEmailService;
    }

    public void saveEntry(User user) throws Exception {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        personalizedEmailService.generateAndSendWelcomeEmail(user);
    }

    public User checkUser(String email,String password){
        User user = userRepository.findByEmail(email);
        if(user == null){
            return null;
        }

        if(passwordEncoder.matches(password,user.getPassword())){
            return user;
        }

        return null;
    }
}
