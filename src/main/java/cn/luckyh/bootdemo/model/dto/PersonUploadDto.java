package cn.luckyh.bootdemo.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PersonUploadDto {

    private List<String> ids;
    private String reason;
    private MultipartFile file;
}
