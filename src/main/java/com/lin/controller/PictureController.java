package com.lin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.common.BaseResponse;
import com.lin.common.ErrorCode;
import com.lin.common.ResultUtils;
import com.lin.exception.ThrowUtils;
import com.lin.model.dto.picture.PictureQueryRequest;
import com.lin.model.entity.Picture;
import com.lin.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子接口
 *
 */
@RestController
@RequestMapping("/picture")
@Slf4j
public class PictureController {

    @Resource
    private PictureService pictureService;


    /**
     * 分页获取列表（封装类）
     *
     * @param pictureQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Picture>> listPostVOByPage(@RequestBody PictureQueryRequest pictureQueryRequest,
                                                        HttpServletRequest request) {
        long pageNum = pictureQueryRequest.getCurrent();
        long pageSize = pictureQueryRequest.getPageSize();
        String searchText = pictureQueryRequest.getSearchText();
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR);

        return ResultUtils.success(pictureService.search(searchText,pageNum,pageSize));
    }

}
