package com.develop.web.ingest.mapper;

import com.develop.web.ingest.dto.Metadata;
import com.develop.web.ingest.dto.ResultConvertMetadata;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VideoMapper {

    void insertClipSuccessResponse(ResultConvertMetadata data);

    void insertIngestSuccessId(Integer ingestId);

    void insertArchiveMetadata(Metadata metadata);

    void insertConvertMetadata(Metadata metadata);
}
