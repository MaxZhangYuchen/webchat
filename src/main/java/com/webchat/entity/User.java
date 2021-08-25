package com.webchat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

// 创建User实例
@Data   //省略get,set等操作
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {  //支持序列化

    private Integer id; //主键
    private String email; //邮箱
    private String password; //密码, 使用MD5加salt进行加密
    private String salt;        // 盐
    private String confirmCode;         //确认码
    private LocalDateTime activationTime;       //激活失效时间
    private Byte isValid;  //是否可用



}
