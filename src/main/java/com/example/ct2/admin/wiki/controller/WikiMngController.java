package com.example.ct2.admin.wiki.controller;

import com.example.ct2.admin.wiki.service.WikiMngService;
import com.example.ct2.common.service.CommonService;
import com.example.ct2.admin.common.vo.Pagination;
import com.example.ct2.admin.common.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/wiki")
public class WikiMngController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private WikiMngService wikiService;

    @GetMapping("/list")
    public String wikiList(@RequestParam(required = false) Map<String, Object> param,
                           @RequestParam(defaultValue = "1") int curPage,
                           Model model) {
        UserVo userVo = commonService.getUserVo();
        param.put("orgCode", userVo.getOrganizationCode());

        param.put("tagType", "BO002");

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

    @GetMapping("/insert")
    public String insertWiki(Model model) {
        Map<String, Object> param = new HashMap<>();
        param.put("tagType", "BO002");
        List<Map<String, Object>> tagList = commonService.selectTagList(param);

        model.addAttribute("tagList", tagList);
        return "admin/wiki/wikiEdit";
    }

    @PostMapping("/insert")
    public String insertWikiPost(@RequestParam Map<String, Object> param,
                              @RequestParam MultipartFile thumbnail,
                              @RequestParam MultipartFile titleImg,
                              HttpServletRequest request,
                              Model model) throws IOException {
        UserVo userVo = commonService.getUserVo();
        // 사용여부
        String showStatus = (String) param.get("showStatus");
        if (showStatus != null) {
            param.put("showStatus", 1);
        } else {
            param.put("showStatus", 0);
        }
        param.put("title_img", titleImg);
        param.put("thumbnail_img", thumbnail);
        param.put("userVo", userVo);

        int result = wikiService.insertWiki(param);

        return "redirect:/" + "admin/wikiList";
    }



    @GetMapping("/edit")
    public String editWiki(@RequestParam int wikiId,
                           Model model) {

        Map<String, Object> param = new HashMap<>();
        param.put("tagType", "BO002");

        UserVo userVo = commonService.getUserVo();
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

        model.addAttribute("userVo", userVo);
        model.addAttribute("wiki", wiki);
        model.addAttribute("wikiTagList", wikiTagList);
        model.addAttribute("wikiTag", wikiTag);
        model.addAttribute("tagList", tagList);
        return "admin/wiki/wikiEdit";
    }

    @PostMapping("/edit")
    public String editWikiPost(@RequestParam Map<String, Object> param,
                               @RequestParam MultipartFile thumbnail,
                               @RequestParam MultipartFile titleImg) {
        // 사용여부
        String showStatus = (String) param.get("showStatus");
        if (showStatus != null) {
            param.put("showStatus", 1);
        } else {
            param.put("showStatus", 0);
        }

        param.put("title_img", titleImg);
        param.put("thumbnail_img", thumbnail);

        int result = wikiService.updateWiki(param);

        return "redirect:/" + "admin/wikiList";
    }

    @PostMapping("/delete")
    public String deleteWiki(@RequestParam Map<String, Object> param) {

        int wikiId = Integer.parseInt((String) param.get("wikiId"));
        Map<String, Object> selectWiki = wikiService.selectWiki(wikiId);
        wikiService.deleteWiki(selectWiki);

        return "redirect:/" + "admin/wikiList";
    }
}
