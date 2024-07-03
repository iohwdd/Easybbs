package com.easybbs.controller;

import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.service.ForumBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: iohw
 * @date: 2024/5/14 16:17
 * @description:
 */
@RestController
@RequestMapping("/board")
public class ForumBoardController extends ABaseController{
    @Autowired
    ForumBoardService forumBoardService;
    @RequestMapping("/loadBoard")
    public ResponseVO loadBoard() {
        return getSuccessResponseVo(forumBoardService.getBoardTree(null));
    }
}
