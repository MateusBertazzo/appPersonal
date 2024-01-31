package br.com.apppersonal.apppersonal.model.entitys;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "verification_codes")
public class VerificationCodeEntity extends BaseEntity{
    private String code;
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public VerificationCodeEntity(String code, UserEntity user) {
        this.code = code;
        this.user = user;
    }

    public VerificationCodeEntity() {
    }

    public VerificationCodeEntity(UserEntity user) {
        this.user = user;
    }
}
