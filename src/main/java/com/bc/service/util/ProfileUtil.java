package com.bc.service.util;

import org.springframework.core.env.Environment;

/**
 * @author hp
 */
public final class ProfileUtil {
    
    public static final String DEVELOPMENT = "dev";
    public static final String STAGING = "staging";
    public static final String PRODUCTION = "prod";
    
    private ProfileUtil() {}
    
    public static boolean isProductionProfile(Environment env) {
        return isProfileActive(env, PRODUCTION);
    }

    public static boolean isStagingProfile(Environment env) {
        return isProfileActive(env, STAGING);
    }

    public static boolean isDevelopmentProfile(Environment env) {
        return isProfileActive(env, DEVELOPMENT);
    }

    public static boolean isAnyProfileActive(Environment env, String... profiles) {
        final String [] activeProfiles = env.getActiveProfiles();
        for(String activeProfile : activeProfiles) {
            if(containsIgnoreCase(activeProfile, profiles)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isProfileActive(Environment env, String profile) {
        final String [] activeProfiles = env.getActiveProfiles();
        return containsIgnoreCase(profile, activeProfiles);
    }

    private static boolean containsIgnoreCase(String s, String... arr) {
        for(String e : arr) {
            if(e.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }
}
