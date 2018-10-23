package com.web_application_development.evoting.controllers;

import com.web_application_development.evoting.dtos.VoteResultsDTO;
import com.web_application_development.evoting.services.UserStatisticsService;
import com.web_application_development.evoting.services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Controller
public class StatisticsController {

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
        userStatisticsService.saveUserStatistics(request,"/statistics");
        List<Object[]> votesListObj = voteService.findAllVotes();
        List<VoteResultsDTO> votesList = new ArrayList<>();
        for (Object[] candidate : votesListObj) {
            votesList.add(new VoteResultsDTO((Integer) candidate[0],
                    (String) candidate[1],
                    (String) candidate[2],
                    (String) candidate[3],
                    (String) candidate[4],
                    (BigInteger) candidate[5]));
        }

        model.addAttribute("votes", votesList);
        return "statistics/index";
    }
}
