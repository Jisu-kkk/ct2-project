package com.example.ct2.controller;

import com.example.ct2.service.Common.CommonService;
import com.example.ct2.service.Common.FileService;
import com.example.ct2.service.admin.WikiService;
import com.example.ct2.vo.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Value("${resource.path}")
    private String resourcePath;

    @Autowired
    private WikiService wikiService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private FileService fileService;

    @GetMapping("/index")
    public String home() {
        return "admin/main/index";
    }

    @GetMapping("/wikiList")
    public String wikiList(@RequestParam(required = false) Map<String, Object> param,
                           @RequestParam(defaultValue = "1") int curPage,
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
    public String getAddWiki(Model model) {
        Map<String, Object> param = new HashMap<>();
        param.put("tagType", "BO002");
        List<Map<String, Object>> tagList = commonService.selectTagList(param);

        model.addAttribute("tagList", tagList);
        return "admin/wiki/wikiEdit";
    }

    @PostMapping("/addWiki")
    public String postAddWiki(@RequestParam Map<String, Object> param,
                              @RequestParam MultipartFile thumbnail,
                              @RequestParam MultipartFile titleImg,
                              HttpServletRequest request,
                              Model model) {
        // 사용여부
        String showStatus = (String) param.get("showStatus");
        if (showStatus != null) {
            param.put("showStatus", 1);
        }
        param.put("title_img", titleImg);
        param.put("thumbnail_img", thumbnail);

        int result = wikiService.insertWiki(param);

        return "redirect:/" + "admin/wikiList";
    }

    @GetMapping("/project")
    public String project(Model model) {
        return "admin/project/projectList";
    }

    @GetMapping("/addProject")
    public String addProject(Model model) {
        model.addAttribute("status", "add");
        return "admin/project/projectDetail";
    }

    @GetMapping("/editProject")
    public String editProject(Model model) {
        model.addAttribute("status", "edit");
        return "admin/project/projectDetail";
    }

    @GetMapping("/editWiki")
    public String getEditWiki(@RequestParam int wikiId,
                           Model model) {

        Map<String, Object> param = new HashMap<>();
        param.put("tagType", "BO002");

        Map<String, Object> wiki = wikiService.selectWiki(wikiId);
        List<Integer> wikiTagList = wikiService.selectWikiTagList(wikiId);
        String wikiTag = "";
        for (int i = 0; i < wikiTagList.size(); i++) {
            if (i != 0){
                wikiTag += ",";
            }
            wikiTag += wikiTagList.get(i);
        }

        // 해시태그 전체
        List<Map<String, Object>> tagList = commonService.selectTagList(param);

        model.addAttribute("wiki", wiki);
        model.addAttribute("wikiTagList", wikiTagList);
        model.addAttribute("wikiTag", wikiTag);
        model.addAttribute("tagList", tagList);
        return "admin/wiki/wikiEdit";
    }

    @PostMapping("/editWiki")
    public String postEditWiki(@RequestParam Map<String, Object> param,
                               @RequestParam MultipartFile thumbnail,
                               @RequestParam MultipartFile titleImg) {
        // 사용여부
        String showStatus = (String) param.get("showStatus");
        if (showStatus != null) {
            param.put("showStatus", 1);
        }

        param.put("title_img", titleImg);
        param.put("thumbnail_img", thumbnail);

        int result = wikiService.updateWiki(param);

        return "redirect:/" + "admin/wikiList";
    }

}
