// 在 common 模块中创建
package com.example.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String nickname;
    private String phone;
    private String avatar;
    private Integer status;
    private String createdAt;
    private String updatedAt;
}