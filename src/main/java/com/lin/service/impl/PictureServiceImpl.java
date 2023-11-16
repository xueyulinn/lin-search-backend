package com.lin.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.model.entity.Picture;
import com.lin.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 图片服务实现
 */
@Service
@Slf4j
public class PictureServiceImpl implements PictureService {

    @Override
    public Page<Picture> search(String searchText, long pageNum, long pageSize) {
        long current = (pageNum - 1) * pageSize;

        String url = String.format("https://www.bing.com/images/search?q=%s&first=%s", searchText, current);

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(doc);

        Elements elements = doc.select(".iuscp.isv");
        List<Picture> pictureList = new ArrayList<>();
        for (Element element : elements) {

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

            if (pictureList.size() >= pageSize) {
                break;
            }
        }

        Page<Picture> page = new Page<>();
        page.setRecords(pictureList);
        return page;
    }
}




