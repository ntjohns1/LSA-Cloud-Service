package com.noslen.lsaadminservice.controller;

import com.noslen.lsaadminservice.model.LSAUser;
import com.noslen.lsaadminservice.repository.LSAUserRepository;
import com.noslen.lsaadminservice.util.auth.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RefreshScope
public class AdminServiceController {
    @Autowired
    private LSAUserRepository userRepository;

    @PostMapping("/api/addUser")
    @ResponseStatus(HttpStatus.CREATED)
    public LSAUser createCUser(@RequestBody LSAUser user) {
        PasswordUtil pUtil = new PasswordUtil(user.getPassword());
        user.setPassword(pUtil.getEncodedPassword());
        userRepository.save(user);
        return user;
    }
}
