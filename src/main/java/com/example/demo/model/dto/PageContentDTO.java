package com.example.demo.model.dto;

import java.util.List;

public record PageContentDTO<T>(Long totalElements, int currentPage, int totalPageNumber, List<T> content) {
}
