package org.apppooproject.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//Class to encrypt a given password using the cryptographic hash function MD5
public class Password {

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
