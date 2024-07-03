package com.easybbs.controller;

import com.easybbs.annotation.GlobalInterceptor;
import com.easybbs.annotation.VerifyParam;
import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.dto.CreateImageCode;
import com.easybbs.entity.dto.SessionWebUserDto;
import com.easybbs.entity.dto.SysSetting4CommentDto;
import com.easybbs.entity.dto.SysSettingDto;
import com.easybbs.entity.enums.ResponseCodeEnum;
import com.easybbs.entity.enums.VerifyRegexEnum;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.service.EmailCodeService;
import com.easybbs.service.UserInfoService;
import com.easybbs.utils.IpUtils;
import com.easybbs.utils.StringUtils;
import com.easybbs.utils.SysCacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.easybbs.entity.constants.Constants.CHECK_CODE_KEY;
import static com.easybbs.entity.constants.Constants.CHECK_CODE_KEY_EMAIL;

/**
 * @author: iohw
 * @date: 2024/5/8 21:52
 * @description:
 */
@RestController
public class AccountController extends ABaseController{
    @Autowired
    private EmailCodeService emailCodeService;
    @Autowired
    private UserInfoService userInfoService;
    /**
     * 发送图片验证码
     */
    @RequestMapping("/checkCode")
    public void checkCode(HttpServletResponse response, HttpSession session,Integer type) throws IOException {
        CreateImageCode vCode = new CreateImageCode(130,38,4,10);
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires",0);
        response.setContentType("image/jpeg");
        String code = vCode.getCode();
        //登录注册 所需要的验证码
        if(type == null || type == 0){
            session.setAttribute(Constants.CHECK_CODE_KEY,code);
        }else{
            //获取邮箱验证码 所需要的验证码
            session.setAttribute(Constants.CHECK_CODE_KEY_EMAIL,code);
        }
        vCode.write(response.getOutputStream());
    }

    /**
     * 发送邮箱验证码邮件
     * @param session
     * @param email
     * @param checkCode
     * @param type 0:注册 1:找回密码
     * @return
     * @throws IOException
     */
    @GlobalInterceptor(checkParams = true)
    @RequestMapping("/sendEmailCode")
    public ResponseVO sendEmailCode(HttpSession session,
                                    @VerifyParam(required = true) String email,
                                    @VerifyParam(required = true) String checkCode,
                                    @VerifyParam(required = true) Integer type) {
       try {
           if(StringUtils.isEmpty(email) || StringUtils.isEmpty(checkCode) || type == null){
               throw new BusinessException(ResponseCodeEnum.CODE_600);
           }
           if(!checkCode.equalsIgnoreCase((String) session.getAttribute(CHECK_CODE_KEY_EMAIL))){
               throw new BusinessException("图片验证码错误");
           }
           emailCodeService.sendEmailCode(email,type);
           return getSuccessResponseVo(null);
       } finally {
           session.removeAttribute(CHECK_CODE_KEY_EMAIL); //让校验过的验证码失效
       }
    }

    /**
     * 用户注册
     * @param session
     * @param email
     * @param emailCode
     * @param nickName
     * @param password
     * @param checkCode
     * @return
     * @throws IOException
     */
    @RequestMapping("/register")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO register(HttpSession session,
                               @VerifyParam(required = true, regex = VerifyRegexEnum.EMAIL, max = 150) String email,
                               @VerifyParam(required = true) String emailCode,
                               @VerifyParam(required = true, max = 20) String nickName,
                               @VerifyParam(required = true, min = 8, max = 18 ,regex = VerifyRegexEnum.PASSWORD) String password,
                               @VerifyParam(required = true) String checkCode) {
        try {
            if(!checkCode.equalsIgnoreCase((String) session.getAttribute(CHECK_CODE_KEY))){
                throw new BusinessException("图片验证码错误");
            }
            userInfoService.register(email, emailCode, nickName, password, checkCode);
            return getSuccessResponseVo(null);
        } finally {
            session.removeAttribute(CHECK_CODE_KEY); //让校验过的验证码失效
        }
    }
    @RequestMapping("/login")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO login(HttpSession session,
                           HttpServletRequest request,
                           @VerifyParam(required = true) String email,
                           @VerifyParam(required = true) String password,
                           @VerifyParam(required = true) String checkCode) {
        try {
            if(!checkCode.equalsIgnoreCase((String) session.getAttribute(CHECK_CODE_KEY))){
                throw new BusinessException("图片验证码错误");
            }
            String ip = IpUtils.getIpAddress(request);
            SessionWebUserDto sessionWebUserDto =  userInfoService.login(email, password, ip);
            session.setAttribute(Constants.SESSION_KEY, sessionWebUserDto);
            return getSuccessResponseVo(sessionWebUserDto);
        } finally {
            session.removeAttribute(CHECK_CODE_KEY); //让校验过的验证码失效
        }
    }
    @RequestMapping("/getUserInfo")
    @GlobalInterceptor()
    public ResponseVO getUserInfo(HttpSession session) {
        SessionWebUserDto sessionWebUserDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
        return getSuccessResponseVo(sessionWebUserDto);
    }
    @RequestMapping("/logout")
    @GlobalInterceptor()
    public ResponseVO logout(HttpSession session) {
        session.invalidate();
        return getSuccessResponseVo(null);
    }
    @RequestMapping("/getSysSetting")
    @GlobalInterceptor()
    public ResponseVO getSysSetting() {
        SysSettingDto sysSettingDto = SysCacheUtils.getSysSetting();
        SysSetting4CommentDto commentDto = sysSettingDto.getCommentSetting();
        Map<String, Object> map = new HashMap<>();
        map.put("commentOpen", commentDto.getCommentOpen());
        return getSuccessResponseVo(map);
    }
    @RequestMapping("/resetPwd")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO resetPwd(HttpSession session,
                               @VerifyParam(required = true) String email,
                               @VerifyParam(required = true, min = 8, max = 18 ,regex = VerifyRegexEnum.PASSWORD) String password,
                               @VerifyParam(required = true) String emailCode,
                               @VerifyParam(required = true) String checkCode) {
        try {
            if(!checkCode.equalsIgnoreCase((String) session.getAttribute(CHECK_CODE_KEY))){
                throw new BusinessException("图片验证码错误");
            }
            userInfoService.resetPwd(email, password, emailCode);
        } finally {
            session.removeAttribute(CHECK_CODE_KEY);
        }
        return getSuccessResponseVo(null);
    }
}
