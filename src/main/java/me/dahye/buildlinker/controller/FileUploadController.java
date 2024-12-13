package me.dahye.buildlinker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    @PostMapping
    public ResponseEntity<String> handleFileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("buildCommand") String buildCommand) {
        try {
            // 파일 저장 경로
            String uploadDir = "uploads/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs(); // 디렉토리 생성
            }

            // 파일 저장
            String filePath = uploadDir + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            // 빌드 명령어 로그 출력
            System.out.println("Received build command: " + buildCommand);

            return ResponseEntity.ok("파일 업로드 성공: " + file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("파일 업로드 실패");
        }
    }
}