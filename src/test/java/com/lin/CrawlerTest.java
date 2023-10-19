package com.lin;

import cn.hutool.core.lang.Assert;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lin.model.entity.Picture;
import com.lin.model.entity.Post;
import com.lin.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 薛坤
 * @version 1.0
 */
@SpringBootTest
@Slf4j
public class CrawlerTest {
    @Autowired
    private PostService postService;

    //爬取图片
    @Test
    void testPicture() throws IOException {
        int offset = 1;
        String url = "https://www.bing.com/images/search?q=%E8%B7%AF%E9%A3%9E&first=" + 1;
        Document doc = Jsoup.connect(url).get();
        System.out.println(doc);
        Elements elements = doc.select(".iuscp.isv");
        List<Picture> pictureList = new ArrayList<>();
        for (Element element : elements) {
            System.out.println(element);
            //获取图片地址
            String m = element.select(".iusc").get(0).attr("m");

            Map map = JSONUtil.toBean(m, Map.class);

            String turl = (String) map.get("turl");


            //获取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");

            //封装对象
            Picture picture = new Picture();
            picture.setTitle(title);
            picture.setUrl(turl);
            pictureList.add(picture);
            // System.out.println(turl);
            // System.out.println(title);
        }
    }

    //爬取文章
    @Test
    void testPassage() {
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
        Map map = JSONUtil.toBean(result2, Map.class);
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
