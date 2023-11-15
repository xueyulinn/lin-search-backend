package com.lin.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author 薛坤
 * @version 1.0
 */
public interface DataSource<T> {

    Page<T> doSearch(String searchText, long pageNum, long pageSize);
}
