package com.example.blsslab.jca;

import jakarta.resource.ResourceException;

public interface ErpNextConnectionFactory {
    ErpNextConnection getConnection() throws ResourceException;
}
