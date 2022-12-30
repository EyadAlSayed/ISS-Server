package com.iss.info.security.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

import static com.iss.info.security.system.helper.EncryptionKeysUtils.getServerPublicKeyFromFile;

@Controller
public class EncryptionController {

    @GetMapping("get/server/public/key")
    public String getServerPublicKey() throws IOException {
        return getServerPublicKeyFromFile();
    }
}
