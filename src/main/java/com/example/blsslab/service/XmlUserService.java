package com.example.blsslab.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.blsslab.model.dto.UserDTO;
import com.example.blsslab.model.dto.UserXmlWrapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class XmlUserService {

    Map<String, UserDTO> users;
    XmlMapper mapper;
    UserXmlWrapper wrapper;
    PasswordEncoder encoder;

    @Value("${users.xml.path}")
    String pathToXmlUsers;

    @PostConstruct
    void init() {
        users = new HashMap<>();
        mapper = new XmlMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        File xmlFile = new File(pathToXmlUsers);

        if (xmlFile.exists()) {
            try {
                wrapper = mapper.readValue(xmlFile, UserXmlWrapper.class);
                if (wrapper.getUsers() != null) {
                    for (UserDTO user : wrapper.getUsers()) {
                        users.put(user.getUsername(), user);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PreDestroy
    void destroy() throws IOException {
        File xmlFile = new File(pathToXmlUsers);
        wrapper.setUsers(new ArrayList<>(users.values()));
        mapper.writeValue(xmlFile, wrapper);
    }

    public boolean checkIfPresent(String username) {
        return users.containsKey(username);
    }

    public boolean verifyPassword(String username, String password) {
        UserDTO user = getUserByUsername(username);
        if (user == null || !encoder.matches(password, user.getPassword())) {
            return false;
        } else
            return true;
    }

    public UserDTO getUserByUsername(String username) {
        if (checkIfPresent(username)) {
            return users.get(username);
        } else
            return null;
    }

    public boolean addUser(UserDTO user) {
        if (checkIfPresent(user.getUsername())) {
            return false;
        }
        user.setPassword(encoder.encode(user.getPassword()));
        users.put(user.getUsername(), user);
        return true;
    }

    public boolean deleteUser() {
        return true;
    }

    public boolean updateUser() {
        return true;
    }
}
