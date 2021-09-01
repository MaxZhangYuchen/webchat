package com.webchat.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * 邮件服务
 */
@Service
public class MailService {

    //获取发件人用户名
    @Value("${spring.mail.username}")
    private String mailUsername;

    //java发邮件的对象
    @Resource
    private JavaMailSender javaMailSender;

    //java邮件模板引擎
    @Resource
    private TemplateEngine templateEngine;

    public void sendMailForActivationAccount(String activationUrl, String email){

        //创建邮件对象
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage,true);
            //设置邮件主题
            message.setSubject("欢迎注册聊天室——账号激活");
            //设置邮件发送者
            message.setFrom(mailUsername);
            //设置邮件的接收者
            message.setTo(email);
            //设置邮件发送日期
            message.setSentDate(new Date());
            //创建上下文环境，模板
            Context context = new Context();
            context.setVariable("activationUrl", activationUrl);
            String text = templateEngine.process("activation-account.html", context);
            //设置邮件正文
            message.setText(text, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        //邮件发送
        javaMailSender.send(mimeMessage);
        }



    }
