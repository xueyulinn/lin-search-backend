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
import com.lin.model.enums.SearchTypeEnum;
import com.lin.model.vo.PostVO;
import com.lin.model.vo.SearchVO;
import com.lin.model.vo.UserVO;
import com.lin.service.PictureService;
import com.lin.service.PostService;
import com.lin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

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

        String type = searchQueryRequest.getType();
        String searchText = searchQueryRequest.getSearchText();
        long current = searchQueryRequest.getCurrent();
        long pageSize = searchQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR);

        Page<PostVO> postPage = null;

        Page<Picture> picturePage = null;

        Page<UserVO> userPage = null;

        if (StringUtil.isBlank(type)) {

            //查询全部
            // 获取文章数据
            PostQueryRequest postQueryRequest = new PostQueryRequest();
            postQueryRequest.setSearchText(searchText);
            postPage = postService.listPostVOByPage(postQueryRequest, current, pageSize, request);

            //获取图片数据
            picturePage = pictureService.search(searchText, current, pageSize);

            //获取用户数据
            UserQueryRequest userQueryRequest = new UserQueryRequest();
            userQueryRequest.setUserName(searchText);
            userPage = userService.listUserVOByPage(userQueryRequest, current, pageSize, request);

        } else {
            switch (Objects.requireNonNull(SearchTypeEnum.getEnumByValue(type))) {
                case POST:
                    // 获取文章数据
                    PostQueryRequest postQueryRequest = new PostQueryRequest();
                    postQueryRequest.setSearchText(searchText);
                    postPage = postService.listPostVOByPage(postQueryRequest, current, pageSize, request);
                case PICTURE:
                    //获取图片数据
                    picturePage = pictureService.search(searchText, current, pageSize);
                case USER:
                    //获取用户数据
                    UserQueryRequest userQueryRequest = new UserQueryRequest();
                    userQueryRequest.setUserName(searchText);
                    userPage = userService.listUserVOByPage(userQueryRequest, current, pageSize, request);
                default:
            }
        }

        SearchVO searchVO = new SearchVO();
        searchVO.setPostList(postPage);
        searchVO.setPictureList(picturePage);
        searchVO.setUserList(userPage);
        return ResultUtils.success(searchVO);
    }
}
