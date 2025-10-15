package com.moci_3d_backend.domain.webRTC.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class WebRTCDto {
    private String type;
    private String sdp;
    private String candidate;

}
