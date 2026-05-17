package com.example.blsslab.jca;

import com.example.blsslab.model.doctype.DocType;
import com.example.blsslab.model.doctype.DocTypes;

import jakarta.resource.cci.Connection;

public interface ErpNextConnection extends Connection {
    boolean isAlive();

    <T extends DocType> void createDocument(DocTypes doctype, T data);

    void deleteDocument(DocTypes doctype, String documentName);
}
