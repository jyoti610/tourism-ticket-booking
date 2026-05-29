package com.college.tourism.dto.ImgBBDTO;

import lombok.Data;

@Data
public class ImgBBResponse {

    private ImgData data;

    @Data
    public static class ImgData {
        private String url;
    }
}
