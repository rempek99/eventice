package com.zimnyciechan.eventice.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.zimnyciechan.eventice.auth.exceptions.UserNotFoundException;
import com.zimnyciechan.eventice.auth.services.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTests {

    private static final String LOGIN_URL = "/login";
    private static final String REGISTER_URL = "/register";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private final String TEST_USERNAME = "testuser";
    private final String TEST_PASSWORD = "testpassword";
    private final String TEST_EMAIL = "testuser@example.com";

    @Test
    public void contextLoads() {
        assertNotNull(mockMvc);
    }

    @BeforeEach
    public void setUp() {
        // Delete test user if exists
        try {
            userService.deleteUserByUsername(TEST_USERNAME);
        } catch (UserNotFoundException e) {
            // User might not exist, ignore the exception
        }
        assertThrowsExactly(UserNotFoundException.class, () -> {
            userService.loadUserByUsername(TEST_USERNAME);
        });
    }

    @Test
    public void testLoginFailed() throws Exception {
        String body = """
                    {"username":"%s","password":"%s"}
                """.formatted(TEST_USERNAME, TEST_PASSWORD);

        var response = mockMvc.perform(post(LOGIN_URL)
                .contentType("application/json")
                .content(body))
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    public void registerUserMissingEmailFailed() throws Exception {
        String body = """
                    {
                     "username":"%s",
                     "password":"%s"
                    }
                """.formatted(TEST_USERNAME, TEST_PASSWORD);

        var response = mockMvc.perform(post(REGISTER_URL)
                .contentType("application/json")
                .content(body))
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("A required field is missing.", response.getContentAsString());
    }

    @Test
    public void registerUserMissingUsernameFailed() throws Exception {
        String body = """
                    {
                     "password":"%s",
                     "email":"%s"
                    }
                """.formatted(TEST_PASSWORD, TEST_EMAIL);

        var response = mockMvc.perform(post(REGISTER_URL)
                .contentType("application/json")
                .content(body))
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("A required field is missing.", response.getContentAsString());
    }

    @Test
    public void registerUserMissingPasswordFailed() throws Exception {
        String body = """
                    {
                     "username":"%s",
                     "email":"%s"
                    }
                """.formatted(TEST_USERNAME, TEST_EMAIL);
        var response = mockMvc.perform(post(REGISTER_URL)
                .contentType("application/json")
                .content(body))
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("Password is required", response.getContentAsString());
    }

    @Test
    public void registerUser() throws Exception {
        String body = """
                    {
                     "username":"%s",
                     "password":"%s",
                     "email":"%s"
                    }
                """.formatted(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
        var response = mockMvc.perform(post(REGISTER_URL)
                .contentType("application/json")
                .content(body))
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertNotNull(response.getContentAsString());
        assertTrue(response.getContentAsString().contains("User created with ID: "));

        String userId = response.getContentAsString().split("ID: ")[1];
        assertNotNull(userId);

        // TODO: check user exists in the database, and has 3 authorities?
    }

    @Test
    public void registerAndLoginUser() throws Exception {
        String body = """
                    {
                     "username":"%s",
                     "password":"%s",
                     "email":"%s"
                    }
                """.formatted(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
        var registerResponse = mockMvc.perform(post(REGISTER_URL)
                .contentType("application/json")
                .content(body))
                .andReturn()
                .getResponse();
        assertEquals(HttpStatus.CREATED.value(), registerResponse.getStatus());
        var loginResponse = mockMvc.perform(post(LOGIN_URL)
                .contentType("application/json")
                .content("""
                            {
                             "username":"%s",
                             "password":"%s"
                            }
                        """.formatted(TEST_USERNAME, TEST_PASSWORD)))
                .andReturn()
                .getResponse();
        assertEquals(HttpStatus.OK.value(), loginResponse.getStatus());
        assertNotNull(loginResponse.getContentAsString());
        assertTrue(loginResponse.getContentAsString().length() > 100); // JWT
    }

    @Test
    public void jwtClaims() throws Exception {
        String body = """
                    {
                     "username":"%s",
                     "password":"%s",
                     "email":"%s"
                    }
                """.formatted(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
        var registerResponse = mockMvc.perform(post(REGISTER_URL)
                .contentType("application/json")
                .content(body))
                .andReturn()
                .getResponse();
        assertEquals(HttpStatus.CREATED.value(), registerResponse.getStatus());
        var loginResponse = mockMvc.perform(post(LOGIN_URL)
                .contentType("application/json")
                .content("""
                            {
                             "username":"%s",
                             "password":"%s"
                            }
                        """.formatted(TEST_USERNAME, TEST_PASSWORD)))
                .andReturn()
                .getResponse();
        assertEquals(HttpStatus.OK.value(), loginResponse.getStatus());
        assertNotNull(loginResponse.getContentAsString());
        assertTrue(loginResponse.getContentAsString().length() > 100); // JWT

        String jwt = loginResponse.getContentAsString();
        String[] jwtParts = jwt.split("\\.");
        assertEquals(3, jwtParts.length);

        String claimsJson = new String(java.util.Base64.getUrlDecoder().decode(jwtParts[1]));
        assertTrue(claimsJson.contains("\"sub\":\"" + TEST_USERNAME + "\""));
    }

    @Test
    public void registerDuplicateUserFailed() throws Exception {
        String body = """
                    {
                     "username":"%s",
                     "password":"%s",
                     "email":"%s"
                    }
                """.formatted(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
        var registerResponse = mockMvc.perform(post(REGISTER_URL)
                .contentType("application/json")
                .content(body))
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.CREATED.value(), registerResponse.getStatus());

        body = """
                    {
                     "username":"%s",
                     "password":"%s",
                     "email":"%s"
                    }
                """.formatted(TEST_USERNAME + "A", TEST_PASSWORD, TEST_EMAIL);

        var secondResponse = mockMvc.perform(post(REGISTER_URL)
                .contentType("application/json")
                .content(body))
                .andReturn()
                .getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), secondResponse.getStatus());

        assertEquals("Email address already exists.", secondResponse.getContentAsString());

        body = """
                    {
                     "username":"%s",
                     "password":"%s",
                     "email":"%s"
                    }
                """.formatted(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL + "A");
        var thirdResponse = mockMvc.perform(post(REGISTER_URL)
                .contentType("application/json")
                .content(body))
                .andReturn()
                .getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), thirdResponse.getStatus());
        assertEquals("Username already exists.", thirdResponse.getContentAsString());
    }
}
