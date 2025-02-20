package com.postgresql.ytdemo2.model;

/*import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;


@Data
@Entity
@Table(name="dogs")


public class Dog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Add this line to auto-generate IDs
    private long id;

    public String sex;
    public String breed;
    private long age;
    public String colour;

}
*/
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@Data
@Entity
@Table(name="dogs")
public class Dog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Sex must not be empty")
    @Pattern(regexp = "^(male|female)$", message = "Sex must be male or female")
    private String sex;

    @NotBlank(message = "Breed must not be empty")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Breed must contain only letters")
    private String breed;

    @NotNull(message = "Age must not be empty")
    @Min(value = 0, message = "Age must be positive")
    private long age;

    @NotBlank(message = "Colour must not be empty")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Colour must contain only letters")
    private String colour;
}
