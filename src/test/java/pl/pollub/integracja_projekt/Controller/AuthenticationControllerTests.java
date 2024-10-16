package pl.pollub.integracja_projekt.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.pollub.integracja_projekt.Config.AuthenticationRequest;
import pl.pollub.integracja_projekt.Config.AuthenticationResponse;
import pl.pollub.integracja_projekt.Config.RegisterRequest;
import pl.pollub.integracja_projekt.Controllers.AuthenticationController;
import pl.pollub.integracja_projekt.Services.AuthenticationService;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerTests {
    private MockMvc mockMvc;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    public void AuthenticationController_Register_ShouldReturnOk() throws Exception {
        RegisterRequest request = new RegisterRequest("test@example.com", "Password123");
        AuthenticationResponse response = new AuthenticationResponse("success", 200);

        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    public void AuthenticationController_Register_ShouldReturnBadRequest() throws Exception {
        RegisterRequest request = new RegisterRequest("test", "password123");
        AuthenticationResponse response = new AuthenticationResponse("Bad Request", 400);

        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void AuthenticationController_Authenticate_ShouldReturnOk() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "Password123");
        AuthenticationResponse response = new AuthenticationResponse("success", 200);

        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/authenticate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    public void AuthenticationController_Authenticate_ShouldReturnNotFound() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "Password123");

        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenThrow(new NoSuchElementException("Not found"));

        mockMvc.perform(post("/api/auth/authenticate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
