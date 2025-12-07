package sudarshangc.com.UserUnboardingApi.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username",unique = true,nullable = false)
    private String userName;

    @NonNull
    @Column(name ="email", unique = true,nullable = false)
    private String email;

    @Column(name = "password",unique = true)
    private String password;

    @Column(name = "is_enabled")
    private Boolean is_enabled=false;//for email verification

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role= Role.USER;//default role

    //enum for roles
    public enum Role{
        USER, ADMIN
    }



}
