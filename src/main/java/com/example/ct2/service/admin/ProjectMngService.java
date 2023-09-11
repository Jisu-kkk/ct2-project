package com.example.ct2.service.admin;

import com.example.ct2.repo.admin.ProjectMngMapper;
import com.example.ct2.service.Common.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class ProjectMngService {

    @Autowired
    private ProjectMngMapper projectMngMapper;

    @Autowired
    private FileService fileService;

    public int selectProjectListCnt(Map<String, Object> param) {
        return projectMngMapper.selectProjectListCnt(param);
    }

    public List<Map<String, Object>> selectProjectList(Map<String, Object> param) {
        return projectMngMapper.selectProjectList(param);
    }

    @Transactional
    public int insertProject(Map<String, Object> param) {
        int result = -1;
        int insertProject = -1;

        MultipartFile thumbnail = (MultipartFile) param.get("thumbnail_img");
        int thumbnailNo = fileService.insertFile(thumbnail);

        if (thumbnailNo > 0) {
            param.put("thumbnailNo", thumbnailNo);
            insertProject = projectMngMapper.insertProject(param);
        }

        if (insertProject > 0) {
            String[] tagList = ((String)param.get("projectTag")).split(",");

            for (String tag : tagList) {
                param.put("tag_id", tag);
                projectMngMapper.insertProjectTag(param);
            }
        }

        return result;
    }

    public Map<String, Object> selectProject(int projectId) {
        return projectMngMapper.selectProject(projectId);
    }

    public List<Integer> selectProjectTagList(int projectId) {
        return projectMngMapper.selectProjectTagList(projectId);
    }

}
