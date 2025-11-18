package com.example.PetAdoption.interfAdaptadora.dtos;


import java.util.List;

public class PageResult<T> {
    public final int page;
    public final int size;
    public final long totalElements;
    public final int totalPages;
    public final List<T> content;

    public PageResult(int page, int size, long totalElements, int totalPages, List<T> content) {
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.content = content;
    }
}