package com.example.blsslab.jca;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.example.blsslab.jca.exception.ErpNextDuplicateOperationException;
import com.example.blsslab.model.doctype.DocTypes;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.ConnectionEvent;
import jakarta.resource.spi.ConnectionEventListener;
import jakarta.resource.spi.ConnectionRequestInfo;
import jakarta.resource.spi.LocalTransaction;
import jakarta.resource.spi.ManagedConnection;
import jakarta.resource.spi.ManagedConnectionMetaData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErpNextManagedConnection implements ManagedConnection {

    private ErpNextManagedConnectionFactory managedConnectionFactory;
    RestClient restClient;
    private final List<ConnectionEventListener> listeners = new ArrayList<>();

    public ErpNextManagedConnection(ErpNextManagedConnectionFactory mcf, String apiKey, String apiSecret) {
        this.managedConnectionFactory = mcf;
        this.restClient = RestClient.builder()
                .baseUrl(managedConnectionFactory.getUrl())
                .defaultHeader("Authorization", String.format("token %s:%s", apiKey, apiSecret))
                .build();

        log.info("ERPNextManagedConnection created for URL={}", managedConnectionFactory.getUrl());
    }

    @Override
    public Object getConnection(Subject subject, ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        log.debug("Creating new ERPNextConnection handle");
        return new ErpNextConnectionImpl(this);
    }

    @Override
    public void destroy() throws ResourceException {
        log.warn("ERPNextManagedConnection destroyed");
    }

    @Override
    public void cleanup() throws ResourceException {
        log.debug("ERPNextManagedConnection cleanup called");
    }

    @Override
    public void associateConnection(Object connection) throws ResourceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'associateConnection'");
    }

    @Override
    public void addConnectionEventListener(ConnectionEventListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Connection listener is null");
        }
        listeners.add(listener);
    }

    @Override
    public void removeConnectionEventListener(ConnectionEventListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Connection listener is null");
        }
        listeners.remove(listener);
    }

    void closeHandle(ErpNextConnection connection) {
        log.debug("Closing connection handle");

        ConnectionEvent event = new ConnectionEvent(this, ConnectionEvent.CONNECTION_CLOSED);
        event.setConnectionHandle(connection);
        listeners.forEach(l -> l.connectionClosed(event));
    }

    @Override
    public XAResource getXAResource() throws ResourceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getXAResource'");
    }

    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLocalTransaction'");
    }

    @Override
    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMetaData'");
    }

    @Override
    public void setLogWriter(PrintWriter out) throws ResourceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setLogWriter'");
    }

    @Override
    public PrintWriter getLogWriter() throws ResourceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLogWriter'");
    }

    <T> void createDocument(DocTypes doctype, T data) {
        String path = String.format("/api/resource/%s", doctype.type);

        log.info("ERPNext CREATE request: doctype={}, url={}", doctype, path);
        log.debug("Payload: {}", data);

        try {
            restClient.post()
                    .uri(path)
                    .body(data)
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toBodilessEntity();

            log.info("ERPNext CREATE success: doctype={}", doctype);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatusCode.valueOf(409))) {
                log.warn("ERPNext duplicate document: doctype={}", doctype);
                throw new ErpNextDuplicateOperationException("This document is already created");
            }

            log.error("ERPNext CREATE failed: doctype={}, status={}", doctype, e.getStatusCode(), e);
            throw e;
        }
    }

    void deleteDocument(DocTypes doctype, String documentName) {
        String path = String.format("/api/resource/%s/%s", doctype.type, documentName);

        log.info("ERPNext DELETE request: doctype={}, name={}", doctype, documentName);

        try {
            restClient.delete()
                    .uri(path)
                    .retrieve()
                    .toBodilessEntity();

            log.info("ERPNext DELETE success: doctype={}, name={}", doctype, documentName);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatusCode.valueOf(404))) {
                log.warn("ERPNext delete target already removed: name={}", documentName);
                throw new ErpNextDuplicateOperationException("This document is already deleted");
            }

            log.error("ERPNext DELETE failed: doctype={}, name={}, status={}",
                    doctype, documentName, e.getStatusCode(), e);
            throw e;
        }
    }
}
