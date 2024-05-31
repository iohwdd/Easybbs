package com.easybbs.controller;

import com.easybbs.annotation.GlobalInterceptor;
import com.easybbs.annotation.VerifyParam;
import com.easybbs.config.AdminConfig;
import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.dto.CreateImageCode;
import com.easybbs.entity.dto.SessionAdminUserDto;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.service.UserInfoService;
import com.easybbs.utils.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.easybbs.entity.constants.Constants.CHECK_CODE_KEY;

/**
 * @author: iohw
 * @date: 2024/5/8 21:52
 * @description:
 */
@RestController
public class AccountController extends ABaseController {
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    AdminConfig adminConfig;

    /**
     * 发送图片验证码
     */
    @RequestMapping("/checkCode")
    public void checkCode(HttpServletResponse response, HttpSession session) throws IOException {
        CreateImageCode vCode = new CreateImageCode(130, 38, 4, 10);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        String code = vCode.getCode();
        session.setAttribute(Constants.CHECK_CODE_KEY, code);
        vCode.write(response.getOutputStream());
    }

    @RequestMapping("/login")
    @GlobalInterceptor(checkParams = true)
    @ResponseBody
    public ResponseVO login(HttpSession session,
                            @VerifyParam(required = true) String account,
                            @VerifyParam(required = true) String password,
                            @VerifyParam(required = true) String checkCode) {
        String code = (String) session.getAttribute(CHECK_CODE_KEY);
        if (!StringUtils.isEmpty(code) && !checkCode.equals(code)) {
            throw new BusinessException("验证码错误");
        }
        String adminAccount = adminConfig.getAdminAccount();
        String adminPassword = adminConfig.getPassword();
        if (!account.equals(adminAccount) || !password.equals(DigestUtils.md5Hex(adminPassword))) {
            throw new BusinessException("账号或密码错误");
        }
        SessionAdminUserDto sessionAdminUserDto = new SessionAdminUserDto();
        sessionAdminUserDto.setAccount(adminAccount);
        session.setAttribute(Constants.SESSION_KEY, sessionAdminUserDto);
        return getSuccessResponseVo(adminAccount);
    }


}
