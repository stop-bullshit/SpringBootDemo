package cn.luckyh.bootdemo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PersonDTO {
    private String id;
    private String name;
    private int age;
    private Integer status;

    @Builder.Default
    private Boolean useRule = Boolean.TRUE;

    private Boolean notUseRule = Boolean.TRUE;


    public PersonDTO(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
