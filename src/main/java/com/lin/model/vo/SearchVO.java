package com.lin.model.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.model.entity.Picture;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 薛坤
 * @version 1.0
 */
@Data
public class SearchVO implements Serializable {

    private Page<PostVO> postList;

    private Page<Picture> pictureList;

    private Page<UserVO> userList;

    private Page<Object> page;

    private static final long serialVersionUID = 1L;
}
