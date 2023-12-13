package cn.luckyh.bootdemo.model.dto;

import java.io.Serial;
import java.io.Serializable;

public class TestDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 检查时间
    private String checkdate;
    // 姓名
    private String name;
    // 性别
    private String sex;
    // 生日
    private String birthday;
    // 身份证号码
    private String sno;
    // 家庭住址
    private String home;
    // 民族
    private String nation;
    // 学校（街道）
    private String school;
    // 电话
    private String parentTel;
    // 裸眼（右）
    private String lyRight;
    // 裸眼（左）
    private String lyLeft;
    // 戴镜（右）
    private String djRight;
    // 戴镜（左）
    private String djLeft;
    // 球镜（右）
    private String qjR;
    // 柱镜（右）
    private String zjR;
    // 轴位（右）
    private String zwR;
    // 球镜（左）
    private String qjL;
    // 柱镜（左）
    private String zjL;
    // 轴位（左）
    private String zwL;
    // 眼压（左）
    private String yyL;
    // 眼压（右）
    private String yyR;

    public TestDO() {
    }

    public String getCheckdate() {
        return checkdate;
    }

    public void setCheckdate(String checkdate) {
        this.checkdate = checkdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getParentTel() {
        return parentTel;
    }

    public void setParentTel(String parentTel) {
        this.parentTel = parentTel;
    }

    public String getLyRight() {
        return lyRight;
    }

    public void setLyRight(String lyRight) {
        this.lyRight = lyRight;
    }

    public String getLyLeft() {
        return lyLeft;
    }

    public void setLyLeft(String lyLeft) {
        this.lyLeft = lyLeft;
    }

    public String getDjRight() {
        return djRight;
    }

    public void setDjRight(String djRight) {
        this.djRight = djRight;
    }

    public String getDjLeft() {
        return djLeft;
    }

    public void setDjLeft(String djLeft) {
        this.djLeft = djLeft;
    }

    public String getQjR() {
        return qjR;
    }

    public void setQjR(String qjR) {
        this.qjR = qjR;
    }

    public String getZjR() {
        return zjR;
    }

    public void setZjR(String zjR) {
        this.zjR = zjR;
    }

    public String getZwR() {
        return zwR;
    }

    public void setZwR(String zwR) {
        this.zwR = zwR;
    }

    public String getQjL() {
        return qjL;
    }

    public void setQjL(String qjL) {
        this.qjL = qjL;
    }

    public String getZjL() {
        return zjL;
    }

    public void setZjL(String zjL) {
        this.zjL = zjL;
    }

    public String getZwL() {
        return zwL;
    }

    public void setZwL(String zwL) {
        this.zwL = zwL;
    }

    public String getYyL() {
        return yyL;
    }

    public void setYyL(String yyL) {
        this.yyL = yyL;
    }

    public String getYyR() {
        return yyR;
    }

    public void setYyR(String yyR) {
        this.yyR = yyR;
    }
}
