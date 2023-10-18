package com.lin;

import cn.hutool.core.lang.Assert;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lin.model.entity.Post;
import com.lin.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 薛坤
 * @version 1.0
 */
@SpringBootTest
public class CrawlerTest {
    @Autowired
    private PostService postService;

    @Test
    void test() {
        String json = ("{\n" +
                "  \"current\": 1,\n" +
                "  \"pageSize\": 8,\n" +
                "  \"sortField\": \"createTime\",\n" +
                "  \"sortOrder\": \"descend\",\n" +
                "  \"category\": \"文章\",\n" +
                "  \"reviewStatus\": 1\n" +
                "}");
        String url = "https://www.code-nav.cn/api/post/search/page/vo";
        String result2 = HttpRequest.post(url)
                .body(json)
                .execute().body();

        System.out.println(result2);


        //转为JSON对象
        Map<String, Object> map = JSONUtil.toBean(result2, Map.class);
        JSONObject data = (JSONObject) map.get("data");
        JSONArray records = (JSONArray) data.get("records");
        List<Post> postList = new ArrayList<>();
        for (Object record : records) {
            JSONObject tempRecord = (JSONObject) record;
            Post post = new Post();
            post.setTitle(tempRecord.getStr("title"));
            post.setContent(tempRecord.getStr("content"));
            JSONArray tags = (JSONArray) tempRecord.get("tags");
            List<String> tagList = tags.toList(String.class);
            post.setTags(JSONUtil.toJsonStr(tagList));
            post.setUserId(1L);
            postList.add(post);
        }

        // 数据入库
        boolean b = postService.saveBatch(postList);
        Assert.isTrue(b);

    }
}
