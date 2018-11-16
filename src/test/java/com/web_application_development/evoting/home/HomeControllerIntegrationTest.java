package com.web_application_development.evoting.home;

import com.web_application_development.evoting.configurations.SecurityConfiguration;
import com.web_application_development.evoting.controllers.HomeController;
import com.web_application_development.evoting.entities.Candidate;
import com.web_application_development.evoting.services.CandidateService;
import com.web_application_development.evoting.services.UserStatisticsService;
import com.web_application_development.evoting.services.VoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)
@Import(SecurityConfiguration.class)
public class HomeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SimpMessageSendingOperations messagingTemplate;
    @MockBean
    private VoteService voteService;
    @MockBean
    private UserStatisticsService userStatisticsService;
    @MockBean
    private CandidateService candidateService;

    @Test
    public void testModelIsPopulatedWithDataFromDB() throws Exception {
        List<Candidate> candidateList = new ArrayList<>();
        Candidate candidate = new Candidate();
        candidate.setFirstName("Mari");
        candidate.setLastName("Mets");
        candidate.setIdentityCode("12345678910");
        candidate.setId(1L);
        candidate.setRegion("Makumaa");
        candidate.setParty("Makud");
        candidate.setHasWithdrawn(0L);
        candidate.setCandidacyAnnounced(LocalDateTime.now());
        candidateList.add(candidate);

        when(candidateService.findAllCandidates()).thenReturn(candidateList);

        this.mockMvc.perform(get("/"))
                // just for debugging purpose
                // .andDo(print())
                .andExpect(content().string(containsString("Mari")))
                .andExpect(content().string(containsString("Mets")))
                .andExpect(content().string(containsString("1")))
                .andExpect(content().string(containsString("Makumaa")))
                .andExpect(content().string(containsString("Makud")))
                .andExpect(status().isOk())
                .andExpect(view().name("home/index"))
                .andReturn();
    }
}
