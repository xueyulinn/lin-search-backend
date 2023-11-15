package com.lin.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.model.entity.Picture;
import com.lin.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 图片服务实现
 */
@Service
@Slf4j
public class PictureDataSource implements DataSource<Picture> {

    @Resource
    private PictureService pictureService;

    @Override
    public Page<Picture> doSearch(String searchText, long pageNum, long pageSize) {
        return pictureService.search(searchText, pageNum, pageSize);
    }
}




