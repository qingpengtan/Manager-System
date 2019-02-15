package com.example.config.redis;

public class IpKey extends BasePrefix{

    public static final int TOKEN_EXPIRE = 3600*24*360*100;
    private IpKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static IpKey ip = new IpKey(TOKEN_EXPIRE, "ip-");
}
