package com.web_application_development.evoting;

import com.web_application_development.evoting.dtos.VoteResultsDTO;
import com.web_application_development.evoting.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Controller
public class StatisticsController {

    private final VoteRepository voteRepository;

    @Autowired
    StatisticsController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @GetMapping("/statistics")
    public String showAllVotes(Model model) {
        List<Object[]> votesListObj = voteRepository.findAllVotes();
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
