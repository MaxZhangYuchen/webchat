package com.webchat.mapper;

import com.webchat.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;


//SQL
public interface UserMapper {
    /**
     * 新增账号
     * createAccount调用
     * @param user
     * @return
     */
    @Insert("INSERT INTO user( nickname, email, password, salt, confirm_code, activation_time, is_valid)" +
            "VALUES(#{nickname}, #{email}, #{password}, #{salt}, #{confirmCode}, #{activationTime}, #{isValid})")
    int insertUser(User user);

    /**
     * 根据确认码查询用户
     * activationAccount 调用
     * @param confirmCode
     * @return
     */
    @Select("SELECT email, activation_time FROM user WHERE confirm_code = #{confirmCode} AND is_valid=0")
    User selectUserByConfirmCode(@Param("confirmCode") String confirmCode);

    /**
     * 更新用户为激活状态
     * activationAccount 调用
     * @param confirmCode
     * @return
     */
    @Update("UPDATE user SET is_valid=1 WHERE confirm_code = #{confirmCode}")
    int updateUserByConfirmCode(@Param("confirmCode") String confirmCode);

    /**
     * 根据邮箱查询用户
     * loginAccount 调用
     * 查询出昵称，邮箱，密码，盐
     * @param email
     * @return
     */
    @Select("SELECT nickname, email, password, salt FROM user WHERE email = #{email} AND is_valid=1")
    List<User> selectUserByEmail(@Param("email") String email);


}

