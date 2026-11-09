package com.nexride.service;

import java.security.*;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
  private static final int ITERATIONS = 210_000;

  public String hash(String password) {
    try {
      byte[] salt = new byte[16];
      SecureRandom.getInstanceStrong().nextBytes(salt);
      byte[] derived = derive(password, salt, ITERATIONS);
      return "pbkdf2$"
          + ITERATIONS
          + "$"
          + Base64.getEncoder().encodeToString(salt)
          + "$"
          + Base64.getEncoder().encodeToString(derived);
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException(e);
    }
  }

  public boolean matches(String password, String encoded) {
    try {
      String[] p = encoded.split("\\$");
      return p.length == 4
          && MessageDigest.isEqual(
              Base64.getDecoder().decode(p[3]),
              derive(password, Base64.getDecoder().decode(p[2]), Integer.parseInt(p[1])));
    } catch (Exception e) {
      return false;
    }
  }

  private byte[] derive(String value, byte[] salt, int iterations) throws GeneralSecurityException {
    PBEKeySpec spec = new PBEKeySpec(value.toCharArray(), salt, iterations, 256);
    try {
      return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
    } finally {
      spec.clearPassword();
    }
  }
}
