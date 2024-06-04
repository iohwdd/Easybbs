package com.easybbs;

import com.alibaba.fastjson.JSON;
import com.easybbs.entity.dto.SysSetting4RegisterDto;
import com.easybbs.entity.po.ForumArticle;
import com.easybbs.mapper.ForumArticleMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: iohw
 * @date: 2024/5/10 14:09
 * @description:
 */
@SpringBootTest
public class Test {
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    ForumArticleMapper forumArticleMapper;
    @org.junit.jupiter.api.Test
    public void test01() throws MessagingException, UnsupportedEncodingException {
        // 创建一个邮件消息
        MimeMessage message = javaMailSender.createMimeMessage();

        // 创建 MimeMessageHelper
        MimeMessageHelper helper = new MimeMessageHelper(message, false);

        // 发件人邮箱和名称
        helper.setFrom("2023321332@qq.com", "springdoc");
        // 收件人邮箱
        helper.setTo("884217544@qq.com");
        // 邮件标题
        helper.setSubject("Hello");
        // 邮件正文，第二个参数表示是否是HTML正文
        helper.setText("Hello <strong> World</strong>！", true);

        // 发送
        javaMailSender.send(message);
    }
    @org.junit.jupiter.api.Test
    public void test02(){
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9!@#$%^&*()_+]{8,18}$");
        Matcher matcher = pattern.matcher("12345678");
        System.out.println(matcher.matches());
    }
    @org.junit.jupiter.api.Test
    public void test03() {
        PageHelper.startPage(1,15);
        List<ForumArticle> list = forumArticleMapper.selectAll();
        PageInfo<ForumArticle> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
    }
}
