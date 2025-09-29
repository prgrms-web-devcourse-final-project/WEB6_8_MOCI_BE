package com.moci_3d_backend.domain.webRTC.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebRTCDto {
    private String type;
    private String sdp;
    private String candidate;

}
