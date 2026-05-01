package com.example.blsslab.security;

import java.io.IOException;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import com.example.blsslab.security.principals.*;

import com.example.blsslab.config.ApplicationContextHolder;
import com.example.blsslab.service.XmlUserService;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JaasLoginModule implements LoginModule {

    XmlUserService xmlUserService;

    private String username;
    Subject subject;
    CallbackHandler callbackHandler;
    Map<String, ?> sharedState;
    Map<String, ?> options;

    boolean loginSuccess = false;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState,
            Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        this.options = options;
        xmlUserService = ApplicationContextHolder.getBean(XmlUserService.class);
    }

    @Override
    public boolean login() throws LoginException {
        System.out.println("Login attempt");
        NameCallback nameCallback = new NameCallback("username: ");
        PasswordCallback passwordCallback = new PasswordCallback("password: ", false);
        try {
            callbackHandler.handle(new Callback[] { nameCallback, passwordCallback });
            username = nameCallback.getName();
            String password = new String(passwordCallback.getPassword());
            if (xmlUserService.verifyPassword(username, password)) {
                loginSuccess = true;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedCallbackException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return loginSuccess;
    }

    @Override
    public boolean commit() throws LoginException {
        System.out.println("Commit attempt");
        if (!loginSuccess) {
            return false;
        }
        subject.getPrincipals().add(new UserPrincipal(username));

        if (username.contains("moder")) {
            subject.getPrincipals().add(new RolePrincipal("ROLE_MODER"));
        } else {
            subject.getPrincipals().add(new RolePrincipal("ROLE_USER"));
        }
        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        System.out.println("Abort attempt");
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        System.out.println("Logout attempt");
        return true;
    }
}
