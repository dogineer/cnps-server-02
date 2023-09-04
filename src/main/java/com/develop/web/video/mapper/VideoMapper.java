package com.develop.web.video.mapper;

import com.develop.web.video.dto.Metadata;
import com.develop.web.video.dto.ResultConvertMetadata;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VideoMapper {

    void insertClipSuccessResponse(ResultConvertMetadata data);

    void insertIngestSuccessId(Integer ingestId);

    void insertArchiveMetadata(Metadata metadata);

    void insertConvertMetadata(Metadata metadata);
}
