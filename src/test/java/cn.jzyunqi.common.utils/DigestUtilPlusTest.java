package cn.jzyunqi.common.utils;

import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;


class DigestUtilPlusTest {

    @Test
    public void serviceAlgorithmsTest() throws Exception {
        DigestUtilPlus.Info.serviceAlgorithms(DigestUtilPlus.AlgorithmService.MessageDigest).forEach(System.out::println);
    }

    @Test
    public void aesKeyIvEncodeTest1() throws Exception {
        String aesKey = DigestUtilPlus.AES.generateKey(256, true);
        String aesIv = DigestUtilPlus.AES.generateIv(true);

        System.out.println("秘钥 IV：" + aesKey + "  " + aesIv);

        String word = DigestUtilPlus.MD5.sign("hello word random key and iv", false);
        String ccc;
        String d;

        ccc = DigestUtilPlus.AES.encryptCBCPKCS7Padding(word.getBytes(), DigestUtilPlus.Base64.decodeBase64(aesKey), DigestUtilPlus.Base64.decodeBase64(aesIv), true);
        System.out.println("7Padding加密后的内容：" + ccc);

        d = DigestUtilPlus.AES.decryptCBCPKCS7Padding(DigestUtilPlus.Base64.decodeBase64(ccc), DigestUtilPlus.Base64.decodeBase64(aesKey), DigestUtilPlus.Base64.decodeBase64(aesIv));
        System.out.println("7Padding解密后的内容：" + d);
        Assertions.assertEquals(word,d);

        ccc = DigestUtilPlus.AES.encryptCBCPKCS5Padding(word.getBytes(), DigestUtilPlus.Base64.decodeBase64(aesKey), DigestUtilPlus.Base64.decodeBase64(aesIv), true);
        System.out.println("5Padding加密后的内容：" + ccc);

        d = DigestUtilPlus.AES.decryptCBCPKCS5Padding(DigestUtilPlus.Base64.decodeBase64(ccc), DigestUtilPlus.Base64.decodeBase64(aesKey), DigestUtilPlus.Base64.decodeBase64(aesIv));
        System.out.println("5Padding解密后的内容：" + d);
        Assertions.assertEquals(word,d);
    }

    @Test
    public void aesKeyIvEncodeTest2() throws Exception {
        String aesKey = DigestUtilPlus.AES.generateKey(256, true);
        String aesIv = DigestUtilPlus.AES.generateIv(true);

        System.out.println("秘钥 IV：" + aesKey + "  " + aesIv);

        String word = DigestUtilPlus.MD5.sign("hello word random key and iv", false);
        String ccc;
        String d;

        ccc = DigestUtilPlus.AES.encryptCBCNoPadding(word.getBytes(), DigestUtilPlus.Base64.decodeBase64(aesKey), DigestUtilPlus.Base64.decodeBase64(aesIv), true);
        System.out.println("NoPadding加密后的内容：" + ccc);

        d = DigestUtilPlus.AES.decryptCBCNoPadding(DigestUtilPlus.Base64.decodeBase64(ccc), DigestUtilPlus.Base64.decodeBase64(aesKey), DigestUtilPlus.Base64.decodeBase64(aesIv));
        System.out.println("NoPadding解密后的内容：" + d);
        Assertions.assertEquals(word,d);
    }

    @Test
    public void aesKeyIvHexTest1() throws Exception {
        String aesKey = DigestUtilPlus.AES.generateKey(256, false);
        String aesIv = DigestUtilPlus.AES.generateIv(false);

        System.out.println("秘钥 IV：" + aesKey + "  " + aesIv);

        String word = "hello word random key and iv";
        String ccc;
        String d;

        ccc = DigestUtilPlus.AES.encryptCBCPKCS7Padding(word.getBytes(), Hex.decodeHex(aesKey), Hex.decodeHex(aesIv), true);
        System.out.println("7Padding加密后的内容：" + ccc);

        d = DigestUtilPlus.AES.decryptCBCPKCS7Padding(DigestUtilPlus.Base64.decodeBase64(ccc), Hex.decodeHex(aesKey), Hex.decodeHex(aesIv));
        System.out.println("7Padding解密后的内容：" + d);
        Assertions.assertEquals(word,d);

        ccc = DigestUtilPlus.AES.encryptCBCPKCS5Padding(word.getBytes(), Hex.decodeHex(aesKey), Hex.decodeHex(aesIv), true);
        System.out.println("5Padding加密后的内容：" + ccc);

        d = DigestUtilPlus.AES.decryptCBCPKCS5Padding(DigestUtilPlus.Base64.decodeBase64(ccc), Hex.decodeHex(aesKey), Hex.decodeHex(aesIv));
        System.out.println("5Padding解密后的内容：" + d);
        Assertions.assertEquals(word,d);
    }

