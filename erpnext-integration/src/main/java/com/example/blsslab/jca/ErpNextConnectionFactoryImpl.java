package com.example.blsslab.jca;

import javax.naming.NamingException;
import javax.naming.Reference;

import jakarta.resource.ResourceException;
import jakarta.resource.cci.Connection;
import jakarta.resource.cci.ConnectionSpec;
import jakarta.resource.cci.RecordFactory;
import jakarta.resource.cci.ResourceAdapterMetaData;
import jakarta.resource.spi.ConnectionManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ErpNextConnectionFactoryImpl implements ErpNextConnectionFactory {

    final ConnectionManager connectionManager;
    final ErpNextManagedConnectionFactory managedConnectionFactory;

    @Override
    public ErpNextConnection getConnection() throws ResourceException {
        if (connectionManager == null) {
            return new ErpNextConnectionImpl(managedConnectionFactory.createManagedConnection(null, null));
        }
        return (ErpNextConnection) connectionManager.allocateConnection(managedConnectionFactory, null);
    }

    @Override
    public Connection getConnection(ConnectionSpec properties) throws ResourceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getConnection'");
    }

    @Override
    public RecordFactory getRecordFactory() throws ResourceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRecordFactory'");
    }

    @Override
    public ResourceAdapterMetaData getMetaData() throws ResourceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMetaData'");
    }

    @Override
    public void setReference(Reference reference) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setReference'");
    }

    @Override
    public Reference getReference() throws NamingException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getReference'");
    }

}
