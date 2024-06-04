package com.easybbs.service.impl;

import com.easybbs.entity.enums.PageSize;
import com.easybbs.entity.enums.UserIntegralOperTypeEnum;
import com.easybbs.entity.po.UserIntegralRecord;
import com.easybbs.entity.query.UserIntegralRecordQuery;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.vo.web.UserIntegralRecordVO;
import com.easybbs.mapper.UserIntegralRecordMapper;
import com.easybbs.service.UserIntegralRecordService;
import com.easybbs.utils.CopyTools;
import com.easybbs.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: iohw
 * @date: 2024/5/23 21:21
 * @description:
 */
@Service
public class UserIntegralRecordServiceImpl implements UserIntegralRecordService {
    @Autowired
    UserIntegralRecordMapper userIntegralRecordMapper;

    @Override
    public PaginationResultVO findByParam(Integer pageNo, UserIntegralRecordQuery query) {
        pageNo = pageNo == null ? 1 : pageNo;
        PageHelper.startPage(pageNo, PageSize.SIZE15.getSize());
        List<UserIntegralRecord> list = userIntegralRecordMapper.findByParam(query);
        List<UserIntegralRecordVO> recordVOList = list.stream().map(item -> {
            UserIntegralRecordVO integralRecordVO = CopyTools.copy(item, UserIntegralRecordVO.class);
            String desc = UserIntegralOperTypeEnum.getByType(item.getOperType()) == null ? "下载附件" : UserIntegralOperTypeEnum.getByType(item.getOperType()).getDesc();
            integralRecordVO.setOperTypeName(desc);
            return integralRecordVO;
        }).collect(Collectors.toList());
        PaginationResultVO resultVO = new PaginationResultVO<>();
        PageInfo<UserIntegralRecord> pageInfo = new PageInfo<>(list);
        resultVO.setPageTotal(pageInfo.getPages());
        resultVO.setTotalCount((int) pageInfo.getTotal());
        resultVO.setPageNo(pageNo);
        resultVO.setPageSize(pageInfo.getPageSize());
        resultVO.setList(recordVOList);
        return resultVO;
    }
}
