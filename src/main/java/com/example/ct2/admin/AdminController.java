package com.example.ct2.admin;

import com.example.ct2.common.service.CommonService;
import com.example.ct2.admin.intro.service.IntroMngService;
import com.example.ct2.admin.common.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IntroMngService introMngService;

    @Autowired
    private CommonService commonService;

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                        Model model) {
        model.addAttribute("error", error);
        return "admin/login/login";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
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
}
