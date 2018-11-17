package com.web_application_development.evoting.controllers;

import com.web_application_development.evoting.dtos.VoteResultsDTO;
import com.web_application_development.evoting.entities.VoteResult;
import com.web_application_development.evoting.entities.VoteResultForParty;
import com.web_application_development.evoting.services.UserStatisticsService;
import com.web_application_development.evoting.services.VoteService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class StatisticsController {

    private static final Logger logger = LogManager.getLogger(StatisticsController.class);

    private final HttpServletRequest request;
    private final VoteService voteService;
    private final UserStatisticsService userStatisticsService;

    @Autowired
    public StatisticsController(HttpServletRequest request, VoteService voteService, UserStatisticsService userStatisticsService) {
        this.request = request;
        this.voteService = voteService;
        this.userStatisticsService = userStatisticsService;
    }

    @GetMapping("/statistics")
    public String showAllVotes(Model model) {
        logger.debug("Statistics page GET request");
        userStatisticsService.saveUserStatistics(request, "/statistics");

        List<VoteResult> votesListObj = voteService.findAllVotes();
        List<VoteResultsDTO> votesList = new ArrayList<>();
        for (VoteResult candidate : votesListObj) {
            VoteResultsDTO voteResult = mapEntityToDTO(candidate);
            votesList.add(voteResult);
        }

        List<VoteResultForParty> voteResultForPartyList = voteService.findAllVotesForEachParty();

        model.addAttribute("votes", votesList);
        model.addAttribute("votesForParty", voteResultForPartyList);
        model.addAttribute("selectedLanguage", LocaleContextHolder.getLocale());
        return "statistics/index";
    }

    private VoteResultsDTO mapEntityToDTO(VoteResult voteResult) {
        return new VoteResultsDTO(voteResult.getId(), voteResult.getFirstName(), voteResult.getLastName(), voteResult.getRegion(), voteResult.getParty(), voteResult.getCount());
    }
}
