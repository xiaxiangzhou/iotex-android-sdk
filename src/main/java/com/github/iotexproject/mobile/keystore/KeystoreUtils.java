package com.github.iotexproject.mobile.keystore;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.iotexproject.mobile.account.Account;
import com.github.iotexproject.mobile.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;

/**
 * keystore utils.
 *
 * @author Yang XuePing
 */
public class KeystoreUtils {
    static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * create keystore file by Account.
     *
     * @param password keystore password
     * @param account  account.
     * @return
     */
    public static KeystoreFile createWalletFileByAcount(String password, Account account) {
        return Keystore.createStandard(password, Numeric.toBigInt(account.privateKey()));
    }

    /**
     * create keystore file by private key.
     *
     * @param password   keystore password
     * @param privateKey private key
     * @return
     */
    public static KeystoreFile createWalletFileByKey(String password, BigInteger privateKey) {
        return Keystore.createStandard(password, privateKey);
    }

    /**
     * load private key from keystore.
     *
     * @param password keystore password
     * @param keystore keystore content
     * @return private key.
     */
    public static BigInteger loadKeyFromWalletFile(String password, String keystore) {
        try {
            KeystoreFile walletFile = objectMapper.readValue(keystore, KeystoreFile.class);
            return Keystore.decrypt(password, walletFile);
        } catch (IOException e) {
            throw new RuntimeException("read keystore json error", e);
        }
    }
}
