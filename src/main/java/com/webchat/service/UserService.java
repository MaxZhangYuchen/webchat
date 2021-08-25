package com.webchat.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.webchat.entity.User;
import com.webchat.mapper.UserMapper;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jws.soap.SOAPBinding;
import java.beans.Transient;
import java.security.Security;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private MailService mailService;

    /**
     * 注册账号
     * @param user
     * @return
     */
    @Transactional(rollbackFor = Exception.class)  //出错回滚
    public Map<String, Object> createAccount(User user){
        // 雪花算法生成确认码，保证不会重复
        String confirmCode = IdUtil.getSnowflake(1,1).nextIdStr();

        // salt
        String salt = RandomUtil.randomString(6);

        //加密密码 原始密码+盐 转换为md5
        String md5Pwd = SecureUtil.md5(user.getPassword() + salt);

        //激活失效时间：24小时
        LocalDateTime ldt = LocalDateTime.now().plusDays(1);

        //初始化账号信息
        user.setSalt(salt);
        user.setPassword(md5Pwd);
        user.setConfirmCode(confirmCode);
        user.setActivationTime(ldt);
        user.setIsValid((byte)0);


        //新增账号
        int result = userMapper.insertUser(user);

        Map<String, Object> resultMap = new HashMap<>();
        if(result >0){
            //发送邮件
            String activationUrl = "http://localhost:8080/user/activation?confirmCode=" + confirmCode; //在链接后面追加confirmCode
            mailService.sendMailForActivationAccount(activationUrl, user.getEmail());
            resultMap.put("code", 200);
            resultMap.put("message", "注册成功，前往邮箱进行账号激活");
        }else{
            resultMap.put("code", 400);
            resultMap.put("message", "注册失败");
        }
        return resultMap;
    }


    public Map<String, Object> loginAccount(User user){
        Map<String, Object> resultMap = new HashMap<>();
        //根据邮箱查询用户

        List<User> userList = userMapper.selectUserByEmail(user.getEmail());
        //查询不到，返回：用户未激活
        if(userList == null || userList.isEmpty()){
            resultMap.put("code", 400);
            resultMap.put("message", "该用户不存在或未激活");
            return resultMap;
        }
        //查询到多个用户，联系管理员
        if(userList.size()>1){
            resultMap.put("code", 400);
            resultMap.put("message", "账号异常联系管理员");
            return resultMap;
        }
        //查询到一个用户，进行密码比对
        User user1 = userList.get(0);  //u为userlist的第一个，从数据库中查到的
        String md5psw = SecureUtil.md5(user.getPassword() + user1.getSalt());
        //密码不一致，返回，用户名或密码错误
        if(!user1.getPassword().equals(md5psw)){
            resultMap.put("code", 400);
            resultMap.put("message", "用户名密码错误");
            return resultMap;
        }
        //相同，返回：登录成功
        resultMap.put("code", 200);
        resultMap.put("message", "登录成功");
        return resultMap;


    }

    /**
     * 激活账号
     * @param confirmCode
     * @return
     */
    public Map<String, Object> activationAccount(String confirmCode) {
        Map<String,Object> resultMap = new HashMap<>();
        //根据确认码查询用户
        User user = userMapper.selectUserByConfirmCode(confirmCode);
        //判断是否超时
        boolean after = LocalDateTime.now().isAfter(user.getActivationTime());
        if(after){
            resultMap.put("code",400);
            resultMap.put("message","链接失效请重新注册");
            return resultMap;
        }
        //根据确认码查询用户并修改状态值为1
        int result = userMapper.updateUserByConfirmCode(confirmCode);
        if(result > 0){
            resultMap.put("code",200);
            resultMap.put("message","激活成功");
        }
        else{
            resultMap.put("code",400);
            resultMap.put("message","激活失败");
        }
        return resultMap;
    }
}
