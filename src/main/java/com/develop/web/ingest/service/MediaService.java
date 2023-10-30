package com.develop.web.ingest.service;

import com.develop.web.utils.VideoFileUtils;
import com.develop.web.ingest.dto.FileDto;
import com.develop.web.ingest.dto.FolderDto;
import com.develop.web.ingest.dto.Metadata;
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

        return mediaDataFetcher.getMediaInfo(videoFileUtils.ffprobe, resultArchivePath, fileDto);
    }

    public String getFilePathToConvert(MultipartFile files, FileDto fileDto) {

        String filenameUUID = fileDto.uuid + "." + fileDto.ext;
        String convertSourceName = uploadFile.copyFile(files, filenameUUID, folder.convertingTempDirDate);

        return folder.convertDirDate + "/" + convertSourceName;
    }
}
