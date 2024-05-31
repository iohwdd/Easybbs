package com.easybbs.controller;

import com.easybbs.entity.enums.ResponseCodeEnum;
import com.easybbs.entity.vo.PaginationResultVO;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.utils.CopyTools;

/**
 * @author: iohw
 * @date: 2024/5/8 21:52
 * @description:
 */
public class ABaseController{
    protected static final String STATUS_SUCCESS ="success";
    protected static final String STATUS_ERROR= "error";
    protected <T> ResponseVO getSuccessResponseVo(T t){
        ResponseVO<T> responseVO = new ResponseVO<>();
        responseVO.setStatus(STATUS_SUCCESS);
        responseVO.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVO.setInfo(ResponseCodeEnum.CODE_200.getMsg());
        responseVO.setData(t);
        return responseVO;
    }
    protected <T> ResponseVO getBusinessErrorResponseVo(BusinessException e, T t){
        ResponseVO vo = new ResponseVO();
        vo.setStatus(STATUS_ERROR);
        if(e.getCode() == null){
            vo.setCode(ResponseCodeEnum.CODE_600.getCode());
        }else{
            vo.setCode(e.getCode());
        }
        vo.setInfo(e.getMessage());
        vo.setData(t);
        return vo;
    }
    protected <T> ResponseVO getServerErrorResponseVO(T t){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(STATUS_ERROR);
        responseVO.setCode(ResponseCodeEnum.CODE_500.getCode());
        responseVO.setInfo(ResponseCodeEnum.CODE_500.getMsg());
        responseVO.setData(t);
        return responseVO;
    }

    protected <S,T> PaginationResultVO<T> convert2PaginationVO(PaginationResultVO<S> result, Class<T> classz) {
        PaginationResultVO<T> resultVO = new PaginationResultVO<>();
        resultVO.setList(CopyTools.copyList(result.getList(), classz));
        resultVO.setPageNo(result.getPageNo());
        resultVO.setPageSize(result.getPageSize());
        resultVO.setPageTotal(result.getPageTotal());
        resultVO.setTotalCount(result.getTotalCount());
        return resultVO;
    }
}
