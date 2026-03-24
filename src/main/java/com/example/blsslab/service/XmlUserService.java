package com.example.blsslab.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.example.blsslab.model.dto.UserDTO;
import com.example.blsslab.model.dto.UserXmlWrapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class XmlUserService {

    Map<String, UserDTO> users;
    XmlMapper mapper;
    UserXmlWrapper wrapper;

    @Value("${users.xml.path}")
    String pathToXmlUsers;

    public XmlUserService() {
        this.users = new HashMap<>();
        mapper = new XmlMapper();
    }

    @PostConstruct
    void init() {
        users = new HashMap<>();
        mapper = new XmlMapper();
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
