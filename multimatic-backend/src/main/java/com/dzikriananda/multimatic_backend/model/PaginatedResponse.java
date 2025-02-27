package com.dzikriananda.multimatic_backend.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class PaginatedResponse<T> {
    private List<T> data;
    private int currentPage;
    private int totalPages;
    private long totalRows;

    public PaginatedResponse(List<T> data,long totalRows,int offset) {
        int pageSize= 10;
        this.data = data;
        this.currentPage = (offset/pageSize) + 1;
        this.totalRows = totalRows;
        this.totalPages = (int) (totalRows / pageSize) + (totalRows % pageSize == 0 ? 0 : 1);
    }
}
