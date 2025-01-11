package me.dahye.buildlinker.controller;

import me.dahye.buildlinker.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @Autowired
    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping
    public ResponseEntity<String> handleFileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("buildCommand") String buildCommand) {
        try {
            // resources/uploads 경로 설정
            String uploadDir = new File("src/main/resources/uploads").getAbsolutePath();
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                boolean isCreated = directory.mkdirs();
                if (!isCreated) {
                    throw new IOException("디렉토리를 생성할 수 없습니다: " + uploadDir);
                }
            }

            // 파일 저장
            String filePath = uploadDir + File.separator + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            // 빌드 명령어 실행
            String commandResult = fileUploadService.executeBuildCommand(buildCommand, filePath);

            // 결과 반환
            return ResponseEntity.ok("파일 업로드 및 빌드 명령어 실행 성공:\n" + commandResult);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("파일 업로드 실패: " + e.getMessage());
        }
    }
}
