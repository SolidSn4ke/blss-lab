package com.example.blsslab.jca;

import com.example.blsslab.model.doctype.DocType;
import com.example.blsslab.model.doctype.DocTypes;

import jakarta.resource.ResourceException;
import jakarta.resource.cci.ConnectionMetaData;
import jakarta.resource.cci.Interaction;
import jakarta.resource.cci.LocalTransaction;
import jakarta.resource.cci.ResultSetInfo;
import jakarta.resource.spi.ManagedConnection;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErpNextConnectionImpl implements ErpNextConnection {
    ErpNextManagedConnection managedConnection;

    public ErpNextConnectionImpl(ManagedConnection mc) {
        ErpNextManagedConnection erpmc = (ErpNextManagedConnection) mc;
        this.managedConnection = erpmc;

        log.debug("ERPNextConnectionImpl created");
    }

    @Override
    public boolean isAlive() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void close() {
        log.debug("Closing ERPNextConnection handle");
        managedConnection.closeHandle(this);
    }

    @Override
    public <T extends DocType> void createDocument(DocTypes doctype, T data) {
        log.info("ERPNext createDocument called: doctype={}", doctype);
        managedConnection.createDocument(doctype, data);
    }

    @Override
    public void deleteDocument(DocTypes doctype, String documentName) {
        log.info("ERPNext deleteDocument called: doctype={}", doctype);
        managedConnection.deleteDocument(doctype, documentName);
    }

    @Override
    public Interaction createInteraction() throws ResourceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createInteraction'");
    }

    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLocalTransaction'");
    }

    @Override
    public ConnectionMetaData getMetaData() throws ResourceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMetaData'");
    }

    @Override
    public ResultSetInfo getResultSetInfo() throws ResourceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getResultSetInfo'");
    }
}
