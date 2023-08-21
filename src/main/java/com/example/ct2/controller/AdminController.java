package com.example.ct2.controller;

import com.example.ct2.service.Common.CommonService;
import com.example.ct2.service.admin.WikiService;
import com.example.ct2.vo.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private WikiService wikiService;

    @Autowired
    private CommonService commonService;

    @GetMapping("/wikiList")
    public String wikiList(@RequestParam(required = false) Map<String, Object> param,
                           @RequestParam(defaultValue = "1") int curPage,
                           HttpServletRequest request,
                           Model model) {

        param.put("tagType", "BO002");
        param.put("orgCode", "BS003002");

        // 해시태그 전체
        List<Map<String, Object>> tagList = commonService.selectTagList(param);
        // 해당 부서 사람 전체
        List<Map<String, Object>> orgUserList = commonService.selectOrgUserList(param);


        int totalCnt = wikiService.selectWikiListCnt(param);
        Pagination pagination = new Pagination(totalCnt, curPage);

        param.put("pagination", pagination);

        List<Map<String, Object>> selectWikiList = (List<Map<String, Object>>) wikiService.selectWikiList(param);

        model.addAttribute("param", param);
        model.addAttribute("tagList", tagList);
        model.addAttribute("orgUserList", orgUserList);
        model.addAttribute("pagination", pagination);
        model.addAttribute("selectWikiList", selectWikiList);
        return "admin/wiki/wikiList";
    }

    @GetMapping("/addWiki")
    public String addWiki(Model model) {
        return "admin/wiki/wikiEdit";
    }

}