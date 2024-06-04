package com.easybbs.controller;

import com.easybbs.annotation.GlobalInterceptor;
import com.easybbs.entity.dto.*;
import com.easybbs.entity.enums.ResponseCodeEnum;
import com.easybbs.entity.enums.SysSettingCodeEnum;
import com.easybbs.entity.po.SysSetting;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.mapper.SysSettingMapper;
import com.easybbs.service.SysSettingService;
import com.easybbs.utils.JsonUtils;
import com.easybbs.utils.StringUtils;
import com.easybbs.utils.SysCacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author: iohw
 * @date: 2024/5/28 21:08
 * @description:
 */
@RestController
@RequestMapping("/setting")
public class SysSettingController extends ABaseController {
    private static final Logger logger = LoggerFactory.getLogger(SysSettingController.class);
    @Autowired
    SysSettingMapper sysSettingMapper;
    @Autowired
    SysSettingService sysSettingService;

    @RequestMapping("/getSetting")
    public ResponseVO getSetting() {
        SysSettingDto sysSettingDto = new SysSettingDto();
        try {
            //总系统设置类，字段为各个模块的系统设置
            List<SysSetting> list = sysSettingMapper.selectList();
            // SysSetting实体 -> code + jsonContent 两个属性
            for (SysSetting sysSetting : list) {
                // 解析jsonContent,解析成对应于code的系统设置的实体类
                String jsonContent = sysSetting.getJsonContent();
                if (StringUtils.isEmpty(jsonContent)) {
                    continue;
                }
                String code = sysSetting.getCode();
                SysSettingCodeEnum sysSettingCodeEnum = SysSettingCodeEnum.getByCode(code);
                /**
                 * 反射装数据
                 */
                PropertyDescriptor pd = new PropertyDescriptor(sysSettingCodeEnum.getPropName(), SysSettingDto.class);
                Method method = pd.getWriteMethod();// set方法
                Class subClassz = Class.forName(sysSettingCodeEnum.getClassz());// 子系统设置的字节码
                // 形参: 调用者 调用参数
                method.invoke(sysSettingDto, JsonUtils.convertJson2Object(jsonContent, subClassz));
            }
        } catch (Exception e) {
            logger.error("获取系统设置失败", e);
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }
        return getSuccessResponseVo(sysSettingDto);
    }

    @RequestMapping("/saveSetting")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO saveSetting(SysSetting4RegisterDto registerDto, SysSetting4AuditDto auditDto,
                                  SysSetting4CommentDto commentDto, SysSetting4LikeDto likeDto,
                                  SysSetting4PostDto postDto, SysSetting4EmailDto emailDto) {
        SysSettingDto sysSettingDto = new SysSettingDto();
        sysSettingDto.setRegisterSetting(registerDto);
        sysSettingDto.setAuditSetting(auditDto);
        sysSettingDto.setCommentSetting(commentDto);
        sysSettingDto.setLikeSetting(likeDto);
        sysSettingDto.setPostSetting(postDto);
        sysSettingDto.setEmailSetting(emailDto);
        sysSettingService.saveSetting(sysSettingDto);
        return getSuccessResponseVo(null);
    }
}
