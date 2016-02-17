package org.rest.webapp.Services;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by Ivan on 2/17/2016.
 */
public class EncryptionService {

    public static String generatePassHash(String password) {
        byte[] bytesOfMessage=null;
        String str = "";
        try {
            bytesOfMessage = password.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            bytesOfMessage = md.digest(bytesOfMessage);
            str = new String(bytesOfMessage, StandardCharsets.UTF_8);
            System.out.println(str);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("WTF NO ALGORITHM");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static String generateToken() {
        byte[] tokenByte = null;
        StringBuffer hexString=null;
        try {
            SecureRandom random = new SecureRandom();
            String rndString = new BigInteger(130, random).toString(32);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rndString.getBytes("UTF-8"));
            hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            System.out.println(hexString);
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            System.out.println("WTF");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }

}
