package com.leyou.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@JsonIgnoreProperties(value={"hibernateLazyInitializer"})
public class Address implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY) //配置主键的生成策略
    Long aid;

    private String name;// 收件人姓名
    private String  phone;// 电话
    private String  state;// 省份
    private String  city;// 城市
    private String  district;// 区
    private String  address;// 街道地址
    private String  zipcode; // 邮编
    private Boolean defaulter;

    @Transient
    private String  zongdizhi;

    public String getZongdizhi() {
        return zongdizhi;
    }

    public void setZongdizhi(String zongdizhi) {
        this.zongdizhi = zongdizhi;
    }

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Boolean getDefaulter() {
        return defaulter;
    }

    public void setDefaulter(Boolean defaulter) {
        this.defaulter = defaulter;
    }

    public void setDefaulter(boolean defaulter) {
        this.defaulter = defaulter;
    }
}
