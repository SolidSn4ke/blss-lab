package com.example.blsslab.jca;

import javax.transaction.xa.XAResource;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.ActivationSpec;
import jakarta.resource.spi.BootstrapContext;
import jakarta.resource.spi.Connector;
import jakarta.resource.spi.ResourceAdapter;
import jakarta.resource.spi.ResourceAdapterInternalException;
import jakarta.resource.spi.endpoint.MessageEndpointFactory;

@Connector
public class ErpNextResourceAdapter implements ResourceAdapter {

    @Override
    public void start(BootstrapContext ctx) throws ResourceAdapterInternalException {
        System.out.println("ra: start");
    }

    @Override
    public void stop() {
        System.out.println("ra: stop");
    }

    @Override
    public void endpointActivation(MessageEndpointFactory endpointFactory, ActivationSpec spec)
            throws ResourceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'endpointActivation'");
    }

    @Override
    public void endpointDeactivation(MessageEndpointFactory endpointFactory, ActivationSpec spec) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'endpointDeactivation'");
    }

    @Override
    public XAResource[] getXAResources(ActivationSpec[] specs) throws ResourceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getXAResources'");
    }

}
