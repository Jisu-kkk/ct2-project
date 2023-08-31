package com.example.ct2.controller;

import com.example.ct2.service.Common.CommonService;
import com.example.ct2.service.Common.FileService;
import com.example.ct2.service.admin.IntroMngService;
import com.example.ct2.service.admin.ProjectMngService;
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
    private IntroMngService introMngService;

    @Autowired
    private ProjectMngService projectMngService;

    @Autowired
    private WikiService wikiService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private FileService fileService;

    @GetMapping("/login")
    public String login(Model model) {
        return "admin/login/login";
    }

    @GetMapping("/index")
    public String home(Model model, Map<String, Object> param) {
        /*TODO_받아와야하는 부분*/
        param.put("orgCode", "BS003002");

        /*Wiki Count*/
        int wikiShowCnt = commonService.selectWikiListCnt(param, "1");
        int wikiHideCnt = commonService.selectWikiListCnt(param, "0");

        /*프로젝트 Count*/
        int projectShowCnt = commonService.selectProjectListCnt(param, "1");
        int projectHideCnt = commonService.selectProjectListCnt(param, "0");

        /*본부소개*/
        List<Map<String, Object>> introList = introMngService.selectIntroList(param);

        model.addAttribute("wikiShowCnt", wikiShowCnt);
        model.addAttribute("wikiHideCnt", wikiHideCnt);
        model.addAttribute("projectShowCnt", projectShowCnt);
        model.addAttribute("projectHideCnt", projectHideCnt);
        model.addAttribute("introList", introList);

        return "admin/main/index";
    }

    @GetMapping("/intro")
    public String intro(Model model, Map<String, Object> param) {
        /*TODO_받아와야하는 부분*/
        param.put("orgCode", "BS003002");

        /*본부소개*/
        List<Map<String, Object>> introList = introMngService.selectIntroList(param);

        model.addAttribute("introList", introList);

        return "admin/intro/intro";
    }

    @GetMapping("/addIntro")
    public String addIntro(Model model) {
        model.addAttribute("status", "add");
        return "admin/intro/introDetail";
    }

    @GetMapping("/editIntro")
    public String editIntro(Model model) {
        model.addAttribute("status", "edit");
        return "admin/intro/introDetail";
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
    public String project(@RequestParam(required = false) Map<String, Object> param,
                          @RequestParam(defaultValue = "1") int curPage,
                          Model model) {

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

    @PostMapping("/deleteWiki")
    public String deleteWiki(@RequestParam Map<String, Object> param) {

        int wikiId = Integer.parseInt((String) param.get("wikiId"));
        Map<String, Object> selectWiki = wikiService.selectWiki(wikiId);
        wikiService.deleteWiki(selectWiki);

        return "redirect:/" + "admin/wikiList";
    }

}
