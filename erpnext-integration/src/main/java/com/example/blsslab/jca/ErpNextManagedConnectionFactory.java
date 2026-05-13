package com.example.blsslab.jca;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.ConnectionDefinition;
import jakarta.resource.spi.ConnectionManager;
import jakarta.resource.spi.ConnectionRequestInfo;
import jakarta.resource.spi.ManagedConnection;
import jakarta.resource.spi.ManagedConnectionFactory;
import jakarta.resource.spi.ResourceAdapter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ConnectionDefinition(connectionFactory = ErpNextConnectionFactory.class, connectionFactoryImpl = ErpNextConnectionFactoryImpl.class, connection = ErpNextConnection.class, connectionImpl = ErpNextConnectionImpl.class)
public class ErpNextManagedConnectionFactory implements ManagedConnectionFactory {

    private String url;
    private String apiKey;
    private String apiSecret;
    private ResourceAdapter resourceAdapter;

    @Override
    public Object createConnectionFactory() throws ResourceException {
        return new ErpNextConnectionFactoryImpl(null, this);
    }

    @Override
    public Object createConnectionFactory(ConnectionManager connectionManager) throws ResourceException {
        return new ErpNextConnectionFactoryImpl(connectionManager, this);
    }

    @Override
    public ManagedConnection createManagedConnection(Subject arg0, ConnectionRequestInfo arg1)
            throws ResourceException {
        return new ErpNextManagedConnection(this, this.apiKey, this.apiSecret);
    }

    @Override
    public PrintWriter getLogWriter() throws ResourceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLogWriter'");
    }

    @Override
    public ManagedConnection matchManagedConnections(Set connectionSet, Subject subject,
            ConnectionRequestInfo cxRequestInfo)
            throws ResourceException {
        ManagedConnection result = null;
        Iterator<ManagedConnection> iterator = connectionSet.iterator();
        while (result == null && iterator.hasNext()) {
            ManagedConnection mc = iterator.next();
            // TODO дописать проверку на совпадение данных подключения
            if (mc instanceof ErpNextManagedConnection) {
                result = mc;
            }
        }
        return result;
    }

    @Override
    public void setLogWriter(PrintWriter arg0) throws ResourceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setLogWriter'");
    }

}
