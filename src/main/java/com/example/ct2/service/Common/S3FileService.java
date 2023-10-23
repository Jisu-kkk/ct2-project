package com.example.ct2.service.Common;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.ct2.repo.Common.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3FileService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Autowired
    private FileMapper fileMapper;

    private final AmazonS3Client amazonS3Client;

    @Transactional
    public int insertFile(MultipartFile file, String saveFolder){
        int result = -1;
        Map<String, Object> param = new HashMap<>();

        UUID uuid = UUID.randomUUID();
        String uniqueName = uuid.toString();

        try {
            String fileRealName = file.getOriginalFilename();
            int lastStr = fileRealName.lastIndexOf(".");
            String fileExtension = fileRealName.substring(fileRealName.lastIndexOf("."),fileRealName.length());

            // metaData 생성
            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentLength(file.getInputStream().available());
            objMeta.setContentType(file.getContentType()); // 메타데이터 값을 image/png로 변경

            amazonS3Client.putObject(
                    new PutObjectRequest(bucket,
                            saveFolder + uniqueName + fileExtension,
                            file.getInputStream(),
                            objMeta)
            );

            param.put("original_name", fileRealName.substring(0, lastStr));
            param.put("name", uniqueName);
            param.put("path", saveFolder);
            param.put("type", fileExtension);
            param.put("size", file.getSize());

            fileMapper.insertFile(param);
            result = (int) param.get("fileId");

        } catch (AmazonS3Exception s3Exception) {
            s3Exception.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Transactional
    public int updateFile(MultipartFile file, String saveFolder, int fileId) {
        int result = -1;
        try {
            if (file.getOriginalFilename().length() > 0) {
                int delResult = deleteFile((long) fileId);
                if (delResult > 0) {
                    result = insertFile(file, saveFolder);
                }
            } else {
                result = fileId;
            }
        } catch (AmazonS3Exception s3Exception) {
            s3Exception.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Transactional
    public int deleteFile(Long fileId) {
        int result = -1;

        try {
            Map<String, Object> param = new HashMap<>();
            param.put("file_id", fileId);
            String fileKey = fileKey(param);

            // 이미지 삭제
            boolean isObjectExist = amazonS3Client.doesObjectExist(bucket, fileKey);
            if (isObjectExist) {
                amazonS3Client.deleteObject(bucket, fileKey);
                result = fileMapper.deleteFile(param);
            }
        } catch(AmazonS3Exception s3Exception) {
            s3Exception.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 이미지 키값 조회
    public String fileKey(Map<String, Object> param) {
        String result = "";
        Map<String, Object> selectFile = fileMapper.selectFile(param);
        if (selectFile != null) {
            result = (String) selectFile.get("path");
            result += (String) selectFile.get("name");
            result += (String) selectFile.get("type");
        }
        return result;
    }
}
