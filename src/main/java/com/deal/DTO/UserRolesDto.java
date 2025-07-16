package com.deal.Dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * dto for add new role to user
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRolesDto {

    private String login;

    private List<String> roles;

}
