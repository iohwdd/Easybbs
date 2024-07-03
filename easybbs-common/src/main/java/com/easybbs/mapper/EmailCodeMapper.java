package com.easybbs.mapper;


import com.easybbs.config.WebConfig;
import com.easybbs.entity.po.EmailCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

/**
* @author Administrator
* @description 针对表【email_code(邮箱验证码)】的数据库操作Mapper
* @createDate 2024-05-09 16:56:17
* @Entity com.easybbs.entity.po.EmailCode
*/
//@Mapper
public interface EmailCodeMapper {

    int deleteByPrimaryKey(Long id);

    int insert(EmailCode record);

    int insertSelective(EmailCode record);

    EmailCode selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EmailCode record);

    int updateByPrimaryKey(EmailCode record);
    /**
     * 让邮箱验证码失效
     */
    void disableEmailCode(@Param("email")String email);

    /**
     * 通过邮箱+验证码联合主键 查找
     * @param email
     * @param emailCode
     * @return
     */
    EmailCode selectByEmailAndCode(@Param("email") String email, @Param("emailCode") String emailCode);
}
