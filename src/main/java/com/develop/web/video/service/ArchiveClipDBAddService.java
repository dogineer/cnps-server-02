package com.develop.web.video.service;

import com.develop.web.video.dto.Metadata;
import com.develop.web.video.mapper.VideoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArchiveClipDBAddService {
    private final VideoMapper videoMapper;

    public void addArchiveClipMetadata(Metadata metadata){
        videoMapper.insertArchiveMetadata(metadata);
    }
}
