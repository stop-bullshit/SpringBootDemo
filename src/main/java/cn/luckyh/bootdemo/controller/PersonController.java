package cn.luckyh.bootdemo.controller;

import cn.luckyh.bootdemo.model.dto.PersonUploadDto;
import cn.luckyh.bootdemo.service.PersonService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Slf4j
@RestController
public class PersonController {

    @Resource
    private PersonService personService;


    @PostMapping(value = "/uploadTest")
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

    @GetMapping(value = "/test/{name}")
    public String testHello(@PathVariable String name) {
        return name;
    }

    @PostMapping("/upload")
    public void uploadTest(PersonUploadDto dto) {
        Assert.notNull(dto.getFile(), "文件不能为空");
        System.out.println(dto.getIds());
        List<String> list = dto.getIds();
        list.forEach(System.out::println);
        System.out.println(dto.getReason());
    }


    @PostMapping(value = "/threadTst2")
    public void threadTst2(Integer num) {
        personService.threadTst2(num);
    }

    @PostMapping(value = "/threadTst3")
    public void threadTst3(Integer num) {
        personService.threadTst3(num);
    }
}
