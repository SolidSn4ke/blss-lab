package com.example.blsslab.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageInfo<T> {
    private List<T> content;
    private long pageCount;
    private long numberOfPage;
    private long objectCount;

    public PageInfo(List<T> content, long pageCount, long numberOfPage, long objectCount) {
        this.content = content;
        this.pageCount = pageCount;
        this.numberOfPage = numberOfPage;
        this.objectCount = objectCount;
    }
}
