package com.develop.web.video.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResultConvertMetadata {
    public Integer ingest_id;
    public Integer program_id;
    public Integer folder_id;
    public Integer archive_metadata_id;
    public Integer convert_metadata_id;
    public LocalDateTime ingest_at;
    public LocalDateTime end_at;
    public Byte del_fl;

    public ResultConvertMetadata(Integer ingest_id, Integer program_id, Integer folder_id, Integer archive_metadata_id, Integer convert_metadata_id, LocalDateTime ingest_at, LocalDateTime end_at) {
        this.ingest_id = ingest_id;
        this.program_id = program_id;
        this.folder_id = folder_id;
        this.archive_metadata_id = archive_metadata_id;
        this.convert_metadata_id = convert_metadata_id;
        this.ingest_at = ingest_at;
        this.end_at = end_at;
    }

    public String toString() {
        return "ingest_id = " + ingest_id + "\n" +
            "program_id = " + program_id + "\n" +
            "folder_id = " + folder_id + "\n" +
            "archive_metadata_id = " + archive_metadata_id + "\n" +
            "convert_metadata_id = " + convert_metadata_id + "\n" +
            "end_at = " + end_at;
    }
}
