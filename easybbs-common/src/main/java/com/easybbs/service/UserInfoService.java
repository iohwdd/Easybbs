package com.easybbs.service;

import com.easybbs.entity.dto.SessionWebUserDto;
import com.easybbs.entity.enums.UserIntegralChangeTypeEnum;
import com.easybbs.entity.enums.UserIntegralOperTypeEnum;
import com.easybbs.entity.po.UserInfo;
import com.easybbs.entity.query.UserInfoQuery;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.entity.vo.web.UserMsgVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author: iohw
 * @date: 2024/5/10 15:02
 * @description:
 */
@Service
public interface UserInfoService {
    void register(String email, String emailCode, String nickName, String password, String checkCode);

    void updateUserIntegral(String userId, UserIntegralOperTypeEnum operTypeEnum, Integer changeType, Integer integral);


    SessionWebUserDto login(String email, String password, String ip);

    String getIpAddress(String ip);

    void resetPwd(String email, String password, String emailCode);

    UserInfo getUserInfoByUserId(String userId);

    void update(UserInfo userInfo, MultipartFile avatar);

    Object getUserByParam(UserInfoQuery query);

    void updateUserByParam(UserInfoQuery query);

    List<UserMsgVO> getUserListById(List<String> senderIdList);
}
