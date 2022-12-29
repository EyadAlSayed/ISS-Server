package com.iss.info.security.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocketService {
    @Autowired
    UserService userService;

    public String getChatIpByPhoneNumber(String phoneNumber){
       return userService.getUserIpByPhoneNumber(phoneNumber);
    }

    public void updateUserIp(String phoneNumber,String ip){
     userService.updateUserIp(phoneNumber, ip);
    }


}
