package com.futurekawa.backend.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class LotPageFromBrazil {

    private List<LotDto> content;

    private int number;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
}