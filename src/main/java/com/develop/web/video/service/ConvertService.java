package com.develop.web.video.service;

import com.develop.web.video.dto.FileDto;
import com.develop.web.video.dto.Metadata;
import com.develop.web.video.dto.SendMessageDto;
import com.develop.web.video.mapper.IngestMapper;
import com.develop.web.websocket.MyWebSocketClient;
import com.develop.web.utils.VideoFileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class Convert {
    private final VideoFileUtils videoFileUtils;
    private final IngestMapper ingestMapper;
    private final MediaDataFetcher mediaDataFetcher;

    public Metadata transcoding(Integer ingestId, String filePath, String outputPath, FileDto fileDto) throws IOException {
        log.info("[!] 인제스트: '" + ingestId + "'의 변환이 시작되었습니다. \n" + "inputPath : " + filePath + "\noutputPath : " + outputPath);
        long bitrate = 220_000_000L;

        FFmpegBuilder builder = new FFmpegBuilder()
            .setInput(filePath)
            .overrideOutputFiles(true)
            .addOutput(outputPath)
            .disableSubtitle()
            .setVideoCodec("libx264")
            .setVideoResolution(1280, 720)
            .setVideoBitRate(bitrate)
            .setVideoFrameRate(30)
            .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
            .done();

        FFmpegExecutor executor = new FFmpegExecutor(videoFileUtils.ffmpeg, videoFileUtils.ffprobe);
        FFmpegProbeResult probeResult = videoFileUtils.ffprobe.probe(filePath);

        CompletableFuture<Metadata> completableFuture = CompletableFuture.supplyAsync(() -> {
            executor.createJob(builder, progress -> {
                double percentage = Math.round(progress.out_time_ns / probeResult.getFormat().duration) / 10000000.0;

                String sendPercentage = String.valueOf(String.format("%.2f", percentage));
                String sendIngestId = String.valueOf(ingestId);

                try {
                    SendMessageDto sendMessageDto = new SendMessageDto();
                    sendMessageDto.setIngestId(sendIngestId);
                    sendMessageDto.setPercentage(sendPercentage);

                    MyWebSocketClient.sendMessageToAll(sendMessageDto);
                    log.info("[!] 클립을 변환하고 있습니다. => 요청: " + ingestId + ", 퍼센트: " + percentage);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).run();

            ingestMapper.insertIngestSuccessRequest(ingestId);
            SendMessageDto sendMessageDto = new SendMessageDto();
            sendMessageDto.setIngestId(String.valueOf(ingestId));
            sendMessageDto.setPercentage("완료");

            try {
                MyWebSocketClient.sendMessageToAll(sendMessageDto);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            log.info("[!] 인제스트를 마쳤습니다. 성공일을 등록합니다. => ingestId:" + ingestId);

            try {
                return mediaDataFetcher.getMediaInfo(videoFileUtils.ffprobe, outputPath, fileDto);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        completableFuture.thenApply((Void) -> {
            Metadata convertMetadata;

            try {
                convertMetadata = mediaDataFetcher.getMediaInfo(videoFileUtils.ffprobe, outputPath, fileDto);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return convertMetadata;
        });

        return completableFuture.join();
    }
}
