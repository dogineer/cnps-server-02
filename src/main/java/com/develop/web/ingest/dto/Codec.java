package com.develop.web.ingest.dto;

public enum Codec {

    H264("libx264"),
    DNXHD("dnxhd")

    ;

    private String id;

    Codec(String id) {
        this.id = id;
    }

}
