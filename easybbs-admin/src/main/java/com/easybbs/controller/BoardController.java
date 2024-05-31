package com.easybbs.controller;

import com.easybbs.annotation.GlobalInterceptor;
import com.easybbs.annotation.VerifyParam;
import com.easybbs.entity.po.ForumBoard;
import com.easybbs.entity.query.ForumBoardQuery;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.service.ForumBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: iohw
 * @date: 2024/5/28 16:21
 * @description:
 */
@RestController
@RequestMapping("/board")
public class BoardController extends ABaseController {
    @Autowired
    ForumBoardService forumBoardService;

    @RequestMapping("/loadBoard")
    public ResponseVO loadBoard() {
        // 查询所有板块
        return getSuccessResponseVo(forumBoardService.getBoardTree(null));
    }

    @RequestMapping("/saveBoard")
    public ResponseVO saveBoard(ForumBoardQuery query, MultipartFile cover) {
        ForumBoard board = forumBoardService.updateByParam(query, cover);
        return getSuccessResponseVo(board);
    }

    @RequestMapping("/delBoard")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO delBoard(@VerifyParam(required = true) String boardId) {
        forumBoardService.delByBoardId(boardId);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/changeBoardSort")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO changeBoardSort(@VerifyParam(required = true) String boardIds) {
        String[] ids = boardIds.split(",");
        HashMap<String, Integer> map = new HashMap<>();
        for (int i = 1; i <= ids.length; i++) {
            map.put(ids[i - 1], i);
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            ForumBoardQuery query = new ForumBoardQuery();
            query.setBoardId(entry.getKey());
            query.setSort(entry.getValue());
            forumBoardService.updateByParam(query, null);
        }
        return getSuccessResponseVo(null);
    }
}
