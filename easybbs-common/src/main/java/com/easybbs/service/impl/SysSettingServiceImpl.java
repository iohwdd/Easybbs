package com.easybbs.service.impl;

import com.easybbs.entity.dto.SysSettingDto;
import com.easybbs.entity.enums.SysSettingCodeEnum;
import com.easybbs.entity.po.SysSetting;
import com.easybbs.mapper.SysSettingMapper;
import com.easybbs.service.SysSettingService;
import com.easybbs.utils.JsonUtils;
import com.easybbs.utils.StringUtils;
import com.easybbs.utils.SysCacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author: iohw
 * @date: 2024/5/10 19:17
 * @description: 系统设置 -> 项目启动时就要读取
 */
@Service
public class SysSettingServiceImpl implements SysSettingService {
    @Autowired
    SysSettingMapper sysSettingMapper;
    private static Logger logger = LoggerFactory.getLogger(SysSettingServiceImpl.class);

    @Override
    public void refreshCache() {
        try {
            //总系统设置类，字段为各个模块的系统设置
            SysSettingDto sysSettingDto = new SysSettingDto();
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
                // 反射
                PropertyDescriptor pd = new PropertyDescriptor((String)sysSettingCodeEnum.getPropName(), SysSettingDto.class);
                Method method = pd.getWriteMethod();// set方法
                Class subClassz = Class.forName(sysSettingCodeEnum.getClassz());// 子系统设置的字节码
                // 形参: 调用者 调用参数
                method.invoke(sysSettingDto, JsonUtils.convertJson2Object(jsonContent, subClassz));
            }
            // 将系统设置保存在内存中
            SysCacheUtils.refresh(sysSettingDto);
        } catch (Exception e) {
            logger.error("刷新缓存失败", e);
        }
    }

    @Override
    public void saveSetting(SysSettingDto sysSettingDto) {
        sysSettingMapper.updateByPrimaryKey(SysSettingCodeEnum.REGISTER.getCode(), JsonUtils.convertObject2Json(sysSettingDto.getRegisterSetting()));
        sysSettingMapper.updateByPrimaryKey(SysSettingCodeEnum.LIKE.getCode(), JsonUtils.convertObject2Json(sysSettingDto.getLikeSetting()));
        sysSettingMapper.updateByPrimaryKey(SysSettingCodeEnum.EMAIL.getCode(), JsonUtils.convertObject2Json(sysSettingDto.getEmailSetting()));
        sysSettingMapper.updateByPrimaryKey(SysSettingCodeEnum.AUDIT.getCode(), JsonUtils.convertObject2Json(sysSettingDto.getAuditSetting()));
        sysSettingMapper.updateByPrimaryKey(SysSettingCodeEnum.COMMENT.getCode(), JsonUtils.convertObject2Json(sysSettingDto.getCommentSetting()));
        sysSettingMapper.updateByPrimaryKey(SysSettingCodeEnum.POST.getCode(), JsonUtils.convertObject2Json(sysSettingDto.getPostSetting()));
    }
}
