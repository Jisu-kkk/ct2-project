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

    @Transactional
    public int updateProject(Map<String, Object> param) {
        int result = -1;
        int updateProject = -1;

        Map<String, Object> selectProject = selectProject(Integer.parseInt((String)param.get("projectId")));
        MultipartFile thumbnail = (MultipartFile) param.get("thumbnail_img");

        int updateThumbnail = fileService.updateFile(thumbnail, ((Long)selectProject.get("file_id")).intValue());

        if (updateThumbnail > 0) {
            updateProject = projectMngMapper.updateProject(param);
        }

        if (updateProject > 0) {
            int deleteTag = projectMngMapper.deleteProjectTag(param);
            if (deleteTag > 0) {
                String[] tagList = ((String) param.get("projectTag")).split(",");
                for (String tag : tagList) {
                    param.put("tag_id", tag);
                    param.put("project_id", param.get("projectId"));
                    projectMngMapper.insertProjectTag(param);
                }
            }
        }
        return result;
    }

    @Transactional
    public int deleteProject(Map<String, Object> param) {
        int result = -1;

        int projectId = ((Long) param.get("id")).intValue();
        param.put("projectId", projectId);

        int deleteProjectTag = projectMngMapper.deleteProjectTag(param);
        if (deleteProjectTag > 0) {
            int delProject = projectMngMapper.deleteProject(param);
            if (delProject > 0) {
                fileService.deleteFile((Long) param.get("file_id"));
            }
        }
        return result;
    }

}
