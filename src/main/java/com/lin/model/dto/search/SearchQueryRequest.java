package com.lin.model.dto.search;

import com.lin.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author 薛坤
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchQueryRequest extends PageRequest implements Serializable {

    /**
     * 搜索类型
     */

    private String type;

    /**
     * 搜索词
     */
    private String searchText;


    private static final long serialVersionUID = 1L;
}
