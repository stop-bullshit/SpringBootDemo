package cn.luckyh.bootdemo.controller;

import cn.luckyh.bootdemo.annotations.CustomParam;
import cn.luckyh.bootdemo.model.dto.PersonDTO;
import cn.luckyh.bootdemo.model.dto.PersonUploadDto;
import cn.luckyh.bootdemo.service.PersonService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

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
    public Integer threadTst3(@RequestParam Integer num) {
        System.out.println(num);
        return num;
//        personService.threadTst3(num);
    }

    @PostMapping(value = "/threadTst4")
    public Integer threadTst4(@RequestParam(value = "num", name = "num") Integer num) {
        System.out.println(num);
        return num;
//        personService.threadTst3(num);
    }

    @PostMapping(value = "/threadTst5")
    public String threadTst5(@RequestBody Map num) {
        System.out.println(num);
        return String.valueOf(num);
    }

    @PostMapping(value = "/threadTst6")
    public String threadTst6(@RequestBody PersonDTO dto) {
        System.out.println(dto.getName());
        return dto.getName();
//        personService.threadTst3(num);
    }

    @PostMapping(value = "/threadTst7")
    public Integer threadTst7(@CustomParam(value = "num") Integer num,@CustomParam(value = "age") Integer age) {
        System.out.println(num);
        System.out.println(age);
        return num;
//        personService.threadTst3(num);
    }
}
