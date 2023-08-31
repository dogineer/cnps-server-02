package com.develop.web.video.controller;

import com.develop.web.utils.VideoFileUtils;
import com.develop.web.video.dto.FileDto;
import com.develop.web.video.dto.FolderDto;
import com.develop.web.video.service.*;
import com.develop.web.video.dto.Metadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api")
public class VideoController {

  private final FolderDto folder;
  private final VideoFileUtils videoFileUtils;
  private final FetcherFileExt fetcherFileExt;
  private final UploadFile uploadFile;
  private final MediaDataFetcher mediaDataFetcher;
  private final ConvertService convertService;
  private final ThumbnailService thumbnailService;

  @PostMapping(value = "/upload")
  public ResponseEntity<Metadata> upload(
    @RequestParam(value = "files", required = false)MultipartFile file,
    @RequestParam(value = "ingestId", required = false)Integer ingestId) throws IOException {

    String fileOriginalFilename = file.getOriginalFilename();
    assert fileOriginalFilename != null;

    String extractExt = fetcherFileExt.extractExt(fileOriginalFilename);
    String uuid = UUID.randomUUID().toString();

    FileDto fileDto = new FileDto();
    fileDto.uuid = uuid;
    fileDto.originalFileName = fileOriginalFilename;
    fileDto.ext = extractExt;

    String archiveSourceName = uploadFile.copyFile(file, fileOriginalFilename, folder.archiveDirDate);
    String resultArchivePath = folder.archiveDirDate + "/" + archiveSourceName;
    Metadata archiveResultMetadata = mediaDataFetcher.getMediaInfo(videoFileUtils.ffprobe, resultArchivePath, fileDto);
    log.info("\n[!] ▼ ArchiveResponseEntity ▼\n" + archiveResultMetadata.toString());

    String filenameUUID = uuid + "." + extractExt;
    String convertSourceName = uploadFile.copyFile(file, filenameUUID, folder.convertingTempDirDate);
    String convertingSourcePath = folder.convertDirDate + "/" + convertSourceName;
    Metadata convertResultMetadata = convertService.transcoding(ingestId, resultArchivePath, convertingSourcePath, fileDto);
    thumbnailService.markThumbnail(convertResultMetadata);
    log.info("\n[!] ▼ ConvertResponseEntity ▼\n" + convertResultMetadata.toString());

    return ResponseEntity.ok().body(convertResultMetadata);
  }
}
