package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageContent<T> {
    private Long totalElements;
    private int currentPage;
    private int totalPageNumber;
    private List<T> content;
}
