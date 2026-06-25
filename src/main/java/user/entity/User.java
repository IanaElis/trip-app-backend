package user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.Instant;

//@Entity
//@Table("users")
public class User {
//    @Id
//    @GeneratedValue
    private Long id;

//    @Column("")
    private String firstName;

//    @Column("")
    private String lastName;

//    @Email
//    @Column("")
    private String email;

//    @Enumerated(EnumType.STRING)
//    @Column("")
    private Role role;

//    @Column("")
    private String username;

//    @Column("")
    private String password;

//    @Column("")
    private Status status;

 //   @Column(name = "created_at",
 //           insertable = false,
 //           updatable = false,
 //           nullable = false)
    private Instant createdAt;

    public User() {
    }

    public Long getId(){
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getCreatedAt(){
        return createdAt;
    }

}
