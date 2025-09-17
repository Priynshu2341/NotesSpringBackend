package com.example.main.controller;

import com.example.main.dto.LoginRequestDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {


     public String test(){
         return "test";
     }
}
