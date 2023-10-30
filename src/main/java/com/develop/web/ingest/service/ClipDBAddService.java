package com.develop.web.ingest.service;

import com.develop.web.ingest.dto.Metadata;
import com.develop.web.ingest.dto.ResultConvertMetadata;
import com.develop.web.ingest.mapper.VideoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClipDBAddService {
    private final VideoMapper videoMapper;

    public void addArchiveClipMetadata(Metadata metadata){
        videoMapper.insertArchiveMetadata(metadata);
    }

    public void addConvertClipMetadata(Metadata metadata){
        videoMapper.insertConvertMetadata(metadata);
    }

    public void addResultClipId(ResultConvertMetadata resultConvertMetadata){
        videoMapper.insertClipSuccessResponse(resultConvertMetadata);
    }
}
