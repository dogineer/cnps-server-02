package com.develop.web.ingest.service;

import com.develop.web.ingest.dto.FileDto;
import com.develop.web.ingest.dto.Metadata;
import com.develop.web.ingest.dto.SendMessageDto;
import com.develop.web.ingest.mapper.VideoMapper;
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
public class ConvertService {
    private final VideoFileUtils videoFileUtils;
    private final VideoMapper videoMapper;
    private final MediaDataFetcher mediaDataFetcher;

    public Metadata transcoding(Integer memberId, Integer ingestId, String filePath, String outputPath, FileDto fileDto) throws IOException {
        log.info("[!] 인제스트: '" + ingestId + "'의 변환이 시작되었습니다. \n" + "inputPath : " + filePath + "\noutputPath : " + outputPath);
        long bitrate = 220_000_000L;
        int cores = Runtime.getRuntime().availableProcessors();

        FFmpegBuilder builder = new FFmpegBuilder()
            .setInput(filePath)
            .overrideOutputFiles(true)
            .addOutput(outputPath)
            .disableSubtitle()
            .setConstantRateFactor(18)
            .setAudioQuality(6)
            .addExtraArgs("-threads", String.valueOf((int) Math.floor(cores * 0.7)))
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
                    updateConsolePercentage(ingestId, percentage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).run();

            videoMapper.insertIngestSuccessId(ingestId);
            SendMessageDto sendMessageDto = new SendMessageDto();
            sendMessageDto.setIngestId(String.valueOf(ingestId));
            sendMessageDto.setPercentage("완료");

            try {
                MyWebSocketClient.sendMessageToAll(sendMessageDto);
                System.out.println();

                MyWebSocketClient.sendMessageToUser(memberId, "현재 등록한 인제스트 작업이 완료됐습니다.");

                log.info("[!] 인제스트를 마쳤습니다. 성공 날짜를 등록합니다. => ingestId:" + ingestId);
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

    protected synchronized void updateConsolePercentage(Integer ingestId, double percentage) {
        double width = 50.0;
        double interval = 1.0;

        System.out.print("\r[!] 인제스트[" + ingestId + "] 클립을 변환하고 있습니다. Progress: [");

        for (int i = 0; i < width; i++) {
            if (i < percentage / interval * (width / 100.0)) {
                System.out.print("█");
            } else {
                System.out.print("░");
            }
        }

        System.out.print("] " + String.format("%.2f", percentage) + "%");
        System.out.flush();
    }
}
