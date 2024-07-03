package com.easybbs.mapper;


import com.easybbs.entity.po.UserInfo;
import com.easybbs.entity.query.UserInfoQuery;
import com.easybbs.entity.vo.web.UserMsgVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【user_info(用户信息)】的数据库操作Mapper
 * @createDate 2024-05-09 16:56:17
 * @Entity com.easybbs.entity.po.UserInfo
 */
//@Mapper
public interface UserInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    /**
     * 根据邮箱查找用户
     *
     * @param email
     * @return
     */
    UserInfo selectByEmail(@Param("email") String email);

    /**
     * 根据昵称查找用户
     *
     * @param nickName
     * @return
     */
    UserInfo selectByNickName(@Param("nickName") String nickName);

    /**
     * 更新用户积分
     */
    Integer updateIntegral(@Param("userId") String userId, @Param("integral") Integer integral);


    List<UserInfo> getUserByParam(@Param("query") UserInfoQuery query);

    void updateByParam(@Param("query") UserInfoQuery query);

    List<UserMsgVO> getUserListById(@Param("senderIdList") List<String> senderIdList);

}
