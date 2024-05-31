package com.easybbs.service;

import com.easybbs.entity.po.ForumComment;
import com.easybbs.entity.query.ForumCommentQuery;
import com.easybbs.entity.vo.PaginationResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author: iohw
 * @date: 2024/5/16 20:32
 * @description:
 */
public interface ForumCommentService {


    PaginationResultVO findComment(ForumCommentQuery forumCommentQuery);

    ForumComment getByCommentId(String commentId);

    void changeTopType(String userId, String commentId, Integer topType);

    List<ForumComment> findListByParam(ForumCommentQuery query);

    void postComment(ForumComment forumComment, MultipartFile image);

    void delByCommentIds(String[] commentIds);

    void updateByParam(ForumCommentQuery query);
}
