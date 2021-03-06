package com.noslen.lsaauthservice.util.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "lsa-admin-service", url = "localhost:2121", path = "/", configuration = FeignConfiguration.class, decode404 = true)
public interface AdminClient {

//    //GET ALL USERS
//    @GetMapping("/api/users")
//    public List<Object> getAllUsers();
//
//    //GET USER BY ID
//    @GetMapping("/api/users/{id}")
//    public Object getUserById(@PathVariable Integer id);
//
//    //GET USER BY Username
    @GetMapping("/api/users/uname/{username}")
    public Object getUserByUsername(@PathVariable String username);

//    @PostMapping("/api/users")
//    public Object createUser(@RequestBody Object user);
//
//    //UPDATE USER BY ID
//    @PutMapping("/api/users/{id}")
//    public void updateUser(@RequestBody Object user, @PathVariable Integer id);
//
//    //DELETE USER BY ID
//    @DeleteMapping("/api/users/{id}")
//    public void deleteUser(@PathVariable Integer id);
}
