package com.example.ct2.service.Common;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.ct2.repo.Common.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
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

    private final AmazonS3 amazonS3;

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

            // putObject(버킷명, 파일명, 파일데이터, 메타데이터)로 S3에 객체 등록
            amazonS3.putObject(bucket
                    , saveFolder + uniqueName + fileExtension
                    , file.getInputStream(), objMeta);

            // 조회 시
            String url = URLDecoder.decode(amazonS3.getUrl(bucket, saveFolder + uniqueName + fileExtension).toString(), "utf-8");
            System.out.println("+++++ insertFileUrl +++++");
            System.out.println(url);

            param.put("original_name", fileRealName.substring(0, lastStr));
            param.put("name", uniqueName);
            param.put("path", saveFolder);
            param.put("type", fileExtension);
            param.put("size", file.getSize());

            fileMapper.insertFile(param);
            result = (int) param.get("fileId");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
