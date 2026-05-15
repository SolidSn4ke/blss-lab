package com.example.blsslab.jca;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;

import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import com.example.blsslab.model.doctype.DocTypes;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.ConnectionEvent;
import jakarta.resource.spi.ConnectionEventListener;
import jakarta.resource.spi.ConnectionRequestInfo;
import jakarta.resource.spi.LocalTransaction;
import jakarta.resource.spi.ManagedConnection;
import jakarta.resource.spi.ManagedConnectionMetaData;

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
    }

    @Override
    public Object getConnection(Subject subject, ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        return new ErpNextConnectionImpl(this);
    }

    @Override
    public void destroy() throws ResourceException {
        System.out.println("destroy");
    }

    @Override
    public void cleanup() throws ResourceException {
        System.out.println("clean up");
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
        restClient.post().uri(path).body(data).contentType(MediaType.APPLICATION_JSON).retrieve().toBodilessEntity();
    }

    void deleteDocument(DocTypes doctype, String documentName) {
        String path = String.format("/api/resource/%s/%s", doctype.type, documentName);
        restClient.delete().uri(path).retrieve().toBodilessEntity();
    }
}
