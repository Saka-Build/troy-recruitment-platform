package com.troy.ats.entity;

import com.troy.ats.dto.RoleResponseDto;
import com.troy.ats.dto.RolesForTokenDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class TokenResponse {

    private String accessToken;
    private String refreshToken;
    private long expiresInSeconds;

    private RoleResponseDto activeRole;

    private List<RolesForTokenDto> roles;
}