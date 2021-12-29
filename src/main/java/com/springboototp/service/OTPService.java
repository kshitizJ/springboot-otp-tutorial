package com.springboototp.service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import org.springframework.stereotype.Service;

@Service
public class OTPService {

    // assigning expiring time of OTP to 4 mininutes.
    private static final Integer EXPIRE_MINS = 4;

    // With the use of LoadingCache, the values are automatically loaded as a cache
    // which stored on your local system. Caches stored in the form of key-value
    // pairs.
    private LoadingCache<String, Integer> otpCache;

    /**
     * 
     * storing the caches as username which is key and OTP as a value.
     * 
     */
    public OTPService() {
        super();
        otpCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Integer>() {

                    @Override
                    public Integer load(String key) throws Exception {
                        return 0;
                    }

                });
    }

    /**
     * 
     * @param key: key here is username.
     * @return random generated OTP.
     * 
     */
    public int generatedOTP(String key) {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        otpCache.put(key, otp);
        return otp;
    }

    /**
     * 
     * @param key: get the OTP of the following key from the cache.
     * @return the OTP if it is there in the cache or else return 0.
     * 
     */
    public int getOTP(String key) {
        try {
            return otpCache.get(key);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 
     * @param key: delete the OTP from the cache.
     * 
     */
    public void clearOTP(String key) {
        otpCache.invalidate(key);
    }

}
