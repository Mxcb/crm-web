package com.crm.vo;

import java.util.List;

public class Pagination<T> {

    private Integer total;
    private List<T> dataList;

    public Pagination() {
    }

    public Pagination(Integer total, List<T> dataList) {
        this.total = total;
        this.dataList = dataList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "total=" + total +
                ", dataList=" + dataList +
                '}';
    }
}
