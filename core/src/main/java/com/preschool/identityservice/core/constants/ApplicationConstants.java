package com.preschool.identityservice.core.constants;

public class ApplicationConstants {
    public static final String[] PUBLIC_URLS = {
        "/v1/authentication/", "/v1/authentication/internal/**", "/v1/authentication/login",
        //        "/v1/authentication/logout"
    };

    public static final String[] ADMIN_URLS = {"/v1/admin/**"};
}
