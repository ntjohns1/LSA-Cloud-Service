package com.noslen.lsaauthservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noslen.lsaauthservice.UserDetails.CurrentUser;
import com.noslen.lsaauthservice.model.CustomUser;
import com.noslen.lsaauthservice.repository.MapCustomUserRepository;
import com.noslen.lsaauthservice.util.feign.AdminClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RefreshScope
public class LSAAuthServiceController {

    @Autowired
    private final AdminClient adminClient;

    public LSAAuthServiceController(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    //GET USER BY USERNAME
    @GetMapping("/{username}")
    public Object user(@PathVariable String username) throws JsonProcessingException {

        Object obj = adminClient.getUserByUsername(username);
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(obj);
        JSONObject userObj = new JSONObject(s);
        int id = Integer.parseInt(userObj.get("id").toString());
        @CurrentUser CustomUser c = new CustomUser(id, userObj.get("email").toString(),userObj.get("username").toString(),userObj.get("password").toString());


        return userObj;
    }


}
