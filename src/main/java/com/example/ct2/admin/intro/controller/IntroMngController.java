package com.example.ct2.admin.intro.controller;

import com.example.ct2.common.service.CommonService;
import com.example.ct2.admin.intro.service.IntroMngService;
import com.example.ct2.admin.common.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/intro")
public class IntroMngController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private IntroMngService introMngService;

    @GetMapping("/list")
    public String introList(Model model, Map<String, Object> param) {
        UserVo userVo = commonService.getUserVo();
        /*TODO_받아와야하는 부분*/
        param.put("orgCode", userVo.getOrganizationCode());

        /*본부소개*/
        List<Map<String, Object>> introList = introMngService.selectIntroList(param);

        model.addAttribute("introList", introList);

        return "admin/intro/intro";
    }

    @ResponseBody
    @PostMapping("/updateDepth")
    public Map<String, Object> updateIntroDepth(@RequestBody Map<String, Object> param) {
        Map<String, Object> result = new HashMap<>();

        List<String> depthList = (List<String>) param.get("depthList");
        introMngService.updateIntroDepth(depthList);

        return result;
    }

    @GetMapping("/insert")
    public String insertIntro(Model model) {
        model.addAttribute("status", "add");
        return "admin/intro/introDetail";
    }

    @PostMapping("/insert")
    public String insertIntroPost(@RequestParam Map<String, Object> param,
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

    @GetMapping("/edit")
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

    @PostMapping("/edit")
    public String editIntroPost(@RequestParam Map<String, Object> param,
                                @RequestParam MultipartFile thumbnail) {

        param.put("thumbnail", thumbnail);
        introMngService.updateIntro(param);

        return "redirect:/" + "admin/intro";
    }

    @PostMapping("/delete")
    public String deleteIntro(@RequestParam Map<String, Object> param) {
        System.out.println(param);
        int introId = Integer.parseInt((String) param.get("introId"));
        Map<String, Object> selectIntro = introMngService.selectIntro(param);
        introMngService.deleteIntro(selectIntro);

        return "redirect:/" + "admin/intro";
    }
}
