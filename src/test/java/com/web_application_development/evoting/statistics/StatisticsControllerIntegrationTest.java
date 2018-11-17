package com.web_application_development.evoting.statistics;

import com.web_application_development.evoting.configurations.SecurityConfiguration;
import com.web_application_development.evoting.controllers.StatisticsController;
import com.web_application_development.evoting.entities.VoteResult;
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(StatisticsController.class)
@Import(SecurityConfiguration.class)
public class StatisticsControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SimpMessageSendingOperations messagingTemplate;
    @MockBean
    private VoteService voteService;
    @MockBean
    private UserStatisticsService userStatisticsService;

    @Test
    public void testStatisticsModelIsPopulatedWithDataFromDB() throws Exception {
        List<VoteResult> voteResultList = new ArrayList<>();
        VoteResult voteResult = new VoteResult();
        voteResult.setId(1);
        voteResult.setFirstName("Mari");
        voteResult.setLastName("Mets");
        voteResult.setParty("Makud");
        voteResult.setRegion("Makumaa");
        voteResult.setCount(BigInteger.valueOf(3));
        voteResultList.add(voteResult);
        when(voteService.findAllVotes()).thenReturn(voteResultList);
        this.mockMvc.perform(get("/statistics"))
                // just for debugging purpose
                // .andDo(print())
                .andExpect(content().string(containsString("Mari")))
                .andExpect(content().string(containsString("Mets")))
                .andExpect(content().string(containsString("1")))
                .andExpect(content().string(containsString("Makumaa")))
                .andExpect(content().string(containsString("Makud")))
                .andExpect(content().string(containsString("3")))
                .andExpect(status().isOk())
                .andExpect(view().name("statistics/index"))
                .andReturn();
    }
}
