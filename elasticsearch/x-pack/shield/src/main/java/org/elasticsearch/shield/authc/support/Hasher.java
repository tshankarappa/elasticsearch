/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.shield.authc.support;

import org.elasticsearch.common.Base64;
import org.elasticsearch.common.Randomness;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Random;

/**
 *
 */
public enum Hasher {

    BCRYPT() {
        @Override
        public char[] hash(SecuredString text) {
            String salt = org.elasticsearch.shield.authc.support.BCrypt.gensalt();
            return BCrypt.hashpw(text, salt).toCharArray();
        }

        @Override
        public boolean verify(SecuredString text, char[] hash) {
            String hashStr = new String(hash);
            if (!hashStr.startsWith(BCRYPT_PREFIX)) {
                return false;
            }
            return BCrypt.checkpw(text, hashStr);
        }
    },

    BCRYPT4() {
        @Override
        public char[] hash(SecuredString text) {
            String salt = org.elasticsearch.shield.authc.support.BCrypt.gensalt(4);
            return BCrypt.hashpw(text, salt).toCharArray();
        }

        @Override
        public boolean verify(SecuredString text, char[] hash) {
            String hashStr = new String(hash);
            if (!hashStr.startsWith(BCRYPT_PREFIX)) {
                return false;
            }
            return BCrypt.checkpw(text, hashStr);
        }
    },

    BCRYPT5() {
        @Override
        public char[] hash(SecuredString text) {
            String salt = org.elasticsearch.shield.authc.support.BCrypt.gensalt(5);
            return BCrypt.hashpw(text, salt).toCharArray();
        }

        @Override
        public boolean verify(SecuredString text, char[] hash) {
            String hashStr = new String(hash);
            if (!hashStr.startsWith(BCRYPT_PREFIX)) {
                return false;
            }
            return BCrypt.checkpw(text, hashStr);
        }
    },

    BCRYPT6() {
        @Override
        public char[] hash(SecuredString text) {
            String salt = org.elasticsearch.shield.authc.support.BCrypt.gensalt(6);
            return BCrypt.hashpw(text, salt).toCharArray();
        }

        @Override
        public boolean verify(SecuredString text, char[] hash) {
            String hashStr = new String(hash);
            if (!hashStr.startsWith(BCRYPT_PREFIX)) {
                return false;
            }
            return BCrypt.checkpw(text, hashStr);
        }
    },

    BCRYPT7() {
        @Override
        public char[] hash(SecuredString text) {
            String salt = org.elasticsearch.shield.authc.support.BCrypt.gensalt(7);
            return BCrypt.hashpw(text, salt).toCharArray();
        }

        @Override
        public boolean verify(SecuredString text, char[] hash) {
            String hashStr = new String(hash);
            if (!hashStr.startsWith(BCRYPT_PREFIX)) {
                return false;
            }
            return BCrypt.checkpw(text, hashStr);
        }
    },

    BCRYPT8() {
        @Override
        public char[] hash(SecuredString text) {
            String salt = org.elasticsearch.shield.authc.support.BCrypt.gensalt(8);
            return BCrypt.hashpw(text, salt).toCharArray();
        }

        @Override
        public boolean verify(SecuredString text, char[] hash) {
            String hashStr = new String(hash);
            if (!hashStr.startsWith(BCRYPT_PREFIX)) {
                return false;
            }
            return BCrypt.checkpw(text, hashStr);
        }
    },

    BCRYPT9() {
        @Override
        public char[] hash(SecuredString text) {
            String salt = org.elasticsearch.shield.authc.support.BCrypt.gensalt(9);
            return BCrypt.hashpw(text, salt).toCharArray();
        }

        @Override
        public boolean verify(SecuredString text, char[] hash) {
            String hashStr = new String(hash);
            if (!hashStr.startsWith(BCRYPT_PREFIX)) {
                return false;
            }
            return BCrypt.checkpw(text, hashStr);
        }
    },

    SHA1() {
        @Override
        public char[] hash(SecuredString text) {
            byte[] textBytes = CharArrays.toUtf8Bytes(text.internalChars());
            MessageDigest md = SHA1Provider.sha1();
            md.update(textBytes);
            String hash = Base64.encodeBytes(md.digest());
            return (SHA1_PREFIX + hash).toCharArray();
        }

        @Override
        public boolean verify(SecuredString text, char[] hash) {
            String hashStr = new String(hash);
            if (!hashStr.startsWith(SHA1_PREFIX)) {
                return false;
            }
            byte[] textBytes = CharArrays.toUtf8Bytes(text.internalChars());
            MessageDigest md = SHA1Provider.sha1();
            md.update(textBytes);
            String passwd64 = Base64.encodeBytes(md.digest());
            String hashNoPrefix = hashStr.substring(SHA1_PREFIX.length());
            return SecuredString.constantTimeEquals(hashNoPrefix, passwd64);
        }
    },

