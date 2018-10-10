package com.web_application_development.evoting.controllers;

import com.web_application_development.evoting.dtos.CandidateDTO;
import com.web_application_development.evoting.repositories.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final CandidateRepository candidateRepository;

    @Autowired
    HomeController(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    @GetMapping("/")
    public String showAllVotes(Model model) {
        List<Object[]> candidateListObj = candidateRepository.findAllCandidates();
        List<CandidateDTO> candidateList = new ArrayList<>();
        for (Object[] candidate : candidateListObj) {
            candidateList.add(new CandidateDTO(String.valueOf(candidate[0]),
                    (String) candidate[1],
                    (String) candidate[2],
                    (String) candidate[3],
                    (String) candidate[4]));
        }
        model.addAttribute("candidates", candidateList);
        return "home/index";
    }
}
