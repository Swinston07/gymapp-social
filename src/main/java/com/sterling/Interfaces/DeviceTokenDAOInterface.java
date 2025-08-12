package com.sterling.Interfaces;

import java.util.List;

public interface DeviceTokenDAOInterface {
    void upsertToken(int userId, String token, String platform);
    void revokeToken(int userId, String token);
    List<String> getActiveTokensForUser(int userId);
}
