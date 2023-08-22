package com.example.ct2.service.Common;

import com.example.ct2.repo.Common.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class FileService {

    @Value("${resource.path}")
    private String uploadDir;

    @Autowired
    private FileMapper fileMapper;

    public int insertFile(MultipartFile file) {
        int result = -1;
        Map<String, Object> param = new HashMap<>();

        String fileRealName = file.getOriginalFilename();
        Long size = file.getSize();

        String fileExtension = fileRealName.substring(fileRealName.lastIndexOf("."),fileRealName.length());

        UUID uuid = UUID.randomUUID();
        String[] uuids = uuid.toString().split("-");
        String uniqueName = uuids[0];

        File saveFile = new File(uploadDir+"\\"+uniqueName + fileExtension);

        param.put("original_name", fileRealName);
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

}
