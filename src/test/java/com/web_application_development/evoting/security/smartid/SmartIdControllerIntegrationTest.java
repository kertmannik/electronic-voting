package com.web_application_development.evoting.security.smartid;

import com.web_application_development.evoting.configurations.SecurityConfiguration;
import com.web_application_development.evoting.smartid.SmartIdController;
import com.web_application_development.evoting.smartid.SmartIdService;
import ee.sk.smartid.AuthenticationHash;
import ee.sk.smartid.AuthenticationIdentity;
import ee.sk.smartid.HashType;
import ee.sk.smartid.SmartIdAuthenticationResult;
import ee.sk.smartid.exception.DocumentUnusableException;
import ee.sk.smartid.exception.UserAccountNotFoundException;
import ee.sk.smartid.exception.UserRefusedException;
import ee.sk.smartid.rest.dao.NationalIdentity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.web_application_development.evoting.IntegrationTestUtils.getFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@RunWith(SpringRunner.class)
@WebMvcTest(SmartIdController.class)
@Import(SecurityConfiguration.class)
public class SmartIdControllerIntegrationTest {

    private static final String BASE64_HASH = "4V7/mFBc8qgUZlvwVrW0PkchKC/ESrdHD0PP1D8rq5mNGj246mIn3B0CRkHw+cAgftgOTdu8iqP4hjec/NP1gg==";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SmartIdService smartIdService;

    private MockHttpSession httpSession;

    private AuthenticationHash fixedAuthenticationHash;

    @Before
    public void setUpSmartIdControllerIntegrationTest() {
        httpSession = new MockHttpSession();
        fixedAuthenticationHash = new AuthenticationHash();
        fixedAuthenticationHash.setHashInBase64(BASE64_HASH);
        fixedAuthenticationHash.setHashType(HashType.SHA512);
    }

    @Test
    public void testStartAuthentication() throws Exception {
        when(smartIdService.getAuthenticationHash()).thenReturn(fixedAuthenticationHash);

        this.mockMvc.perform(post("/smart-id/authentication/start")
                .session(httpSession)
                .content(getFile("smart-id/request.json"))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                // just for debugging purpose
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(getFile("smart-id/response.json")))
                .andReturn();

        assertEquals(fixedAuthenticationHash, httpSession.getAttribute("authenticationHash"));
        assertEquals("EE", ((NationalIdentity) httpSession.getAttribute("nationalIdentity")).getCountryCode());
        assertEquals("12345678910", ((NationalIdentity) httpSession.getAttribute("nationalIdentity")).getNationalIdentityNumber());
    }

    @Test
    public void testPollAuthenticationResultSuccess() throws Exception {
        // Set-up mocked http session variables
        NationalIdentity nationalIdentity = new NationalIdentity();
        nationalIdentity.setCountryCode("EE");
        nationalIdentity.setNationalIdentityNumber("10987654321");

        httpSession.setAttribute("authenticationHash", fixedAuthenticationHash);
        httpSession.setAttribute("nationalIdentity", nationalIdentity);

        // mock smart-id client response
        SmartIdAuthenticationResult smartIdAuthenticationResult = new SmartIdAuthenticationResult();
        AuthenticationIdentity authenticationIdentity = new AuthenticationIdentity();
        authenticationIdentity.setSurName("Mets");
        authenticationIdentity.setCountry("EE");
        authenticationIdentity.setGivenName("Mari");
        authenticationIdentity.setIdentityCode("12345678910");
        smartIdAuthenticationResult.setAuthenticationIdentity(authenticationIdentity);
        when(smartIdService.authenticate(nationalIdentity, fixedAuthenticationHash)).thenReturn(smartIdAuthenticationResult);

        this.mockMvc.perform(post("/smart-id/authentication/poll")
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                // just for debugging purpose
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(getFile("smart-id/poll-response.json")))
                .andReturn();

        assertEquals(smartIdAuthenticationResult, httpSession.getAttribute("SMART_ID_AUTHENTICATION_RESULT"));
    }

    @Test
    public void testPollAuthenticationResultWithUserAccountNotFoundException() throws Exception {
        // Set-up mocked http session variables
        NationalIdentity nationalIdentity = new NationalIdentity();
        nationalIdentity.setCountryCode("EE");
        nationalIdentity.setNationalIdentityNumber("12345678910");

        httpSession.setAttribute("authenticationHash", fixedAuthenticationHash);
        httpSession.setAttribute("nationalIdentity", nationalIdentity);
        when(smartIdService.authenticate(nationalIdentity, fixedAuthenticationHash))
                .thenThrow(new UserAccountNotFoundException());

        this.mockMvc.perform(post("/smart-id/authentication/poll")
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                // just for debugging purpose
                // .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(getFile("smart-id/user-account-not-found-error-response.json")))
                .andReturn();
        assertNull(httpSession.getAttribute("SMART_ID_AUTHENTICATION_RESULT"));
    }

    @Test
    public void testPollAuthenticationResultWithUserRefusedException() throws Exception {
        // Set-up mocked http session variables
        NationalIdentity nationalIdentity = new NationalIdentity();
        nationalIdentity.setCountryCode("EE");
        nationalIdentity.setNationalIdentityNumber("12345678910");

        httpSession.setAttribute("authenticationHash", fixedAuthenticationHash);
        httpSession.setAttribute("nationalIdentity", nationalIdentity);
        when(smartIdService.authenticate(nationalIdentity, fixedAuthenticationHash))
                .thenThrow(new UserRefusedException());

        this.mockMvc.perform(post("/smart-id/authentication/poll")
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                // just for debugging purpose
                // .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(getFile("smart-id/user-refused-error-response.json")))
                .andReturn();
        assertNull(httpSession.getAttribute("SMART_ID_AUTHENTICATION_RESULT"));
    }

    @Test
    public void testPollAuthenticationResultWithDocumentUnusableException() throws Exception {
        // Set-up mocked http session variables
        NationalIdentity nationalIdentity = new NationalIdentity();
        nationalIdentity.setCountryCode("EE");
        nationalIdentity.setNationalIdentityNumber("12345678910");

        httpSession.setAttribute("authenticationHash", fixedAuthenticationHash);
        httpSession.setAttribute("nationalIdentity", nationalIdentity);
        when(smartIdService.authenticate(nationalIdentity, fixedAuthenticationHash))
                .thenThrow(new DocumentUnusableException());

        this.mockMvc.perform(post("/smart-id/authentication/poll")
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                // just for debugging purpose
                // .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(getFile("smart-id/document-unusable-error-response.json")))
                .andReturn();
        assertNull(httpSession.getAttribute("SMART_ID_AUTHENTICATION_RESULT"));
    }

}