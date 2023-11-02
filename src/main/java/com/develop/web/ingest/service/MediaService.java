package com.develop.web.ingest.service;

import com.develop.web.utils.VideoFileUtils;
import com.develop.web.ingest.dto.FileDto;
import com.develop.web.ingest.dto.FolderDto;
import com.develop.web.ingest.dto.Metadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaService {
    private final FolderDto folder;

    private final VideoFileUtils videoFileUtils;
    private final UploadFile uploadFile;

    public Metadata archiveFileAndFetchMetadata(MultipartFile files, String fileOriginalFilename, FileDto fileDto) throws IOException {

        String archiveSourceName = uploadFile.copyFile(files, fileOriginalFilename, folder.archiveDirDate);
        String resultArchivePath = folder.archiveDirDate + "/" + archiveSourceName;

        return getMediaInfo(videoFileUtils.ffprobe, resultArchivePath, fileDto);
    }

    public String getFilePathToConvert(MultipartFile files, FileDto fileDto) {

        String filenameUUID = fileDto.uuid + "." + fileDto.ext;
        String convertSourceName = uploadFile.copyFile(files, filenameUUID, folder.convertingTempDirDate);

        return folder.convertDirDate + "/" + convertSourceName;
    }

    /**
     * @description 미디어의 정보값을 보여준다.
     * @param filePath 파일 경로
     * @json data
     * - filename : 파일 이름
     * - file_path : 파일 경로
     * - width : 가로
     * - height : 세로
     * - format_name : 포멧 이름
     * - format_long_name : 포맷 전체 이름
     * - tags : 영상 태그 정보
     * - duration : 길이
     * - size : 사이즈(용량)
     * @return metadata (json)
     * */
    public Metadata getMediaInfo(FFprobe ffprobe, String filePath, FileDto fileDto) throws IOException {
        FFmpegProbeResult probeResult = ffprobe.probe(filePath);
        Metadata metadata = new Metadata();

        metadata.clip_uuid = fileDto.uuid;
        metadata.file_path = filePath;
        metadata.file_name = fileDto.originalFileName;
        metadata.file_ext = fileDto.ext;
        metadata.width = probeResult.getStreams().get(0).width;
        metadata.height = probeResult.getStreams().get(0).height;
        metadata.format_name = probeResult.getFormat().format_name;
        metadata.format_long_name = probeResult.getFormat().format_long_name;
        metadata.tags = probeResult.getFormat().tags.toString();
        metadata.file_duration = probeResult.getFormat().duration;
        metadata.file_size = probeResult.getFormat().size;

        return metadata;
    }
}
