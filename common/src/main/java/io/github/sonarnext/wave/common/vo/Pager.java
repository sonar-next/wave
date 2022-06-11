package io.github.sonarnext.wave.common.vo;

import io.github.sonar.next.wave.EnumProto;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Pager<T> {

    private Integer page;
    private Integer size;
    private List<T> data;

    public Pager() {
    }

    public Pager(Integer page, Integer size, List<T> data) {
        this.page = page;
        this.size = size;
        this.data = data;
    }

    public static <T> Pager<T> create(int i, int i1, List<T> emptyList) {
        return new Pager<>(i, i1, emptyList);
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Pager{" +
                "page=" + page +
                ", size=" + size +
                ", data=" + data +
                '}';
    }
}
