package com.lin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.common.BaseResponse;
import com.lin.common.ErrorCode;
import com.lin.common.ResultUtils;
import com.lin.datasource.*;
import com.lin.exception.ThrowUtils;
import com.lin.model.dto.post.PostQueryRequest;
import com.lin.model.dto.search.SearchQueryRequest;
import com.lin.model.dto.user.UserQueryRequest;
import com.lin.model.entity.Picture;
import com.lin.model.vo.PostVO;
import com.lin.model.vo.SearchVO;
import com.lin.model.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.internal.StringUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 薛坤
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/search")
public class SearchController {

    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private PostDataSource postDataSource;

    @Resource
    private UserDataSource userDataSource;

    @Resource
    private DataSourceRegistry dataSourceRegistry;

    @PostMapping("/all")
    public BaseResponse<SearchVO> listPostVOByPage(@RequestBody SearchQueryRequest searchQueryRequest,
                                                   HttpServletRequest request) {

        String type = searchQueryRequest.getType();
        String searchText = searchQueryRequest.getSearchText();
        long pageNum = searchQueryRequest.getPage();
        long pageSize = searchQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR);

        Page<PostVO> postPage;

        Page<Picture> picturePage;

        Page<UserVO> userPage;

        if (StringUtil.isBlank(type)) {
            //查询全部
            // 获取文章数据
            PostQueryRequest postQueryRequest = new PostQueryRequest();
            postQueryRequest.setSearchText(searchText);
            postPage = postDataSource.doSearch(searchText, pageNum, pageSize);

            //获取图片数据
            picturePage = pictureDataSource.doSearch(searchText, pageNum, pageSize);

            //获取用户数据
            UserQueryRequest userQueryRequest = new UserQueryRequest();
            userQueryRequest.setUserName(searchText);
            userPage = userDataSource.doSearch(searchText, pageNum, pageSize);

            SearchVO searchVO = new SearchVO();
            searchVO.setPostList(postPage);
            searchVO.setPictureList(picturePage);
            searchVO.setUserList(userPage);
            return ResultUtils.success(searchVO);

        } else {
            dataSourceRegistry.doInit();
            DataSource dataSourceType = dataSourceRegistry.getDataSourceType(type);
            Page page = dataSourceType.doSearch(searchText, pageNum, pageSize);
            SearchVO searchVO = new SearchVO();
            searchVO.setPage(page);
            return ResultUtils.success(searchVO);
        }
    }
}
