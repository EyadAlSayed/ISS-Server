package com.iss.info.security.system.helper;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class EncryptionConverters {

    public static String convertByteToHexadecimal(byte[] byteArray) {
        String hex = "";

        // Iterating through each byte in the array
        for (byte i : byteArray) {
            hex += String.format("%02X", i);
        }

        return hex;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16)); //fixme: ...
        }
        return data;
    }

    public static SecretKey retrieveSymmetricSecretKey(String keyAsString){

        byte[] bytes = new BigInteger("7F" + keyAsString, 16).toByteArray();
        return new SecretKeySpec(bytes, 1, bytes.length-1, "AES");
    }

    public static PublicKey retrievePublicKey(String keyAsString) throws NoSuchAlgorithmException, InvalidKeySpecException {

        System.out.println("eyad " + keyAsString.length());

        keyAsString = "30820122300D06092A864886F70D01010105000382010F003082010A0282010100B90C59C529F306DC908086A5B7CE754A981FB29D4794EA4302F49D54164112D51F8084A436A12A365D419C22CE6B5E1C1E8F1DFE102B737820133E873B84F08160B8D16FFC299AB21C6BA1FBE1B81F3A5E595944DC8F8688B8076E671CAFCB20CA852E96D66BFDF999E11F9DE374D0CCD05C4309C4FF53D3E27DC98846CFBF0ED7690EA743A2FACDEEBA4A5E148A51D576988F5BE077075058411CA92A47E008A0B67215446C80E4B69EA8B473C27249BFBE97AB2537DF34926D4C200418A08933A4FB6643F9E6DAEB3226A1BD05316232DC42A225DD26D8B3EEE5FDA3FE1C22250B9107D7CBD363E83B705A8184886DDECF1D1167556EAF227273428994D4090203010001";
        byte[] byteKey = DatatypeConverter.parseHexBinary(keyAsString);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        return kf.generatePublic(new X509EncodedKeySpec(byteKey));
    }

    public static PrivateKey retrievePrivateKey(String keyAsString) throws NoSuchAlgorithmException, InvalidKeySpecException {

        byte[] keyData = hexStringToByteArray(keyAsString);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePrivate(new PKCS8EncodedKeySpec(keyData));
    }
}
