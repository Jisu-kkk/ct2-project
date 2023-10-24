package com.example.ct2.admin.project.controller;

import com.example.ct2.common.service.CommonService;
import com.example.ct2.admin.project.service.ProjectMngService;
import com.example.ct2.admin.common.vo.Pagination;
import com.example.ct2.admin.common.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/project")
public class ProjectMngController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private ProjectMngService projectMngService;

    @GetMapping("/list")
    public String projectList(@RequestParam(required = false) Map<String, Object> param,
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

    @GetMapping("/insert")
    public String insertProject(Model model) {
        Map<String, Object> param = new HashMap<>();
        param.put("tagType", "BO001");
        // 프로젝트 유형 전체
        List<Map<String, Object>> tagList = commonService.selectTagList(param);

        model.addAttribute("status", "add");
        model.addAttribute("tagList", tagList);
        return "admin/project/projectDetail";
    }

    @PostMapping("/insert")
    public String insertProjectPost(@RequestParam Map<String, Object> param,
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

    @GetMapping("/edit")
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
        model.addAttribute("userVo", commonService.getUserVo());
        return "admin/project/projectDetail";
    }

    @PostMapping("/edit")
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

    @PostMapping("/delete")
    public String deleteProject(@RequestParam Map<String, Object> param,
                                Model model) {
        int projectId = Integer.parseInt((String) param.get("projectId"));
        Map<String, Object> selectProject = projectMngService.selectProject(projectId);
        projectMngService.deleteProject(selectProject);

        return "redirect:/" + "admin/project";
    }
}
