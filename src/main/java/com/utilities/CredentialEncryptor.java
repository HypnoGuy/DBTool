package com.utilities;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;

/**
 * Utility class for encrypting and decrypting sensitive information like database passwords.
 * Uses Jasypt for encryption with a password-based encryption algorithm.
 */
public class CredentialEncryptor {

    private static final String DEFAULT_ENCRYPTION_KEY = "DBStatus_SecureKey";
    private static StandardPBEStringEncryptor encryptor;

    /**
     * Initialize the encryptor with the default encryption key.
     */
    static {
        initializeEncryptor(DEFAULT_ENCRYPTION_KEY);
    }

    /**
     * Initialize the encryptor with a custom encryption key.
     * 
     * @param encryptionKey The encryption key to use
     */
    public static void initializeEncryptor(String encryptionKey) {
        encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
        encryptor.setPassword(encryptionKey);
        encryptor.setIvGenerator(new RandomIvGenerator());
    }

    /**
     * Encrypts a plain text string and wraps it in the ENC() format.
     * 
     * @param plainText The text to encrypt
     * @return The encrypted text in ENC() format
     */
    public static String encrypt(String plainText) {
        if (plainText == null || plainText.isEmpty()) {
            return plainText;
        }

        // If the text is already encrypted, return it as is
        if (isEncrypted(plainText)) {
            return plainText;
        }

        // Encrypt the text and wrap it in the ENC() format
        return "ENC(" + encryptor.encrypt(plainText) + ")";
    }

    /**
     * Decrypts a string that may be in the ENC() format.
     * If the string is not in the ENC() format, it is returned as is.
     * 
     * @param text The text to decrypt, which may or may not be encrypted
     * @return The decrypted text
     */
    public static String decrypt(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        // If the text is not in the ENC() format, return it as is
        if (!isEncrypted(text)) {
            return text;
        }

        try {
            // Extract the encrypted text from the ENC() format
            String encryptedText = text.substring(4, text.length() - 1);
            return encryptor.decrypt(encryptedText);
        } catch (Exception e) {
            // If decryption fails, return the original text
            // This allows backward compatibility with unencrypted passwords
            return text;
        }
    }

    /**
     * Checks if a string is in the ENC() format.
     * 
     * @param text The text to check
     * @return true if the text is in the ENC() format, false otherwise
     */
    public static boolean isEncrypted(String text) {
        return text != null && text.startsWith("ENC(") && text.endsWith(")");
    }

    /**
     * Utility method to generate encrypted passwords for configuration files.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java com.dbstatus.util.CredentialEncryptor <password-to-encrypt>");
            return;
        }

        String plainPassword = args[0];
        String encryptedPassword = encrypt(plainPassword);

        System.out.println("Original password: " + plainPassword);
        System.out.println("Encrypted password: " + encryptedPassword);
        System.out.println("Decrypted password: " + decrypt(encryptedPassword));
    }
}