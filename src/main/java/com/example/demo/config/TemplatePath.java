package com.example.demo.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TemplatePath {

    // Pebble 파일 있는 곳에 위치를 Enum으로 설정 하드코딩 없애기
    ERROR_FILE_NOT_FOUND("pebble/hello"),
    COMMON_HEADER("commonHeader"),
    PEBBLE_SAMPLE("sample/pebbleSample");
    private String url;
}
