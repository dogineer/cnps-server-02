package com.develop.web.ingest.controller;

import com.develop.web.ingest.dto.*;
import com.develop.web.ingest.service.*;
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
@RequestMapping("/s2/api")
public class IngestController {

    private final ClipDBAddService clipDBAddService;
    private final ConvertService convertService;
    private final ThumbnailService thumbnailService;
    private final MediaService mediaService;

    @Transactional
    @PostMapping(value = "/ingest")
    public void upload(
        @RequestPart(value = "files", required = false) MultipartFile files,
        @RequestPart(value = "memberId", required = false) Integer memberId,
        @RequestPart(value = "ingestId", required = false) Integer ingestId,
        @RequestPart(value = "programId", required = false) Integer programId,
        @RequestPart(value = "folderId", required = false) Integer folderId,
        @RequestPart(value = "ingestAt", required = false) LocalDateTime ingestAt) throws IOException {

        String fileOriginalFilename = files.getOriginalFilename();
        assert fileOriginalFilename != null;

        FileDto fileDto = new FileDto(fileOriginalFilename);

        Metadata archiveCopyMetadata = mediaService.archiveFileAndFetchMetadata(files, fileOriginalFilename, fileDto);
        clipDBAddService.addArchiveClipMetadata(archiveCopyMetadata);

        String resultArchivePath = archiveCopyMetadata.file_path;
        String convertingSourcePath = mediaService.getFilePathToConvert(files, fileDto);
        Metadata convertResultMetadata = convertService.transcoding(memberId, ingestId, resultArchivePath, convertingSourcePath, fileDto);

        thumbnailService.markThumbnail(convertResultMetadata);
        clipDBAddService.addConvertClipMetadata(convertResultMetadata);

        Integer archiveMedataId = archiveCopyMetadata.id;
        Integer convertMedataId = convertResultMetadata.id;
        ResultConvertMetadata resultConvertMetadata = new ResultConvertMetadata(ingestId, programId, folderId, archiveMedataId, convertMedataId, ingestAt, LocalDateTime.now());

        clipDBAddService.addResultClipId(resultConvertMetadata);
    }
}
