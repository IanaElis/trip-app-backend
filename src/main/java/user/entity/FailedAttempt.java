package user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

import java.time.Instant;

@Entity
@Table(name = "failed_login_attempt")
public class FailedAttempt {
    @Id
    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "attempts",nullable = false)
    private int attempts = 0;

    @Column(name = "first_attempt", nullable = false)
    private Instant firstAttempt;

    @Column(name = "last_attempt", nullable = false)
    private Instant lastAttempt;

    public void increment() {
        attempts++;
        lastAttempt = Instant.now();
    }

    public void reset() {
        attempts = 0;
    }

    public FailedAttempt(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAttempts() {
        return attempts;
    }

    public Instant getFirstAttempt() {
        return firstAttempt;
    }

    public void setFirstAttempt(Instant firstAttempt) {
        this.firstAttempt = firstAttempt;
    }

    public Instant getLastAttempt() {
        return lastAttempt;
    }
}
