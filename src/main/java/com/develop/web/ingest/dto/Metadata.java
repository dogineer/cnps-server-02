package com.develop.web.ingest.dto;

import lombok.Data;

@Data
public class Metadata {
    public Integer id;
    public String clip_uuid;
    public String file_path;
    public String file_name;
    public String file_ext;
    public int width;
    public int height;
    public String format_name;
    public String format_long_name;
    public String tags;
    public double file_duration;
    public Long file_size;

    public String toString(){
        return  "clip_uuid = " + clip_uuid + "\n" +
                "file_path = " + file_path + "\n" +
                "file_name = " + file_name + "\n" +
                "file_ext = " + file_ext + "\n" +
                "width = " + width + "\n" +
                "height = " + height + "\n" +
                "format_name = " + format_name + "\n" +
                "format_long_name = " + format_long_name + "\n" +
                "tags = " + tags + "\n" +
                "duration = " + file_duration + "\n" +
                "size = " + file_size + "\n";
    }
}
