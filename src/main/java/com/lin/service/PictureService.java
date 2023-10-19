package com.lin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.model.entity.Picture;

/**
 * 图片服务
 *
 */
public interface PictureService  {

    Page<Picture> search(String searchText, long pageNum, long pageSize);

}
