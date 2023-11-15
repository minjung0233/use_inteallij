package com.example.chapter6.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class GalleryVO {
    private int id;
    private String title;
    private String content;
    private String fileName;
    private String filePath;

    public GalleryVO(int id, String title, String content, String fileName, String filePath) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
