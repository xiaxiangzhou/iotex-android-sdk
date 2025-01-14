package com.github.iotexproject.mobile.keystore;

import com.github.iotexproject.mobile.account.Account;
import com.github.iotexproject.mobile.account.IotexAccount;
import com.github.iotexproject.mobile.utils.Numeric;
import org.junit.Assert;
import org.junit.Test;

import static com.github.iotexproject.mobile.account.IotexAccountTest.*;


/**
 * keystore test.
 *
 * @author Yang XuePing
 */
public class KeystoreTest {
    private static final String TEST_KEYSTORE = "{\"address\":\"io187wzp08vnhjjpkydnr97qlh8kh0dpkkytfam8j\",\"id\":\"91cd0053-ab75-48f3-8532-f3b421ba12c0\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"5a2fadcc2c20666e43f078ee8a55b09f798be10f3dd12b1394eeee29f79bf193\",\"cipherparams\":{\"iv\":\"c54b9f15e6bff1087362c23b358e2992\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"69a9fa00faedbd1a92065ad8007254fdd7c84d19233bb50d95baf32829a1c152\"},\"mac\":\"1cbc221fb1bbd34970523d8d2587c737521e36dabe9e1bb343aabf99c90886d9\"}}";

    private static final String TEST_KEYSTORE_JS = "{\"version\":3,\"id\":\"dd66ba63-83d8-43d5-ac6f-d79a06394d02\",\"address\":\"3f9c20bcec9de520d88d98cbe07ee7b5ded0dac4\",\"crypto\":{\"ciphertext\":\"b15eaee8da76a4eb3a28ae6e8f6ba96641dc1a433d3d7d2ea87d57b95a27b494\",\"cipherparams\":{\"iv\":\"6a3aac2b8b73a04ef6236edcfff27fc3\"},\"cipher\":\"aes-128-ctr\",\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"salt\":\"d8ca15c771459d43ac75a44893f7ce8949c5e87c07195cb9b0eda10db1409a1a\",\"n\":262144,\"r\":8,\"p\":1},\"mac\":\"5b4400e2b49ba23f75c1bac6724d8759b4870c5c5634e36c33e694cdcf4f363b\"}}";

    @Test
    public void testCreateWalletFileByExistKeyPair() {
        Account account = IotexAccount.create(Numeric.hexStringToByteArray(TEST_PRIVATE));
        KeystoreFile walletFile = Keystore.createStandard("123456", Numeric.toBigInt(account.privateKey()));
        Assert.assertNotNull(walletFile.toJsonString());
    }

    @Test
    public void testLoadKeystore() {
        Account account = IotexAccount.create(KeystoreUtils.loadKeyFromWalletFile("123456", TEST_KEYSTORE));
        Assert.assertNotNull(account);
        Assert.assertEquals(TEST_ADDRESS, account.address());
        Assert.assertEquals(TEST_PRIVATE, Numeric.toHexString(account.privateKey()));
        Assert.assertEquals(TEST_PUBLIC, Numeric.toHexString(account.publicKey()));



        KeystoreFile walletFile =  KeystoreUtils.createWalletFileByAcount("abc123CBA%$", account);
        System.out.println(walletFile.toJsonString());
    }

    @Test
    public void testLoadErrorPassword() {
        try {
            KeystoreUtils.loadKeyFromWalletFile("111111", TEST_KEYSTORE);
        } catch (CipherException e) {
            Assert.assertEquals("Invalid password provided", e.getMessage());
        }
    }

    @Test
    public void testLoadJSKeystore() {
        Account account = IotexAccount.create(KeystoreUtils.loadKeyFromWalletFile("iotexPassw0rd", TEST_KEYSTORE_JS));
        Assert.assertNotNull(account);
        Assert.assertEquals(TEST_ADDRESS, account.address());
        Assert.assertEquals(TEST_PRIVATE, Numeric.toHexString(account.privateKey()));
        Assert.assertEquals(TEST_PUBLIC, Numeric.toHexString(account.publicKey()));
    }
}
