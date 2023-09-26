package com.example.ct2.service.Common;

import com.example.ct2.repo.Common.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class FileService {

    @Value("${resource.path}")
    private String uploadDir;

    @Autowired
    private FileMapper fileMapper;

    @Transactional
    public int insertFile(MultipartFile file) {
        int result = -1;
        Map<String, Object> param = new HashMap<>();

        String fileRealName = file.getOriginalFilename();
        int lastStr = fileRealName.lastIndexOf(".");
        String realName = fileRealName.substring(0, lastStr);

        Long size = file.getSize();

        String fileExtension = fileRealName.substring(fileRealName.lastIndexOf("."),fileRealName.length());

        UUID uuid = UUID.randomUUID();
        String[] uuids = uuid.toString().split("-");
        String uniqueName = uuids[0];

        File saveFile = new File(uploadDir + uniqueName + fileExtension);

        param.put("original_name", realName);
        param.put("name", uniqueName);
        param.put("path", uploadDir);
        param.put("type", fileExtension);
        param.put("size", size);

        try {
            file.transferTo(saveFile);
            fileMapper.insertFile(param);
            result = (int) param.get("fileId");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Transactional
    public int updateFile(MultipartFile file, int fileId) {
        int result = -1;
        Map<String, Object> param = new HashMap<>();

        param.put("file_id", fileId);
        Map<String, Object> selectFile = fileMapper.selectFile(param);

        String fileRealName = file.getOriginalFilename();

        if (fileRealName != null && !("").equals(fileRealName)) {
            int lastStr = fileRealName.lastIndexOf(".");
            String realName = fileRealName.substring(0, lastStr);

            Long size = file.getSize();

            String fileExtension = fileRealName.substring(fileRealName.lastIndexOf("."),fileRealName.length());

            UUID uuid = UUID.randomUUID();
            String[] uuids = uuid.toString().split("-");
            String uniqueName = uuids[0];

            File saveFile = new File(uploadDir + uniqueName + fileExtension);
            File delFile = new File(uploadDir
                                    + (String) selectFile.get("name")
                                    + (String) selectFile.get("type"));

            param.put("file_id", fileId);
            param.put("original_name", realName);
            param.put("name", uniqueName);
            param.put("path", uploadDir);
            param.put("type", fileExtension);
            param.put("size", size);

            try {
                if (delFile.exists()) {
                    if (delFile.delete()) {
                        file.transferTo(saveFile);
                        result = fileMapper.updateFile(param);
                    }
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            result = 1;
        }

        return result;
    }

    @Transactional
    public int deleteFile(Long fileId) {
        int result = -1;
        Map<String, Object> param = new HashMap<>();
        param.put("file_id", fileId);

        Map<String, Object> selectFile = fileMapper.selectFile(param);

        File delFile = new File(uploadDir
                                + ((String) selectFile.get("name"))
                                + ((String) selectFile.get("type")));

        if (delFile.exists()){
            if (fileMapper.deleteFile(param) > 0) {
                if(delFile.delete()){
                    result = 1;
                }
            }
        }

        return result;
    }
}
