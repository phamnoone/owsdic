package com.example.hongb_000.dictionaryows.PIII.DataController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;

/**
 * Created by phamn on 9/11/2015.
 */
public class DecryptData {
    Cipher dcipher;
    final static String strPassword = "p!a@t%a^h2*1$1#4";
    static SecretKeySpec key = new SecretKeySpec(strPassword.getBytes(), "AES");

    public DecryptData() {
        try {
            AlgorithmParameterSpec paramSpec =
                    new IvParameterSpec(strPassword.getBytes());
            dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeyException e) {
        } catch (InvalidAlgorithmParameterException ex) {
        } catch (NoSuchPaddingException e) {
        }
    }

    public String decrypt(String str) {
        try {
            // Decode base64 to get bytes
            //byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

            byte[] dec = new BASE64Decoder().decodeBuffer(str);
            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);
            // Decode using utf-8
            return new String(utf8, "UTF8");
        } catch (BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        }
        return null;
    }

}
