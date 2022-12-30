package com.iss.info.security.system;

import com.iss.info.security.system.app.Constant;
import com.iss.info.security.system.helper.EncryptionConverters;
import com.iss.info.security.system.helper.EncryptionKeysUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.security.KeyPair;

@SpringBootApplication
public class InfoSecuritySystemApplication {

	public static void main(String[] args) throws Exception{
		generateAndStoreServerKeys();
		SpringApplication.run(InfoSecuritySystemApplication.class, args);
	}


	private static void generateAndStoreServerKeys() throws Exception {
		KeyPair keyPair = EncryptionKeysUtils.generateRSAKeyPair();

		FileOutputStream fileOutputStream = new FileOutputStream(Constant.SERVER_KEYS_FILE_NAME);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeUTF(EncryptionConverters.convertByteToHexadecimal(keyPair.getPublic().getEncoded()));
		objectOutputStream.writeUTF(EncryptionConverters.convertByteToHexadecimal(keyPair.getPrivate().getEncoded()));
		objectOutputStream.close();

	}
}
