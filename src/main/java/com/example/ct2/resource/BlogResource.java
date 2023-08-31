package com.example.ct2.resource;

import com.example.ct2.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/main/blogList")
public class BlogResource {

    @Autowired
    private BlogService blogService;

    @ResponseBody
    @PostMapping("/selectBlogListData")
    public Map<String, Object> blogList(@RequestBody Map<String, Object> param) {
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> blogList = new ArrayList<>();

        int endPage = blogService.selectBlogListEndPage(param);
        int currentPage = (int) param.get("currentPage") + 1;

        int startPost = 0;
        if (endPage >= currentPage) {
            startPost = (int) param.get("postCnt") * (int) param.get("currentPage");
        }
        param.put("startPost", startPost);

        blogList = blogService.selectBlogList(param);

        result.put("blogList", blogList);
        result.put("endPage", endPage);
        return result;
    }

}
