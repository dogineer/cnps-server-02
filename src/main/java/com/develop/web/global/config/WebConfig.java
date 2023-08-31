package com.develop.web.global.config;

import com.develop.web.video.dto.FolderDto;
import com.develop.web.utils.MakeFolderSetup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebConfig {
    private final MakeFolderSetup makeFolderSetup;

    @Bean
    public FolderDto SetupFolder(){
        log.info("[R] 폴더를 검사합니다.");
        FolderDto folder = new FolderDto();
        makeFolderSetup.init(folder);
        return folder;
    }
}
