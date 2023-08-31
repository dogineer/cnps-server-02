package com.develop.web.utils;

import com.develop.web.video.dto.FolderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class MakeFolderSetup {
    private final RouteStatus routeStatus;

    @Value("${app.upload.dir:${user.home}/media-buddies/temp/}")
    private String uploadTempDir;

    @Value("${app.upload.dir:${user.home}/media-buddies/archive/}")
    private String uploadArchiveDir;

    @Value("${app.upload.dir:${user.home}/media-buddies/thumbnail/}")
    private String uploadThumbnailDir;

    @Value("${app.upload.dir:${user.home}/media-buddies/convert/}")
    private String uploadConvertDir;

    @Value("${app.upload.dir:${user.home}/media-buddies/convert/temp/}")
    private String convertingTempDir;

    public void confirmAndCreateFolder(String folderPath) {
        try {
            routeStatus.uploadPathCheck(folderPath);
        } catch (NullPointerException e) {
            log.info("[+] 해당 경로에 폴더를 생성하였습니다. " + folderPath);
            File newFolder = new File(folderPath);
            newFolder.mkdirs();
        }
    }

    public void init(FolderDto folderDto) {
        LocalDate now = LocalDate.now();

        folderDto.archiveDirDate = uploadArchiveDir + now;
        folderDto.convertDirDate = uploadConvertDir + now;
        folderDto.thumbnailDir = uploadThumbnailDir + now;
        folderDto.convertingTempDirDate = convertingTempDir + now;
        folderDto.tempDir = uploadTempDir;

        confirmAndCreateFolder(folderDto.tempDir);
        confirmAndCreateFolder(folderDto.archiveDirDate);
        confirmAndCreateFolder(folderDto.thumbnailDir);
        confirmAndCreateFolder(folderDto.convertDirDate);
        confirmAndCreateFolder(folderDto.convertingTempDirDate);
    }
}
