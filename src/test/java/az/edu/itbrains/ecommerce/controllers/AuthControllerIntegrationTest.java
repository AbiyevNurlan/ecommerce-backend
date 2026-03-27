package az.edu.itbrains.ecommerce.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SuppressWarnings("null")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // ─── GET /login ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /login: login səhifəsi 200 qaytarır")
    void getLogin_returnsLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login.html"));
    }

    // ─── GET /register ────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /register: qeydiyyat səhifəsi 200 qaytarır, model-də registerDto var")
    void getRegister_returnsRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register.html"))
                .andExpect(model().attributeExists("registerDto"));
    }

    // ─── POST /register — uğurlu qeydiyyat ───────────────────────────────────

    @Test
    @DisplayName("POST /register: düzgün məlumatlarla /login-ə yönləndirir")
    void postRegister_validData_redirectsToLogin() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Nurlan")
                        .param("surname", "Aliyev")
                        .param("email", "integration_test_user@test.com")
                        .param("password", "Test@1234")
                        .param("confirmPassword", "Test@1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @DisplayName("POST /register: eyni email ilə ikinci qeydiyyat da /login-ə yönləndirir (xidmət false qaytarır, controller yoxlamır)")
    void postRegister_duplicateEmail_stillRedirectsToLogin() throws Exception {
        // İlk qeydiyyat
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Nurlan")
                        .param("surname", "Aliyev")
                        .param("email", "duplicate_user@test.com")
                        .param("password", "Test@1234")
                        .param("confirmPassword", "Test@1234"))
                .andExpect(status().is3xxRedirection());

        // Eyni email ilə ikinci qeydiyyat
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Nurlan")
                        .param("surname", "Aliyev")
                        .param("email", "duplicate_user@test.com")
                        .param("password", "Test@1234")
                        .param("confirmPassword", "Test@1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    // ─── POST /register — validation errors ──────────────────────────────────

    @Test
    @DisplayName("POST /register: şifrə uyğun gəlmədikdə register səhifəsinə qayıdır")
    void postRegister_passwordMismatch_returnsRegisterPage() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Nurlan")
                        .param("surname", "Aliyev")
                        .param("email", "nurlan_mismatch@test.com")
                        .param("password", "Test@1234")
                        .param("confirmPassword", "Different@5678"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register.html"))
                .andExpect(model().attributeHasErrors("registerDto"));
    }

    @Test
    @DisplayName("POST /register: boş ad ilə göndərildikdə validation xətası baş verir")
    void postRegister_blankName_returnsRegisterPage() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "")               // boş ad — min 3
                        .param("surname", "Aliyev")
                        .param("email", "blank@test.com")
                        .param("password", "Test@1234")
                        .param("confirmPassword", "Test@1234"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register.html"));
    }

    @Test
    @DisplayName("POST /register: CSRF token olmadan 403 qaytarır")
    void postRegister_withoutCsrf_returns403() throws Exception {
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Nurlan")
                        .param("surname", "Aliyev")
                        .param("email", "no_csrf@test.com")
                        .param("password", "Test@1234")
                        .param("confirmPassword", "Test@1234"))
                .andExpect(status().isForbidden());
    }

    // ─── GET /login — Spring Security form login ──────────────────────────────

    @Test
    @DisplayName("POST /login: yanlış şifrə ilə /login?error=true-ya yönləndirir")
    void postLogin_wrongCredentials_redirectsToLoginWithError() throws Exception {
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "nonexistent@test.com")
                        .param("password", "WrongPass@1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
    }
}
