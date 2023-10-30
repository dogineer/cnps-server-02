package com.develop.web.ingest.dto;

import lombok.Data;

@Data
public class FolderDto {
    public String tempDir;
    public String archiveDirDate;
    public String thumbnailDir;
    public String convertDirDate;
    public String convertingTempDirDate;
}
