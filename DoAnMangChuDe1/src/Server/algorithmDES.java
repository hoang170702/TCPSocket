package Server;

import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class algorithmDES {

    public static String DesEncrypt(String str, String secretKey) {
        try {
            byte[] keyBytes = secretKey.getBytes();
            byte[] data = str.getBytes();

            DESKeySpec keySpec = new DESKeySpec(keyBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            // thuat toan des , che do ma hoa cbc , phan dem 
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(keySpec.getKey()));

            byte[] result = cipher.doFinal(data);
            String encrypt = Base64.getEncoder().encodeToString(result);
            return encrypt;
        } catch (Exception e) {
            System.out.println("Exception:" + e.toString());
        }
        return null;
    }

    public static String DesDecrypt(String str, String secretKey) {
        try {
            byte[] keyBytes = secretKey.getBytes();

            byte[] data = Base64.getDecoder().decode(str);
            DESKeySpec keySpec = new DESKeySpec(keyBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(keyBytes));

            byte[] decrypt = cipher.doFinal(data);
            return new String(decrypt);
        } catch (Exception e) {
            System.out.println("Exception:" + e.toString());
        }
        return null;
    }

}
