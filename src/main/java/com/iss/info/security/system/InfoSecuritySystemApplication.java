package com.iss.info.security.system;

import com.iss.info.security.system.helper.EncryptionKeysUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

import static com.iss.info.security.system.helper.EncryptionConverters.convertByteToHexadecimal;
import static com.iss.info.security.system.helper.EncryptionConverters.retrievePrivateKey;
import static com.iss.info.security.system.helper.EncryptionTools.do_RSADecryption;

@SpringBootApplication
public class InfoSecuritySystemApplication {

    public static String serverPublicKey;
    public static String serverPrivateKey;

    public static  KeyPair keyPair;

    public static void main(String[] args) throws Exception {
        generateAndStoreServerKeys();
        SpringApplication.run(InfoSecuritySystemApplication.class, args);
    }

    private static void generateAndStoreServerKeys() throws Exception {
         keyPair = EncryptionKeysUtils.generateRSAKeyPair();
        serverPublicKey = convertByteToHexadecimal(keyPair.getPublic().getEncoded());
        serverPrivateKey = convertByteToHexadecimal(keyPair.getPrivate().getEncoded());
        System.out.println(serverPublicKey);
        System.out.println(serverPrivateKey);
    }
}
