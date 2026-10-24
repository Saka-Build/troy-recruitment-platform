package com.troy.ats.dto;

import com.troy.ats.enums.PermissionAction;
import com.troy.ats.enums.PermissionModule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreateRequest {

    private String roleName;
    private Map<String, List<String>> permissions;

}
