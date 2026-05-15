package com.example.blsslab.model.doctype;

public interface Convertable<T extends DocType> {
    T toDocType();
}
