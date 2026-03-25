package com.noc.rest_api.config;

public interface TestConfigs {
    public int SERVER_PORT = 8888;

    String HEADER_PARAM_AUTHORIZATION = "Authorization";
    String HEADER_PARAM_ORIGIN = "Origin";

    String ORIGIN_VALID = "http://localhost:8080";
    String ORIGIN_INVALID = "http://localhost:8889";
}