    MD5() {
        @Override
        public char[] hash(SecuredString text) {
            MessageDigest md = MD5Provider.md5();
            md.update(CharArrays.toUtf8Bytes(text.internalChars()));
            String hash = Base64.encodeBytes(md.digest());
            return (MD5_PREFIX + hash).toCharArray();
        }

        @Override
        public boolean verify(SecuredString text, char[] hash) {
            String hashStr = new String(hash);
            if (!hashStr.startsWith(MD5_PREFIX)) {
                return false;
            }
            hashStr = hashStr.substring(MD5_PREFIX.length());
            MessageDigest md = MD5Provider.md5();
            md.update(CharArrays.toUtf8Bytes(text.internalChars()));
            String computedHashStr = Base64.encodeBytes(md.digest());
            return SecuredString.constantTimeEquals(hashStr, computedHashStr);
        }
    },

    SSHA256() {
        @Override
        public char[] hash(SecuredString text) {
            MessageDigest md = SHA256Provider.sha256();
            md.update(CharArrays.toUtf8Bytes(text.internalChars()));
            char[] salt = SaltProvider.salt(8);
            md.update(CharArrays.toUtf8Bytes(salt));
            String hash = Base64.encodeBytes(md.digest());
            char[] result = new char[SSHA256_PREFIX.length() + salt.length + hash.length()];
            System.arraycopy(SSHA256_PREFIX.toCharArray(), 0, result, 0, SSHA256_PREFIX.length());
            System.arraycopy(salt, 0, result, SSHA256_PREFIX.length(), salt.length);
            System.arraycopy(hash.toCharArray(), 0, result, SSHA256_PREFIX.length() + salt.length, hash.length());
            return result;
        }

        @Override
        public boolean verify(SecuredString text, char[] hash) {
            String hashStr = new String(hash);
            if (!hashStr.startsWith(SSHA256_PREFIX)) {
                return false;
            }
            hashStr = hashStr.substring(SSHA256_PREFIX.length());
            char[] saltAndHash = hashStr.toCharArray();
            MessageDigest md = SHA256Provider.sha256();
            md.update(CharArrays.toUtf8Bytes(text.internalChars()));
            md.update(new String(saltAndHash, 0, 8).getBytes(StandardCharsets.UTF_8));
            String computedHash = Base64.encodeBytes(md.digest());
            return SecuredString.constantTimeEquals(computedHash, new String(saltAndHash, 8, saltAndHash.length - 8));
        }
    },

    NOOP() {
        @Override
        public char[] hash(SecuredString text) {
            return text.copyChars();
        }

        @Override
        public boolean verify(SecuredString text, char[] hash) {
            return SecuredString.constantTimeEquals(text.internalChars(), hash);
        }
    };

    private static final String BCRYPT_PREFIX = "$2a$";
    private static final String SHA1_PREFIX = "{SHA}";
    private static final String MD5_PREFIX = "{MD5}";
    private static final String SSHA256_PREFIX = "{SSHA256}";

    public static Hasher resolve(String name, Hasher defaultHasher) {
        if (name == null) {
            return defaultHasher;
        }
        switch (name.toLowerCase(Locale.ROOT)) {
            case "bcrypt"       : return BCRYPT;
            case "bcrypt4"      : return BCRYPT4;
            case "bcrypt5"      : return BCRYPT5;
            case "bcrypt6"      : return BCRYPT6;
            case "bcrypt7"      : return BCRYPT7;
            case "bcrypt8"      : return BCRYPT8;
            case "bcrypt9"      : return BCRYPT9;
            case "sha1"         : return SHA1;
            case "md5"          : return MD5;
            case "ssha256"      : return SSHA256;
            case "noop"         :
            case "clear_text"   :  return NOOP;
            default:
                return defaultHasher;
        }
    }

    public static Hasher resolve(String name) {
        Hasher hasher = resolve(name, null);
        if (hasher == null) {
            throw new IllegalArgumentException("unknown hash function [" + name + "]");
        }
        return hasher;
    }

    public abstract char[] hash(SecuredString data);

    public abstract boolean verify(SecuredString data, char[] hash);

    static final class MD5Provider {

        private static final MessageDigest digest;

        static {
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("unsupported digest algorithm [MD5]. Please verify you are running on Java 7 or above", e);
            }
        }

        private static MessageDigest md5() {
            try {
                MessageDigest md5 = (MessageDigest) digest.clone();
                md5.reset();
                return md5;
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("could not create MD5 digest", e);
            }
        }
    }

    static final class SHA1Provider {

        private static final MessageDigest digest;

        static {
            try {
                digest = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("unsupported digest algorithm [SHA-1]", e);
            }
        }

        private static MessageDigest sha1() {
            try {
                MessageDigest sha1 = (MessageDigest) digest.clone();
                sha1.reset();
                return sha1;
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("could not create SHA-1 digest", e);
            }
        }
    }

    static final class SHA256Provider {

        private static final MessageDigest digest;

        static {
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("unsupported digest algorithm [SHA-256]. Please verify you are running on Java 7 or above", e);
            }
        }

        private static MessageDigest sha256() {
            try {
                MessageDigest sha = (MessageDigest) digest.clone();
                sha.reset();
                return sha;
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("could not create [SHA-256] digest", e);
            }
        }
    }

    static final class SaltProvider {

        static final char[] ALPHABET = new char[] {
                '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
                'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                'U',  'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
        };

        public static char[] salt(int length) {
            Random random = Randomness.get();
            char[] salt = new char[length];
            for (int i = 0; i < length; i++) {
                salt[i] = ALPHABET[(random.nextInt(ALPHABET.length))];
            }
            return salt;
        }
    }
}
