package com.develop.web.ingest.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IngestRequestData {
    private Integer id;                 // 인제스트 ID
    private LocalDateTime createAt;     // 생성 날짜
    private Integer memberId;           // 멤버 ID
    private String title;               // 제목
    private Integer programId;          // 프로그램 아이디
    private String program;             // 프로그램명 (팀명)
    private Integer folder;             // 폴더
    private String name;                // 요청자
    private String phone;               // 전화번호
    private Codec codec;                // 요청코덱
    private LocalDateTime success;      // 성공 유무

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }
}
