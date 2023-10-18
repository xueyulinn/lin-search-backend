package com.lin.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lin.model.entity.Post;
import com.lin.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 薛坤
 * @version 1.0
 */
@Component
@Slf4j
public class FetchInitPostList implements CommandLineRunner {

    @Autowired
    private PostService postService;

    @Override
    public void run(String... args) throws Exception {
        //获取数据
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
        if (b) {
            log.info("获取初始化帖子列表成功,条数{}", postList.size());
        } else {
            log.info("获取初始化帖子列表失败");
        }
    }
}
