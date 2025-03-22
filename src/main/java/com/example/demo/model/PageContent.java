package com.example.demo.model;

import java.util.List;

public record PageContent<T>(Long totalElements, int currentPage, int totalPageNumber, List<T> content) {
}