    @Test
    public void aesKeyIvHexTest2() throws Exception {
        String aesKey = DigestUtilPlus.AES.generateKey(256, false);
        String aesIv = DigestUtilPlus.AES.generateIv(false);

        System.out.println("秘钥 IV：" + aesKey + "  " + aesIv);

        String word = DigestUtilPlus.MD5.sign("hello word random key and iv", false);
        String ccc;
        String d;

        ccc = DigestUtilPlus.AES.encryptCBCNoPadding(word.getBytes(), Hex.decodeHex(aesKey), Hex.decodeHex(aesIv), true);
        System.out.println("NoPadding加密后的内容：" + ccc);

        d = DigestUtilPlus.AES.decryptCBCNoPadding(DigestUtilPlus.Base64.decodeBase64(ccc), Hex.decodeHex(aesKey), Hex.decodeHex(aesIv));
        System.out.println("NoPadding解密后的内容：" + d);
        Assertions.assertEquals(word,d);
    }

    @Test
    public void rsaKeyTest1() throws Exception {
        String[] key = DigestUtilPlus.RSA256.generateKey(1024, true);
        System.out.println("公钥：" + key[0]);
        System.out.println("私钥：" + key[1]);
        System.out.println("N：" + key[2]);
        System.out.println("e：" + key[3]);
        System.out.println("d：" + key[4]);
        //System.out.println("N：" + new BigInteger(DigestUtilPlus.Base64.decodeBase64(key[2])));
        //System.out.println("e：" + new BigInteger(DigestUtilPlus.Base64.decodeBase64(key[3])));
        //System.out.println("d：" + new BigInteger(DigestUtilPlus.Base64.decodeBase64(key[4])));
        String word = "hello word random rsa";
        String ccc;
        String d;

        ccc = DigestUtilPlus.RSA256.encryptPublicKey(word.getBytes(), DigestUtilPlus.Base64.decodeBase64(key[0]), true);
        System.out.println("公钥加密后的内容：" + ccc);

        d = DigestUtilPlus.RSA256.decryptPrivateKey(DigestUtilPlus.Base64.decodeBase64(ccc), DigestUtilPlus.Base64.decodeBase64(key[1]));
        System.out.println("私钥解密后的内容：" + d);
        Assertions.assertEquals(word,d);

        String fff = DigestUtilPlus.RSA256.signPrivateKey(word.getBytes(), DigestUtilPlus.Base64.decodeBase64(key[1]), true);
        System.out.println("私钥签名后的内容：" + fff);

        boolean rst = DigestUtilPlus.RSA256.verifySignPublicKey(word.getBytes(), fff, DigestUtilPlus.Base64.decodeBase64(key[0]));
        System.out.println("公钥验签后的结果：" + rst);
        Assertions.assertTrue(rst);
    }

    @Test
    public void rsaKeyTest2() throws Exception {
        String[] key = DigestUtilPlus.RSA256.generateKey(1024, true);
        System.out.println("公钥：" + key[0]);
        System.out.println("私钥：" + key[1]);
        System.out.println("N：" + key[2]);
        System.out.println("e：" + key[3]);
        System.out.println("d：" + key[4]);
        //System.out.println("N：" + new BigInteger(DigestUtilPlus.Base64.decodeBase64(key[2])));
        //System.out.println("e：" + new BigInteger(DigestUtilPlus.Base64.decodeBase64(key[3])));
        //System.out.println("d：" + new BigInteger(DigestUtilPlus.Base64.decodeBase64(key[4])));
        String word = "hello word random rsa";
        String ccc;
        String d;

        ccc = DigestUtilPlus.RSA256.encryptPublicKey(word.getBytes(), new BigInteger(DigestUtilPlus.Base64.decodeBase64(key[2])),  new BigInteger(DigestUtilPlus.Base64.decodeBase64(key[3])), true);
        System.out.println("公钥加密后的内容：" + ccc);

        d = DigestUtilPlus.RSA256.decryptPrivateKey(DigestUtilPlus.Base64.decodeBase64(ccc), new BigInteger(DigestUtilPlus.Base64.decodeBase64(key[2])),  new BigInteger(DigestUtilPlus.Base64.decodeBase64(key[4])));
        System.out.println("私钥解密后的内容：" + d);
        Assertions.assertEquals(word,d);

        String fff = DigestUtilPlus.RSA256.signPrivateKey(word.getBytes(), new BigInteger(DigestUtilPlus.Base64.decodeBase64(key[2])),  new BigInteger(DigestUtilPlus.Base64.decodeBase64(key[4])), true);
        System.out.println("私钥签名后的内容：" + fff);

        boolean rst = DigestUtilPlus.RSA256.verifySignPublicKey(word.getBytes(), fff, new BigInteger(DigestUtilPlus.Base64.decodeBase64(key[2])),  new BigInteger(DigestUtilPlus.Base64.decodeBase64(key[3])));
        System.out.println("公钥验签后的结果：" + rst);
        Assertions.assertTrue(rst);
    }

    @Test
    public void cmacTest() throws Exception {
        String waitEncode = "5B8C0C9FB449DE7D4F013ACD3821132A";
        String encode = DigestUtilPlus.Mac.sign(waitEncode, "b8ea10826a3c3d76b8ea10826a3c3d76", DigestUtilPlus.MacAlgo.C_AES, false);
        System.out.println(encode);
        byte[] encodeB = DigestUtilPlus.Mac.sign(waitEncode, "b8ea10826a3c3d76b8ea10826a3c3d76", DigestUtilPlus.MacAlgo.C_AES);
        System.out.println(DigestUtilPlus.Hex.encodeHexString(encodeB));
    }
}