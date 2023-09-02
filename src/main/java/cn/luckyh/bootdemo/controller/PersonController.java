package cn.luckyh.bootdemo.controller;

import cn.luckyh.bootdemo.service.PersonService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RestController
public class PersonController {

    @Resource
    private PersonService personService;


    @PostMapping(value = "/upload")
    public void upload(HttpServletResponse response) {
        PrintWriter writer;
        try {
            personService.upload();
            writer = response.getWriter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writer.write("success");

    }

    @PostMapping(value = "/threadTst")
    public void threadTst(Integer num) {
        personService.threadTst(num);
    }
}
