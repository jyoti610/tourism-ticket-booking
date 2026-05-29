package com.college.tourism.dto.helperDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBaseResponse {

    private Boolean success;

    private String title;

    private String message;

    private Long id;

    private String updatedAt;

    public void setUpdatedAt(Date updatedAt) {
        if (updatedAt != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            this.updatedAt = formatter.format(updatedAt);
        } else {
            this.updatedAt = null;
        }
    }

}