package me.dahye.buildlinker.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class FileUploadService {

    public String executeBuildCommand(String buildCommand, String filePath) {
        try {
            // 파일 경로를 큰따옴표로 감싸기
            String fullCommand = buildCommand + " \"" + filePath + "\"";
            System.out.println("Executing command: " + fullCommand);

            Process process = Runtime.getRuntime().exec(fullCommand);
            StringBuilder output = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append(System.lineSeparator());
                }
            }

            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    output.append("ERROR: ").append(errorLine).append(System.lineSeparator());
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return "명령어 실행 성공:\n" + output;
            } else {
                return "명령어 실행 실패 (코드 " + exitCode + "):\n" + output;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "명령어 실행 중 오류 발생: " + e.getMessage();
        }
    }
}

