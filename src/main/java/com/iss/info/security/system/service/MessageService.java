package com.iss.info.security.system.service;

import com.iss.info.security.system.repo.MessageRepo;
import com.iss.info.security.system.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class MessageService {

    @Autowired
    private MessageRepo messageRepo;


}
