package com.example.ct2.resource;

import com.example.ct2.service.Common.CommonService;
import com.example.ct2.vo.admin.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/api/v1")
public class CommonResource {

    @Autowired
    private CommonService commonService;

    @ResponseBody
    @PostMapping("/loginUserProfile")
    public Map<String, Object> blogList(@RequestBody Map<String, Object> param) {

        Map<String, Object> result = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserVo userVo = (UserVo) authentication.getPrincipal();
        if (userVo != null) {
            System.out.println(userVo);
            param.put("userId", String.valueOf(userVo.getId()));
            param.put("orgCode", userVo.getOrganizationCode());
            result = commonService.selectOrgUserProfileOne(param);
        }

        return result;
    }

}
