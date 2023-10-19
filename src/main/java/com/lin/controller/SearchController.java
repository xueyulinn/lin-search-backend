package com.lin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.common.BaseResponse;
import com.lin.common.ErrorCode;
import com.lin.common.ResultUtils;
import com.lin.exception.ThrowUtils;
import com.lin.model.dto.post.PostQueryRequest;
import com.lin.model.dto.search.SearchQueryRequest;
import com.lin.model.dto.user.UserQueryRequest;
import com.lin.model.entity.Picture;
import com.lin.model.vo.PostVO;
import com.lin.model.vo.SearchVO;
import com.lin.model.vo.UserVO;
import com.lin.service.PictureService;
import com.lin.service.PostService;
import com.lin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 薛坤
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private PostService postService;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private UserService userService;


    @PostMapping("/all")
    public BaseResponse<SearchVO> listPostVOByPage(@RequestBody SearchQueryRequest searchQueryRequest,
                                                   HttpServletRequest request) {

        String searchText = searchQueryRequest.getSearchText();
        long current = searchQueryRequest.getCurrent();
        long pageSize = searchQueryRequest.getPageSize();

        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR);

        // 获取文章数据
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setSearchText(searchText);
        Page<PostVO> postPage = postService.listPostVOByPage(postQueryRequest, current, pageSize,request);

        //获取图片数据
        Page<Picture> picturePage = pictureService.search(searchText, current, pageSize);

        //获取用户数据
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        Page<UserVO> userPage = userService.listUserVOByPage(userQueryRequest, current, pageSize, request);


        SearchVO searchVO = new SearchVO();
        searchVO.setPostList(postPage);
        searchVO.setPictureList(picturePage);
        searchVO.setUserList(userPage);

        return ResultUtils.success(searchVO);
    }
}
