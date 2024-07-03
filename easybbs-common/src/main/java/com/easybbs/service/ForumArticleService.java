package com.easybbs.service;

import com.easybbs.entity.po.ForumArticle;
import com.easybbs.entity.po.ForumArticleAttachment;
import com.easybbs.entity.query.ForumArticleQuery;
import com.easybbs.entity.vo.PaginationResultVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: iohw
 * @date: 2024/5/14 19:16
 * @description:
 */
public interface ForumArticleService {
    PaginationResultVO findListByPage(Integer boardId, Integer pBoardId, Integer orderType, Integer pageNo, String currentUserId);

    ForumArticle readArticle(String article);

    void postArticle(Boolean isAdmin, ForumArticle forumArticle, ForumArticleAttachment forumArticleAttachment, MultipartFile cover, MultipartFile attachment);

    void updateArticle(Boolean isAdmin, ForumArticle forumArticle, ForumArticleAttachment forumArticleAttachment, MultipartFile cover, MultipartFile attachment);

    int findCountByParam(ForumArticleQuery query);

    PaginationResultVO findListByParam(ForumArticleQuery query, Integer pageNo);

    PaginationResultVO search(Integer pageNo, String keyword);

    void delByArticleId(@Param("ids") String[] ids);

    void updateArticleByParam(ForumArticleQuery query);

    void delComment(String userId, String commendIds);
}
