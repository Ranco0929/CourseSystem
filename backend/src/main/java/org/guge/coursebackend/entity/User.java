package org.guge.coursebackend.entity;

import lombok.Getter;
import lombok.Setter;
import org.guge.coursebackend.entity.subentity.Role;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "user", catalog = "COURSESERVER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "id不能为空")
    private long userId;

    private String name;

    private boolean enable;

    private String info;

    private String avator;

    @Size(min = 6, max = 20, message = "密码长度应该在 6 到 20 之间。")
    private String password;

    @NotEmpty(message = "账号不能为空")
    @Email(message = "邮箱格式错误")
    private String email;

    private String token;

    private String verifiedCode;

    @Enumerated(EnumType.STRING)
    private Role role;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt=new Date();

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt=new Date();

    public boolean Enable() {
        return enable;
    }
}
