package com.ChatUp.ChatUp.file;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.io.File.separator;
import static java.lang.System.currentTimeMillis;

@Service
@Slf4j
@RequiredArgsConstructor
@AllArgsConstructor
public class FileService {

    @Value("${application.file.uploads.media-output-path}")
    private String fileUploadPath;

    public String saveFile(
          @NotNull MultipartFile sourceFile,
          @NotNull String senderId) {
            final String fileUploadPath  = "users"+ separator + senderId;
            return uploadFile(sourceFile,fileUploadPath);
    }

    private String uploadFile(@NotNull MultipartFile sourceFile, @NotNull String fileUploadSubPath) {
        final String finalUploadPath = fileUploadPath + separator + fileUploadSubPath ;
        File target = new File(finalUploadPath);

        if(!target.exists()) {
            boolean folderCreated = target.mkdirs();
            if(!folderCreated){
                log.warn("Failed to create the target folder, {}", target);
            }
        }

        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        String targetFilePath = fileUploadPath + separator + currentTimeMillis() +"."+ fileExtension ;

        Path targetPath = Paths.get(targetFilePath);
        try{
            Files.write(targetPath, sourceFile.getBytes());
            return targetFilePath ;
        } catch (IOException e) {
            log.error("File was not saved", e);
        }
        return null;
    }

    private String getFileExtension(String fileName) {
        if(fileName==null || fileName.isEmpty()){
            return " ";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        if(lastDotIndex == -1)
            return "";
        return fileName.substring(lastDotIndex+1).toLowerCase();
    }


}
