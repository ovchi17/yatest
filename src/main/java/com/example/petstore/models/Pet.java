package com.example.petstore.models;

import lombok.*;

import java.io.File;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    private Long id;
    private Category category;
    private String name;
    private List<String> photoUrls;
    private List<Tag> tags;
    private Status status;

    public enum Status { available, pending, sold }

    @Data
    public static class Category {
        private Long id;
        private String name;
    }

    @Data
    public static class Tag {
        private Long id;
        private String name;
    }

    @Data
    public static class FormDataForUpdateById {
        private String name;
        private Status status;
    }

    @Data
    @AllArgsConstructor
    public static class FormDataForUploadImage {
        private String additionalMetadata;
        private File file;
    }
}
