package com.example.chapter6.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UploadFileVO {
    private int id;
    private String fileName;
    private String filePath;
    private String contentType;
    private String saveFileName;
    private LocalDateTime regDate;
    private int size;
}
