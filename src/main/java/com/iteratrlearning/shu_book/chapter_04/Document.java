package com.iteratrlearning.shu_book.chapter_04;

import java.util.Map;

public class Document {
    private final Map<String, String> attributes;

    public Document(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getAttributes(String attributeName) {
        return attributes.get(attributeName);
    }
}
