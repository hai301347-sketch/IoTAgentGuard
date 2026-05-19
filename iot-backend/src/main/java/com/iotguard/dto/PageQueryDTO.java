package com.iotguard.dto;

import lombok.Data;

@Data
public class PageQueryDTO {
    private int page = 1;
    private int size = 10;
    private String keyword;

    public int getOffset() {
        return (page - 1) * size;
    }
}
