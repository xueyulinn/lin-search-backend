package com.lin.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.model.dto.post.PostQueryRequest;
import com.lin.model.vo.PostVO;
import com.lin.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 帖子服务实现
 */
@Service
@Slf4j
public class PostDataSource implements DataSource<PostVO> {

    @Resource
    private PostService postService;

    @Override
    public Page<PostVO> doSearch(String searchText, long pageNum, long pageSize) {
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setSearchText(searchText);
        postQueryRequest.setCurrent(pageNum);
        postQueryRequest.setPageSize(pageSize);
        return postService.listPostVOByPage(postQueryRequest, pageNum, pageSize, null);
    }
}




