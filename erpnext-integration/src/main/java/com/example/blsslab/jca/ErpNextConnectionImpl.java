package com.example.blsslab.jca;

import org.springframework.web.client.RestClient;

import jakarta.resource.spi.ManagedConnection;

public class ErpNextConnectionImpl implements ErpNextConnection {
    ErpNextManagedConnection managedConnection;
    RestClient restClient;

    public ErpNextConnectionImpl(ManagedConnection mc) {
        ErpNextManagedConnection erpmc = (ErpNextManagedConnection) mc;
        this.managedConnection = erpmc;
        this.restClient = erpmc.restClient;
    }

    @Override
    public boolean isAlive() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void close() {
        managedConnection.closeHandle(this);
    }
}
