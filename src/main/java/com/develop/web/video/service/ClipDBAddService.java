package com.develop.web.video.service;

import com.develop.web.video.dto.ResultConvertMetadata;
import com.develop.web.video.mapper.VideoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClipDBAddService {
    private final VideoMapper videoMapper;

    public void addClipPost(ResultConvertMetadata resultConvertMetadata){
        videoMapper.insertClipSuccessResponse(resultConvertMetadata);
    }
}
