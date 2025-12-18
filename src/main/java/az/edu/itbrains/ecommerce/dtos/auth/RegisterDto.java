package az.edu.itbrains.ecommerce.dtos.auth;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    @Size(min = 3, max = 20, message = "Name must be minimum 3 maximum 20 symbol")
    private String name;
    @Size(min = 3, max = 20, message = "Surname must be minimum 3 maximum 20 symbol")
    private String surname;
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email cannot be empty")
    private String email;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&*()-+=^])(?=\\S+$).{8,}$",
            message = "Password must be minimum 8 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special character (!@#$%&*()-+=^).")
    private String password;

    @NotBlank(message = "Confirm Password cannot be empty")
    private String confirmPassword;


    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }


}
