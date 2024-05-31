package com.easybbs.service;

import com.easybbs.entity.po.ForumBoard;
import com.easybbs.entity.query.ForumBoardQuery;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author: iohw
 * @date: 2024/5/14 16:17
 * @description:
 */
public interface ForumBoardService {
    List<ForumBoard> getBoardTree(Integer postType);

    ForumBoard updateByParam(ForumBoardQuery query, MultipartFile file);

    void delByBoardId(String boardId);
}
