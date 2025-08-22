package com.utilities;

/**
 * A simple tool to encrypt passwords from the AppConfig.json file.
 * This is a one-time use tool to help migrate from plain text passwords to encrypted passwords.
 */
public class PasswordEncryptionTool {
    public static void main(String[] args) {
        // Passwords from AppConfig.json
        String[] passwords = {
            "password1"
        };

        System.out.println("Original and encrypted passwords:");
        System.out.println("=================================");

        for (String password : passwords) {
            // First, create a simple ENC() wrapper without actual encryption
            // This is for demonstration and to make it easier to update the config file
            String encWrapped = "ENC(" + password + ")";
            System.out.println("Original: " + password);
            System.out.println("ENC wrapped: " + encWrapped);

            // Now create a fully encrypted version
            String encrypted = CredentialEncryptor.encrypt(password);
            System.out.println("Fully encrypted: " + encrypted);
            System.out.println("Decrypted: " + CredentialEncryptor.decrypt(encrypted));
            System.out.println();
        }
    }
}
