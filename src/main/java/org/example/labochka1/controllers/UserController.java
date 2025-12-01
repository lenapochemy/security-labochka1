package org.example.labochka1.controllers;

import org.example.labochka1.dto.UserDTO;
import org.example.labochka1.model.User;
import org.example.labochka1.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@RestController()
@RequestMapping("/auth")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        User user = userService.login(userDTO);
        if (user != null) {
            return ResponseEntity.ok(userService.generateToken(user));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/reg")
    public ResponseEntity<?> registration(@RequestBody UserDTO userDTO) {
        System.out.println("request on registration");
        UserDTO userDTO1 = new UserDTO(HtmlUtils.htmlEscape(userDTO.getLogin()), HtmlUtils.htmlEscape(userDTO.getPassword()));
        User user = userService.registration(userDTO1);
        if(user == null){
            return ResponseEntity.badRequest().body("Пользователь с таким логином уже существует");
        } else {
            return ResponseEntity.ok().build();
        }
    }



}
