package com.noslen.lsaauthservice.constants;

public class MicroServiceConstants {

    public static final String LOGIN_MICROSERVICE = "/login-service/api/login";

    public static final String ADMIN_MICROSERVICE = "lsa-admin-service";
    public static final String BASE_API = "/api";

    public interface AdminMicroServiceConstants {
        String FETCH_ADMIN_BY_USERNAME = "/fetch-admin/{username}";
        String BASE_API = "/api";
    }
}
