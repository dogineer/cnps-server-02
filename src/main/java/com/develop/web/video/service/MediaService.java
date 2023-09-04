package com.develop.web.video.service;

import com.develop.web.utils.VideoFileUtils;
import com.develop.web.video.dto.FileDto;
import com.develop.web.video.dto.FolderDto;
import com.develop.web.video.dto.Metadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaService {
    private final FolderDto folder;

    private final VideoFileUtils videoFileUtils;
    private final MediaDataFetcher mediaDataFetcher;
    private final UploadFile uploadFile;

    public Metadata archiveFileAndFetchMetadata(MultipartFile files, String fileOriginalFilename, FileDto fileDto) throws IOException {

        String archiveSourceName = uploadFile.copyFile(files, fileOriginalFilename, folder.archiveDirDate);
        String resultArchivePath = folder.archiveDirDate + "/" + archiveSourceName;
        Metadata archiveResultMetadata = mediaDataFetcher.getMediaInfo(videoFileUtils.ffprobe, resultArchivePath, fileDto);
        log.info("\n[!] ▼ ArchiveResponseEntity ▼\n" + archiveResultMetadata.toString());

        return archiveResultMetadata;
    }

    public String getFilePathToConvert(MultipartFile files, FileDto fileDto) {

        String filenameUUID = fileDto.uuid + "." + fileDto.ext;
        String convertSourceName = uploadFile.copyFile(files, filenameUUID, folder.convertingTempDirDate);

        return folder.convertDirDate + "/" + convertSourceName;
    }
}
