package com.sterling.Services;

import java.util.List;

import com.sterling.Interfaces.DeviceTokenDAOInterface;

public class DeviceTokenService {
    private final DeviceTokenDAOInterface deviceTokenDao;

    public DeviceTokenService(DeviceTokenDAOInterface deviceTokenDao){
        this.deviceTokenDao = deviceTokenDao;
    }

    public void registerToken(int userId, String token, String platform){
        // Basic validation
        if(token == null || token.isBlank()) throw new IllegalArgumentException("token required");
        if(platform == null) throw new IllegalArgumentException("platform required");
        String p = platform.toLowerCase();
        if(!p.equals("ios") && !p.equals("android") && !p.equals("web"))
            throw new IllegalArgumentException("platform must be ios|android|web");

        deviceTokenDao.upsertToken(userId, token, p);
    }

    public void revokeToken(int userId, String token){
        deviceTokenDao.revokeToken(userId, token);
    }

    public List<String> getActiveTokens(int userId){
        return deviceTokenDao.getActiveTokensForUser(userId);
    }
}
