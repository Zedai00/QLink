package org.qlink;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TokenManager {
  private final int LENGTH = 8;
  private Set<String> activeTokens = new HashSet<>();

  public String generateToken() {
    String token;
    do {
      token = UUID.randomUUID().toString().replace("-", "").substring(0, LENGTH);
    } while (activeTokens.contains(token));
    activeTokens.add(token);
    System.out.println(token);
    return token;
  }

  public boolean contains(String token) {
    return activeTokens.contains(token);
  }

  public void removeToken(String token) {
    activeTokens.remove(token);
  }
}
