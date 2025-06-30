package com.softmania.feeease.component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class PresignedUrl {
    private String url;
    private LocalDateTime expiry;
}
