package com.easybbs.service.impl;

import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.enums.FileUploadTypeEnum;
import com.easybbs.entity.po.FileUploadDto;
import com.easybbs.entity.po.ForumBoard;
import com.easybbs.entity.query.ForumBoardQuery;
import com.easybbs.mapper.ForumBoardMapper;
import com.easybbs.service.ForumBoardService;
import com.easybbs.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: iohw
 * @date: 2024/5/14 16:18
 * @description:
 */
@Service
public class ForumBoardServiceImpl implements ForumBoardService {
    @Autowired
    ForumBoardMapper forumBoardMapper;
    @Autowired
    FileUtils fileUtils;

    @Override
    public List<ForumBoard> getBoardTree(Integer postType) {
        List<ForumBoard> forumBoardList = forumBoardMapper.selectOrderListWithType(postType);
        return convertLine2Tree(forumBoardList, 0);
    }

    @Override
    public ForumBoard updateByParam(ForumBoardQuery query, MultipartFile file) {
        FileUploadDto fileUploadDto = null;
        if (file != null) {
            fileUploadDto = fileUtils.uploadFile2Local(file, Constants.FILE_FOLDER_IMAGE, FileUploadTypeEnum.ARTICLE_COVER);
        }
        if (query.getBoardId() != null) {
            //修改板块
            if (fileUploadDto != null) {
                query.setCoverPath(fileUploadDto.getLocalPath());
            }
            forumBoardMapper.updateByParam(query);
        } else {
            //新增二级板块
            ForumBoard board = new ForumBoard();
            if (fileUploadDto != null) {
                board.setCover(fileUploadDto.getLocalPath());
            }
            board.setBoardDesc(query.getBoardDesc());
            board.setBoardName(query.getBoardName());
            board.setPostType(query.getPostType());
            board.setpBoardId(Integer.valueOf(query.getpBoardId()));
            board.setpBoardName(query.getpBoardName());
            board.setSort(1);
            forumBoardMapper.insert(board);
        }

        return null;
    }

    @Override
    public void delByBoardId(String boardId) {
        ForumBoard board = forumBoardMapper.selectByPrimaryKey(Long.valueOf(boardId));
        if (board.getpBoardId() == 0) {
            //当前为删除对象为父板块 -> 删除子版块
            forumBoardMapper.delChildBypBoardId(boardId);
        }
        forumBoardMapper.deleteByPrimaryKey(Long.valueOf(boardId));
    }

    private List<ForumBoard> convertLine2Tree(List<ForumBoard> dataList, Integer pid) {
        List<ForumBoard> children = new ArrayList<>();
        for (ForumBoard m : dataList) {
            if (m.getpBoardId().equals(pid)) {
                m.setChildren(convertLine2Tree(dataList, m.getBoardId()));
                children.add(m);
            }
        }
        return children;
    }
}
