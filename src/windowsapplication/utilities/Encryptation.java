/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.utilities;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import windowsapplication.service.UserClientREST;

/**
 *
 * @author Adrián Corral
 */
public class Encryptation {
    
    private final static String CRYPTO_METHOD = "RSA";
    private static Cipher cipher;
    private static String publicKey;
    private final static String OPCION_RSA = "RSA/ECB/OAEPWithSHA1AndMGF1Padding";
    private static UserClientREST client = new UserClientREST();

    public static String encrypt(String messageToEncrypt) throws Exception {
        publicKey = client.getPublicKey();
        KeyFactory keyFactory = KeyFactory.getInstance(CRYPTO_METHOD);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(hexStringToByteArray(publicKey));
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        cipher = Cipher.getInstance(OPCION_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = cipher.doFinal(messageToEncrypt.getBytes());
        return toHexadecimal(encrypted);
    }

    public static String toHexadecimal(byte[] resumen) {
        String HEX = "";
        for (int i = 0; i < resumen.length; i++) {
            String h = Integer.toHexString(resumen[i] & 0xFF);
            if (h.length() == 1) {
                HEX += "0";
            }
            HEX += h;
        }
        return HEX.toUpperCase();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
