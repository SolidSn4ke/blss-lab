package com.example.blsslab.jca;

import com.example.blsslab.model.doctype.DocType;
import com.example.blsslab.model.doctype.DocTypes;

public interface ErpNextConnection {
    boolean isAlive();

    void close();

    <T extends DocType> void createDocument(DocTypes doctype, T data);

    void deleteDocument(DocTypes doctype, String documentName);
}
