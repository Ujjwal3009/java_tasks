package com.epam.effective;

import java.util.Objects;

public class CacheEntry {
    private String data;

    public CacheEntry() {
    }

    public CacheEntry(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CacheEntry that)) return false;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        return "CacheEntry{" +
                "data='" + data + '\'' +
                '}';
    }
}
