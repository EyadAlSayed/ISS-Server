package com.iss.info.security.system.helper;

import com.iss.info.security.system.InfoSecuritySystemApplication;
import com.iss.info.security.system.app.Constant;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

public class EncryptionKeysUtils {

    public static KeyPair generateRSAKeyPair() throws Exception {
        SecureRandom secureRandom = new SecureRandom();

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        keyPairGenerator.initialize(2048, secureRandom);

        return keyPairGenerator.generateKeyPair();
    }

    public static String getServerPublicKeyFromFile() throws IOException {
//        String publicKey;
//        FileInputStream fileInputStream = new FileInputStream(Constant.SERVER_KEYS_FILE_NAME);
//        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//        publicKey = objectInputStream.readUTF();
//        objectInputStream.close();
////        return publicKey;
        return InfoSecuritySystemApplication.serverKey;
    }

    public static String getServerPrivateKeyFromFile() throws IOException {
//        String privateKey;
//        FileInputStream fileInputStream = new FileInputStream(Constant.SERVER_KEYS_FILE_NAME);
//        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//        objectInputStream.readUTF();
//        privateKey = objectInputStream.readUTF();
//        objectInputStream.close();
        return InfoSecuritySystemApplication.privateKey;
    }
}
