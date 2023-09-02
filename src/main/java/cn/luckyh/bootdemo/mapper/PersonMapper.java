package cn.luckyh.bootdemo.mapper;

import cn.hutool.core.util.IdUtil;
import cn.luckyh.bootdemo.model.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class PersonMapper {
    public Person findById() {
        Person luckyh = new Person(IdUtil.getSnowflakeNextId(), "luckyh", 15, 1, null);
        log.info("find person: {}", luckyh);
        return luckyh;
    }

    public void save(Person existingObject) {
        log.info("save person: {}", existingObject);
    }
}
