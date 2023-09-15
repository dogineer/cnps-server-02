package com.develop.web.video.service;

import com.develop.web.utils.VideoFileUtils;
import com.develop.web.video.dto.FolderDto;
import com.develop.web.video.dto.Metadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThumbnailService {
    private final VideoFileUtils videoFileUtils;
    private final FolderDto folder;

    public void markThumbnail(Metadata convertResultMetadata) {
        String inputPath = convertResultMetadata.file_path;
        String outputPath = folder.thumbnailDir;

        FFmpegBuilder builder = new FFmpegBuilder()
            .overrideOutputFiles(true)
            .setInput(inputPath)
            .addExtraArgs("-ss", "00:00:03")
            .addOutput(outputPath + "/" + convertResultMetadata.clip_uuid + ".jpg")
            .setFrames(1)
            .done();

        FFmpegExecutor executor = new FFmpegExecutor(videoFileUtils.ffmpeg, videoFileUtils.ffprobe);
        executor.createJob(builder).run();

        log.info("[!] " + convertResultMetadata.file_name + " 썸네일을 생성하였습니다. ");
    }
}
