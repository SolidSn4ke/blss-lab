package com.example.blsslab.jca;

import jakarta.resource.ResourceException;
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

}
