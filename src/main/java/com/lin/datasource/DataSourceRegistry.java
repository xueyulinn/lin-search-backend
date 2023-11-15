package com.lin.datasource;

import com.lin.model.enums.SearchTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 薛坤
 * @version 1.0
 */
@Component
public class DataSourceRegistry {

    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private PostDataSource postDataSource;

    @Resource
    private UserDataSource userDataSource;

    private Map<String, DataSource> typeDataSource;


    @PostConstruct
    public void doInit() {
        typeDataSource = new HashMap() {{
            put(SearchTypeEnum.POST.getValue(), postDataSource);
            put(SearchTypeEnum.PICTURE.getValue(), pictureDataSource);
            put(SearchTypeEnum.USER.getValue(), userDataSource);
        }};
    }

    public DataSource getDataSourceType(String type) {
        if (typeDataSource == null) {
            return null;
        }

        return typeDataSource.get(type);
    }
}
