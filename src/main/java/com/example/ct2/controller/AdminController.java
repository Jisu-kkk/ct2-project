package com.example.ct2.controller;

import com.example.ct2.service.Common.CommonService;
import com.example.ct2.service.Common.FileService;
import com.example.ct2.service.UserService;
import com.example.ct2.service.admin.IntroMngService;
import com.example.ct2.service.admin.ProjectMngService;
import com.example.ct2.service.admin.WikiService;
import com.example.ct2.vo.Pagination;
import com.example.ct2.vo.admin.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private UserService userService;

    @Autowired
    private FileService fileService;

    @GetMapping("/login")
    public String login(Model model) {
        return "admin/login/login";
    }

    @GetMapping("/index")
    public String home(Model model, Map<String, Object> param) {
        UserVo userVo = commonService.getUserVo();
        /*TODO_받아와야하는 부분*/
        param.put("orgCode", userVo.getOrganizationCode());

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
        model.addAttribute("userVo", userVo);

        return "admin/main/index";
    }

    @GetMapping("/intro")
    public String intro(Model model, Map<String, Object> param) {
        UserVo userVo = commonService.getUserVo();
        /*TODO_받아와야하는 부분*/
        param.put("orgCode", userVo.getOrganizationCode());

        /*본부소개*/
        List<Map<String, Object>> introList = introMngService.selectIntroList(param);

        model.addAttribute("introList", introList);

        return "admin/intro/intro";
    }

    @ResponseBody
    @PostMapping("/updateIntroDepth")
    public Map<String, Object> updateIntroDepth(@RequestBody Map<String, Object> param) {
        Map<String, Object> result = new HashMap<>();

        List<String> depthList = (List<String>) param.get("depthList");
        introMngService.updateIntroDepth(depthList);

        return result;
    }

    @GetMapping("/addIntro")
    public String addIntro(Model model) {
        model.addAttribute("status", "add");
        return "admin/intro/introDetail";
    }

    @PostMapping("/addIntro")
    public String addIntroPost(@RequestParam Map<String, Object> param,
                               @RequestParam MultipartFile thumbnail) {

        UserVo userVo = commonService.getUserVo();

        String showStatus = (String) param.get("showStatus");
        if (showStatus != null) {
            param.put("showStatus", 1);
        } else {
            param.put("showStatus", 0);
        }
        param.put("userVo", userVo);
        param.put("thumbnail_img", thumbnail);

        introMngService.insertIntro(param);

        return "redirect:/" + "admin/intro";
    }

    @GetMapping("/editIntro")
    public String editIntro(@RequestParam int introId,
                            Model model) {
        Map<String, Object> param = new HashMap<>();
        param.put("introId", introId);

        Map<String, Object> intro = introMngService.selectIntro(param);
        UserVo userVo = commonService.getUserVo();

        model.addAttribute("intro", intro);
        model.addAttribute("userVo", userVo);
        return "admin/intro/introDetail";
    }

    @PostMapping("/editIntro")
    public String editIntroPost(@RequestParam Map<String, Object> param,
                                @RequestParam MultipartFile thumbnail) {

        param.put("thumbnail", thumbnail);
        introMngService.updateIntro(param);

        return "redirect:/" + "admin/intro";
    }

    @GetMapping("/wikiList")
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
        param.put("orgCode", userVo.getOrganizationCode());

        int result = wikiService.insertWiki(param);

        return "redirect:/" + "admin/wikiList";
    }

    @GetMapping("/project")
    public String project(@RequestParam(required = false) Map<String, Object> param,
                          @RequestParam(defaultValue = "1") int curPage,
                          Model model) {
        UserVo userVo = commonService.getUserVo();

        param.put("orgCode", userVo.getOrganizationCode());
        param.put("tagType", "BO001");

        // 프로젝트 유형 전체
        List<Map<String, Object>> tagList = commonService.selectTagList(param);
        // 해당 부서 사람 전체
        List<Map<String, Object>> orgUserList = commonService.selectOrgUserList(param);

        int totalCnt = projectMngService.selectProjectListCnt(param);
        Pagination pagination = new Pagination(totalCnt, curPage);

        param.put("pagination", pagination);

        List<Map<String, Object>> projectList = projectMngService.selectProjectList(param);

        model.addAttribute("tagList", tagList);
        model.addAttribute("orgUserList", orgUserList);
        model.addAttribute("pagination", pagination);
        model.addAttribute("projectList", projectList);
        model.addAttribute("param", param);
        return "admin/project/projectList";
    }

    @GetMapping("/addProject")
    public String addProject(Model model) {
        Map<String, Object> param = new HashMap<>();
        param.put("tagType", "BO001");
        // 프로젝트 유형 전체
        List<Map<String, Object>> tagList = commonService.selectTagList(param);

        model.addAttribute("status", "add");
        model.addAttribute("tagList", tagList);
        return "admin/project/projectDetail";
    }

    @PostMapping("/addProject")
    public String addProjectPost(@RequestParam Map<String, Object> param,
                                 @RequestParam MultipartFile thumbnail,
                                 HttpServletRequest request,
                                 Model model) {
        UserVo userVo = commonService.getUserVo();

        // 사용여부
        String showStatus = (String) param.get("showStatus");
        if (showStatus != null) {
            param.put("showStatus", 1);
        } else {
            param.put("showStatus", 0);
        }
        param.put("thumbnail_img", thumbnail);
        param.put("userVo", userVo);

        int result = projectMngService.insertProject(param);

        return "redirect:/" + "admin/project";
    }

    @GetMapping("/editProject")
    public String editProject(@RequestParam int projectId,
                              Model model) {
        Map<String, Object> param = new HashMap<>();
        param.put("tagType", "BO001");

        Map<String, Object> project = projectMngService.selectProject(projectId);
        List<Integer> projectTagList = projectMngService.selectProjectTagList(projectId);
        String projectTag = "";
        for (int i = 0; i < projectTagList.size(); i++) {
            if (i != 0) {
                projectTag += ",";
            }
            projectTag += projectTagList.get(i);
        }

        List<Map<String, Object>> tagList = commonService.selectTagList(param);

        model.addAttribute("project", project);
        model.addAttribute("projectTagList", projectTagList);
        model.addAttribute("projectTag", projectTag);
        model.addAttribute("tagList", tagList);
        return "admin/project/projectDetail";
    }

    @PostMapping("/editProject")
    public String editProjectPost(@RequestParam Map<String, Object> param,
                                  @RequestParam MultipartFile thumbnail) {
        // 사용여부
        String showStatus = (String) param.get("showStatus");
        if (showStatus != null) {
            param.put("showStatus", 1);
        } else {
            param.put("showStatus", 0);
        }

        param.put("thumbnail_img", thumbnail);

        projectMngService.updateProject(param);

        return "redirect:/" + "admin/project";
    }

    @PostMapping("/deleteProject")
    public String deleteProject(@RequestParam Map<String, Object> param,
                                Model model) {
        int projectId = Integer.parseInt((String) param.get("projectId"));
        Map<String, Object> selectProject = projectMngService.selectProject(projectId);
        projectMngService.deleteProject(selectProject);

        return "redirect:/" + "admin/project";
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
        } else {
            param.put("showStatus", 0);
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
