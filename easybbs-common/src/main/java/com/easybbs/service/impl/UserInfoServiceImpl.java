package com.easybbs.service.impl;

import com.easybbs.config.WebConfig;
import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.dto.SessionWebUserDto;
import com.easybbs.entity.enums.*;
import com.easybbs.entity.po.UserInfo;
import com.easybbs.entity.po.UserIntegralRecord;
import com.easybbs.entity.po.UserMessage;
import com.easybbs.entity.query.UserInfoQuery;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.vo.web.UserMsgVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.mapper.UserInfoMapper;
import com.easybbs.mapper.UserIntegralRecordMapper;
import com.easybbs.mapper.UserMessageMapper;
import com.easybbs.service.EmailCodeService;
import com.easybbs.service.UserInfoService;
import com.easybbs.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: iohw
 * @date: 2024/5/10 15:03
 * @description:
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    EmailCodeService emailCodeService;
    @Autowired
    UserMessageMapper userMessageMapper;
    @Autowired
    UserIntegralRecordMapper userIntegralRecordMapper;
    @Autowired
    WebConfig webConfig;
    @Autowired
    FileUtils fileUtils;

    /**
     * 用户注册
     *
     * @param email
     * @param emailCode
     * @param nickName
     * @param password
     * @param checkCode
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(String email, String emailCode, String nickName, String password, String checkCode) {
        UserInfo userInfo = userInfoMapper.selectByEmail(email);
        if (userInfo != null) {
            throw new BusinessException("邮箱账号已存在");
        }
        userInfo = userInfoMapper.selectByNickName(nickName);
        if (userInfo != null) {
            throw new BusinessException("昵称已存在");
        }
        emailCodeService.checkCode(email, emailCode); //校验邮箱验证码

        //封装注册实体类
        UserInfo insertUserInfo = new UserInfo();
        String userId = StringUtils.getRandomNumber(Constants.LENGTH_10);
        insertUserInfo.setUserId(userId);
        insertUserInfo.setEmail(email);
        insertUserInfo.setNickName(nickName);
        insertUserInfo.setPassword(StringUtils.encodeMd5(password));
        insertUserInfo.setJoinTime(new Date());
        insertUserInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
        insertUserInfo.setTotalIntegral(Constants.ZERO);
        insertUserInfo.setCurrentIntegral(Constants.ZERO);
        insertUserInfo.setSex(1);//默认男
        userInfoMapper.insert(insertUserInfo);

        //记录消息
        UserMessage userMessage = new UserMessage();
        userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
        userMessage.setReceivedUserId(userId);
        userMessage.setCreateTime(new Date());
        userMessage.setMessageContent(SysCacheUtils.getSysSetting().getRegisterSetting().getRegisterWelcomInfo());
        userMessage.setMessageType(MessageTypeEnum.SYS.getType());
        userMessageMapper.insert(userMessage);

        //更新用户积分
        updateUserIntegral(userId, UserIntegralOperTypeEnum.REGISTER, UserIntegralChangeTypeEnum.ADD.getChangeType(), Constants.INTEGRAL_5);
    }


    /**
     * 更新用户积分
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserIntegral(String userId, UserIntegralOperTypeEnum operTypeEnum, Integer changeType, Integer integral) {
        integral = changeType * integral;
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(Long.valueOf(userId));
        // 最多减分至0,不能减成负数
        if (UserIntegralChangeTypeEnum.REDUCE.getChangeType().equals(changeType) && integral + userInfo.getCurrentIntegral() < 0) {
            integral = changeType * userInfo.getCurrentIntegral();
        }
        UserIntegralRecord record = new UserIntegralRecord();
        record.setUserId(userId);
        record.setIntegral(integral);
        record.setCreateTime(new Date());
        record.setOperType(operTypeEnum.getOperType());
        userIntegralRecordMapper.insert(record);

        Integer count = userInfoMapper.updateIntegral(userId, integral);
        if (count == 0) {
            throw new BusinessException("更新用户积分失败");
        }
    }

    @Override
    public SessionWebUserDto login(String email, String password, String ip) {
        UserInfo userInfo = userInfoMapper.selectByEmail(email);
        if (null == userInfo || !userInfo.getPassword().equals(password)) {
            throw new BusinessException("账号或密码错误");
        }
        if (userInfo.getStatus().equals(UserStatusEnum.DISABLE.getStatus())) {
            throw new BusinessException("账号已禁用");
        }
        String ipAddress = getIpAddress(ip);
        UserInfo updateInfo = new UserInfo();
        BeanUtils.copyProperties(userInfo, updateInfo);
        updateInfo.setLastLoginTime(new Date());
        updateInfo.setLastLoginIp(ip);
        updateInfo.setLastLoginIpAddress(ipAddress);
        userInfoMapper.updateByPrimaryKey(updateInfo);

        SessionWebUserDto sessionWebUserDto = new SessionWebUserDto();
        sessionWebUserDto.setUserId(userInfo.getUserId());
        sessionWebUserDto.setNickName(userInfo.getNickName());
        sessionWebUserDto.setProvince(ipAddress);
        if (!StringUtils.isEmpty(webConfig.getAdminEmail()) && ArrayUtils.contains(webConfig.getAdminEmail().split(","), userInfo.getEmail())) {
            sessionWebUserDto.setIsAdmin(true);
        } else {
            sessionWebUserDto.setIsAdmin(false);
        }
        return sessionWebUserDto;
    }

    /**
     * 获取ip地址所属地
     * 发送网络请求，调用接口获取
     *
     * @param ip
     * @return
     */
    @Override
    public String getIpAddress(String ip) {
        try {
            String url = "https://whois.pconline.com.cn/ipJson.jsp?json=true&ip=" + ip;
            String responseJson = OKHttpUtils.getRequest(url);
            if (null == responseJson) {
                return Constants.NO_ADDRESS;
            } else {
                Map<String, String> map = JsonUtils.convertJson2Object(responseJson, Map.class);
                return map.get("pro");
            }
        } catch (Exception e) {
            logger.error("获取IP地址失败", e);
        }
        return Constants.NO_ADDRESS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPwd(String email, String password, String emailCode) {
        UserInfo userInfo = userInfoMapper.selectByEmail(email);
        if (userInfo == null) {
            throw new BusinessException("邮箱不存在");
        }
        emailCodeService.checkCode(email, emailCode);
        UserInfo updateInfo = new UserInfo();
        BeanUtils.copyProperties(userInfo, updateInfo);
        updateInfo.setPassword(StringUtils.encodeMd5(password));
        userInfoMapper.updateByPrimaryKey(updateInfo);
    }

    @Override
    public UserInfo getUserInfoByUserId(String userId) {
        return userInfoMapper.selectByPrimaryKey(Long.valueOf(userId));
    }

    @Override
    public void update(UserInfo userInfo, MultipartFile avatar) {
        //更新基本信息
        userInfoMapper.updateByPrimaryKeySelective(userInfo);
        //更新头像
        if (avatar != null) {
            fileUtils.uploadFile2Local(avatar, userInfo.getUserId(), FileUploadTypeEnum.AVATAR);
        }
    }

    @Override
    public Object getUserByParam(UserInfoQuery query) {
        PageHelper.startPage(query.getPageNo() == null ? 1 : query.getPageNo(), query.getPageSize() == null ? PageSize.SIZE50.getSize() : query.getPageSize());
        List<UserInfo> list = userInfoMapper.getUserByParam(query);
        PaginationResultVO<UserInfo> resultVO = new PaginationResultVO<>();
        resultVO.setList(list);
        PageInfo<UserInfo> pageInfo = new PageInfo<>(list);
        resultVO.setPageSize(pageInfo.getPageSize());
        resultVO.setPageTotal(pageInfo.getPages());
        resultVO.setTotalCount((int) pageInfo.getTotal());
        resultVO.setPageNo(pageInfo.getPageNum());
        return resultVO;
    }

    @Override
    public void updateUserByParam(UserInfoQuery query) {
        userInfoMapper.updateByParam(query);
    }

    @Override
    public List<UserMsgVO> getUserListById(List<String> senderIdList) {
        return userInfoMapper.getUserListById(senderIdList);
    }


}
