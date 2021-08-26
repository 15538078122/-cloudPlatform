package com.hd.microauservice.service;

public interface AuthService {
    Boolean auth(String account, String scopes, String uri, String method, String enterpriseId);
}
