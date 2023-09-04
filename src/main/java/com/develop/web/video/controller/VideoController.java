package com.develop.web.video.controller;

import com.develop.web.video.dto.*;
import com.develop.web.video.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api")
public class VideoController {

    private final ArchiveClipDBAddService archiveClipDBAddService;
    private final ConvertClipDBAddService convertClipDBAddService;
    private final ClipDBAddService clipDBAddService;

    private final ConvertService convertService;
    private final ThumbnailService thumbnailService;

    private final MediaService mediaService;

    @Transactional
    @PostMapping(value = "/upload")
    public void upload(
        @RequestPart(value = "files", required = false) MultipartFile files,
        @RequestPart(value = "ingestId", required = false) Integer ingestId,
        @RequestPart(value = "teamId", required = false) Integer teamId,
        @RequestPart(value = "folderId", required = false) Integer folderId,
        @RequestPart(value = "ingestAt", required = false) LocalDateTime ingestAt) throws IOException {

        String fileOriginalFilename = files.getOriginalFilename();
        assert fileOriginalFilename != null;

        FileDto fileDto = new FileDto(fileOriginalFilename);

        Metadata archiveCopyMetadata = mediaService.archiveFileAndFetchMetadata(files, fileOriginalFilename, fileDto);
        archiveClipDBAddService.addArchiveClipMetadata(archiveCopyMetadata);

        String resultArchivePath = archiveCopyMetadata.file_path;
        String convertingSourcePath = mediaService.getFilePathToConvert(files, fileDto);
        Metadata convertResultMetadata = convertService.transcoding(ingestId, resultArchivePath, convertingSourcePath, fileDto);

        thumbnailService.markThumbnail(convertResultMetadata);
        convertClipDBAddService.addConvertClipMetadata(convertResultMetadata);
        log.info("\n[!] ▼ ConvertResponseEntity ▼\n" + convertResultMetadata.toString());

        Integer archiveMedataId = archiveCopyMetadata.id;
        Integer convertMedataId = convertResultMetadata.id;
        ResultConvertMetadata resultConvertMetadata = new ResultConvertMetadata(ingestId, teamId, folderId, archiveMedataId, convertMedataId, ingestAt, LocalDateTime.now());
        log.info("\n[!] ▼ ResultResponseEntity ▼\n" + resultConvertMetadata.toString());

        clipDBAddService.addClipPost(resultConvertMetadata);
    }
}
