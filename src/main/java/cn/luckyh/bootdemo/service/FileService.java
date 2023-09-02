package cn.luckyh.bootdemo.service;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
//@org.springframework.transaction.annotation.Transactional(rollbackFor = RuntimeException.class)
public class FileService {
    public String uploadFile() {

        try {
            TimeUnit.SECONDS.sleep(5);
            return IdUtil.fastSimpleUUID();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
