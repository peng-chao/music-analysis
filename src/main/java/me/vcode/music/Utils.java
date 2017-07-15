package me.vcode.music;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by peng_chao_b on 7/2/17.
 */
public class Utils {


    public static String getNumbers(String content) {
        if (isNullOrEmpty(content)) {
            return content;
        }

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }


    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }


    public static boolean isNullOrEmpty(String str) {
        return str == null || str == "";
    }

    // 加密
    public static class Encryp {

        public static String[] encryption() {
            //私钥，随机16位字符串（自己可改）
            String secKey = "cd859f54539b24b7";
            //String text = "{\"username\": \"\", \"rememberLogin\": \"true\", \"password\": \"\"}";
            String text = "{\"username\": \"\", \"rememberLogin\": \"true\", \"password\": \"\"}";

            String modulus = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7";
            String nonce = "0CoJUm6Qyw8W8jud";
            String pubKey = "010001";

            String params = encrypt(encrypt(text, nonce), secKey);

            StringBuffer stringBuffer = new StringBuffer(secKey);
            secKey = stringBuffer.reverse().toString();
            String hex = Hex.encodeHexString(secKey.getBytes());

            BigInteger bigInteger1 = new BigInteger(hex, 16);
            BigInteger bigInteger2 = new BigInteger(pubKey, 16);
            BigInteger bigInteger3 = new BigInteger(modulus, 16);

            BigInteger bigInteger4 = bigInteger1.pow(bigInteger2.intValue()).remainder(bigInteger3);
            String encSecKey = Hex.encodeHexString(bigInteger4.toByteArray());
            encSecKey = zfill(encSecKey, 256);

            return new String[]{params, encSecKey};
        }

        //AES加密
        public static String encrypt(String text, String secKey) {
            byte[] raw = secKey.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = null;
            try {
                cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            }
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
            try {
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }
            byte[] encrypted = new byte[0];
            try {
                encrypted = cipher.doFinal(text.getBytes());
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
            return Base64.encodeBase64String(encrypted);
        }

        //字符填充
        public static String zfill(String result, int n) {
            if (result.length() >= n) {
                result = result.substring(result.length() - n, result.length());
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = n; i > result.length(); i--) {
                    stringBuilder.append("0");
                }
                stringBuilder.append(result);
                result = stringBuilder.toString();
            }
            return result;
        }

    }

}
