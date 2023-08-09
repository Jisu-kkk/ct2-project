package com.example.demo.controller;

import com.example.demo.service.BlogService;
import com.example.demo.service.Common.CommonService;
import com.example.demo.service.IntroService;
import com.example.demo.service.OrganizationService;
import com.example.demo.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private OrganizationService orgService;

    @Autowired
    private IntroService introService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/{orgName}")
    public String main(@PathVariable("orgName") String orgName, Model model, HttpSession session) {
        if (!"favicon.ico".equals(orgName)) {
            Map<String, Object> org = orgService.selectOrg(orgName);

            // Intro
            List<Map<String, Object>> introList = introService.selectIntroList(org);

            // 프로젝트
            List<Map<String, Object>> projectList = projectService.selectProjectList(org);

            session.setAttribute("org", org);
            org.put("startPost", 0);
            org.put("postCnt", 3);
            // 기술
            List<Map<String, Object>> blogList = blogService.selectBlogList(org);

            model.addAttribute("introList", introList);
            model.addAttribute("projectList", projectList);
            model.addAttribute("blogList", blogList);
        }
        return "main/index";
    }

    @RequestMapping("/{orgName}/blogList")
    public String blogList(@PathVariable String orgName, Model model, HttpSession session) {
        Map<String, Object> org = (Map<String, Object>) session.getAttribute("org");
        Map<String, Object> param = new HashMap<>();
        param.put("tagType", "BO002");
        param.put("orgCode", org.get("orgCode"));

        List<Map<String, Object>> tagList = commonService.selectTagList(param);

        model.addAttribute("tagList", tagList);
        return "main/blogList";
    }

    @GetMapping("/{orgName}/detail/{blogId}")
    public String blogDetail(@PathVariable("orgName") String orgName, @PathVariable("blogId") Long blogId, Model model, HttpSession session) {

        model.addAttribute("blogDetail", blogService.selectBlogDetail(blogId));

        return "main/blogDetail";
    }
}
