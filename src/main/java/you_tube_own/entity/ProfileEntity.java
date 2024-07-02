package you_tube_own.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import you_tube_own.enums.ProfileRole;
import you_tube_own.enums.Status;

import java.time.LocalDateTime;
@Setter
@Getter
@Entity
@Table(name = "profile")
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "visible")
    private Boolean visible = Boolean.TRUE;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private ProfileRole role;

    @Column(name = "photo_id")
    private String photoId;

}
