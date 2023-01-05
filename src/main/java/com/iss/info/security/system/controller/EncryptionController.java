package com.iss.info.security.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.iss.info.security.system.helper.EncryptionKeysUtils.getServerPublicKeyFromFile;

@RestController
public class EncryptionController {

    @GetMapping("/server/handshaking")
    public String getServerPublicKey() throws IOException {
        return getServerPublicKeyFromFile();
    }
}
