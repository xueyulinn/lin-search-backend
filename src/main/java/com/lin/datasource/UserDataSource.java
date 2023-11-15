package com.lin.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.model.dto.user.UserQueryRequest;
import com.lin.model.vo.UserVO;
import com.lin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户服务实现
 */
@Service
@Slf4j
public class UserDataSource implements DataSource<UserVO> {

    @Resource
    private UserService userService;

    @Override
    public Page<UserVO> doSearch(String searchText, long pageNum, long pageSize) {
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        userQueryRequest.setCurrent(pageNum);
        userQueryRequest.setPageSize(pageSize);
        return userService.listUserVOByPage(userQueryRequest, pageNum, pageSize, null);
    }
}
