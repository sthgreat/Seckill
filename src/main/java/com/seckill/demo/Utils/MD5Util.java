package com.seckill.demo.Utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    public static String inputPassToFormPass(String inputPass){
        String str = salt.charAt(0)+salt.charAt(3)+inputPass+salt.charAt(2)+salt.charAt(5);
        return md5(str);
    }

    public static String formPassToDBPass(String formPass, String salt){
        String str = salt.charAt(0)+salt.charAt(3)+ formPass +salt.charAt(2)+salt.charAt(5);
        return md5(str);
    }

    public static String inputPassToDbPass(String input, String saltDB){
        String formPass = inputPassToFormPass(input);
        return formPassToDBPass(formPass, saltDB);
    }
}
