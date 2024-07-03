package com.easybbs.service.impl;

import com.easybbs.config.WebConfig;
import com.easybbs.entity.dto.SysSetting4EmailDto;
import com.easybbs.entity.po.EmailCode;
import com.easybbs.entity.po.UserInfo;
import com.easybbs.exception.BusinessException;
import com.easybbs.mapper.EmailCodeMapper;
import com.easybbs.mapper.UserInfoMapper;
import com.easybbs.service.EmailCodeService;
import com.easybbs.utils.StringUtils;
import com.easybbs.utils.SysCacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Objects;

import static com.easybbs.entity.constants.Constants.*;

/**
 * @author: iohw
 * @date: 2024/5/9 16:26
 * @description:
 */
@Service
public class EmailCodeServiceImpl implements EmailCodeService {
    private static Logger logger = LoggerFactory.getLogger(EmailCodeServiceImpl.class);
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    EmailCodeMapper emailCodeMapper;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    WebConfig webConfig;

    /**
     * 发送邮箱验证码邮件
     *
     * @param email
     * @param type
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendEmailCode(String email, Integer type) {
        if (Objects.equals(type, ZERO)) {
            //是注册的话，先检查该邮箱是否已经被注册
            UserInfo userInfo = userInfoMapper.selectByEmail(email);
            if (userInfo != null) {
                throw new BusinessException("邮箱已存在");
            }
        }
        String code = StringUtils.getRandomString(LENGTH_5);
        sendEmailCodeDo(email, code);// 发邮件
        //在新的验证码插入数据库之前，将旧的验证码全部设会失效 -> 保证只有一个验证码是有效的
        emailCodeMapper.disableEmailCode(email);
        EmailCode emailCode = new EmailCode();
        emailCode.setEmail(email);
        emailCode.setCode(code);
        emailCode.setStatus(ZERO);
        emailCode.setCreateTime(new Date());


        emailCodeMapper.insert(emailCode);
    }

    /**
     * 检验邮箱验证码
     * email + emailCode -> 联合主键
     *
     * @param email
     * @param emailCode
     */
    @Override
    public void checkCode(String email, String emailCode) {
        EmailCode dbInfo = emailCodeMapper.selectByEmailAndCode(email, emailCode);
        if (dbInfo == null) {
            throw new BusinessException("邮箱验证码不正确");
        }
        if (dbInfo.getStatus() != ZERO || System.currentTimeMillis() - dbInfo.getCreateTime().getTime() > 1000 * 60 * LENGTH_15) {
            throw new BusinessException("邮箱验证码已失效");
        }
        emailCodeMapper.disableEmailCode(email);
    }

    /**
     * spring 发送邮件
     *
     * @param toEmail
     * @param code
     */
    public void sendEmailCodeDo(String toEmail, String code) {
        try {
            SysSetting4EmailDto emailSetting = SysCacheUtils.getSysSetting().getEmailSetting();
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //发送人
            helper.setFrom(webConfig.getSendUserName(), emailSetting.getEmailTitle());
            //收件人
            helper.setTo(toEmail);
            helper.setSubject("注册邮箱验证码");
            helper.setText(String.format(emailSetting.getEmailContent(), code));
            helper.setSentDate(new Date());
            javaMailSender.send(message);
        } catch (Exception e) {
            logger.error("发送邮件失败", e);
            throw new BusinessException("邮件发送失败");
        }

    }
}
