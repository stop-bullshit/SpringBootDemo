package cn.luckyh.bootdemo.model.vo;

import cn.luckyh.bootdemo.model.dto.TestDO;
import lombok.Data;

import java.util.List;

@Data
public class Result {
    private Integer curPage;
    private Integer totalNum;
    private Integer totalPageNum;
    private Integer pageSize;
    private List<TestDO> pageData;
}
