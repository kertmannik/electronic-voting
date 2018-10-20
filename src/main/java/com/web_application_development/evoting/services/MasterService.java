package com.web_application_development.evoting.services;

import com.web_application_development.evoting.dtos.CandidateDTO;
import com.web_application_development.evoting.dtos.UserStatisticsDTO;
import com.web_application_development.evoting.dtos.VoteDTO;
import com.web_application_development.evoting.entities.Candidate;
import com.web_application_development.evoting.entities.UserStatistics;
import com.web_application_development.evoting.entities.Vote;
import com.web_application_development.evoting.repositories.CandidateRepository;
import com.web_application_development.evoting.repositories.UserStatisticsRepository;
import com.web_application_development.evoting.repositories.VoteRepository;
import ee.sk.smartid.AuthenticationIdentity;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class MasterService {

    @Autowired
    private final CandidateRepository candidateRepository;

    @Autowired
    private final VoteRepository voteRepository;

    @Autowired
    private final UserStatisticsRepository userStatisticsRepository;

    MasterService(CandidateRepository candidateRepository, VoteRepository voteRepository, UserStatisticsRepository userStatisticsRepository) {
        this.candidateRepository = candidateRepository;
        this.voteRepository = voteRepository;
        this.userStatisticsRepository = userStatisticsRepository;
    }

    public List<Object[]> findAllCandidates() {
        return candidateRepository.findAllCandidates();
    }

    public List<Object[]> findAllVotes() {
        return voteRepository.findAllVotes();
    }

    public Candidate findCandidateById(Integer candidateId) {
        return candidateRepository.findById(candidateId);
    }

    public void saveCandidate(CandidateDTO candidateDTO, AuthenticationIdentity authIdentity) {
        // map DTO to entity
        Candidate entity = mapDTOToEntity(candidateDTO, authIdentity);
        // save new entity
        candidateRepository.save(entity);
    }

    public void saveVote(VoteDTO voteDTO, String voterId) {
        Vote entity = mapDTOToEntity(voteDTO, voterId);
        voteRepository.save(entity);
    }

    private Vote mapDTOToEntity(VoteDTO voteDTO, String voterId) {
        Vote voteEntity = new Vote();
        voteEntity.setVoterIdentityCode(voterId);
        voteEntity.setCandidateId(voteDTO.getCandidateId());
        voteEntity.setIsWithdrawn(0);
        voteEntity.setVotingTime(new Timestamp(System.currentTimeMillis()));
        return voteEntity;
    }

    private Candidate mapDTOToEntity(CandidateDTO candidateDTO, AuthenticationIdentity authIdentity) {
        Candidate candidateEntity = new Candidate();
        String givenName = authIdentity.getGivenName();
        String lastName = authIdentity.getSurName();
        candidateEntity.setFirstName(
                givenName.substring(0, 1).toUpperCase() + givenName.substring(1).toLowerCase());
        candidateEntity.setLastName(
                lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase());
        candidateEntity.setIdentityCode(authIdentity.getIdentityCode());
        candidateEntity.setRegion(candidateDTO.getRegion());
        candidateEntity.setParty(candidateDTO.getParty());
        candidateEntity.setHasWithdrawn(0);
        candidateEntity.setCandidacyAnnounced(new Timestamp(System.currentTimeMillis()));
        return candidateEntity;
    }

    private boolean sessionExists(String session_id) {
        return userStatisticsRepository.sessionExists(session_id) > 0;
    }

    private boolean ipLoggedToday(String ip, String browser) {
        return userStatisticsRepository.ipLoggedToday(ip, browser) > 0;
    }

    public void saveUserStatistics(HttpServletRequest request, String landing_page) {
        String session_id = RequestContextHolder.currentRequestAttributes().getSessionId();
        String ip = request.getRemoteAddr();
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String browser = userAgent.getBrowser().getName();
//        UserStatistics entity = mapDTOToEntity(voteDTO, voterId);
        if (!sessionExists(session_id) && !ipLoggedToday(ip, browser)) {
            userStatisticsRepository.save(new UserStatistics(session_id, landing_page, browser, ip, new Timestamp(System.currentTimeMillis())));
        }
    }

//    private UserStatistics mapDTOToEntity(UserStatisticsDTO userStatisticsDTO) {
//        UserStatistics userStatisticsEntity = new UserStatistics();
//        userStatisticsEntity.setSessionId(userStatisticsDTO.getSessionId());
//        userStatisticsEntity.setLandingPage(userStatisticsDTO.getLandingPage());
//        userStatisticsEntity.setBrowser(userStatisticsDTO.getBrowser());
//        userStatisticsEntity.setIp(userStatisticsDTO.getIp());
//        userStatisticsEntity.setTimestamp(new Timestamp(System.currentTimeMillis()));
//        return userStatisticsEntity;
//    }

    public List<String> getTopBrowsers() {
        return userStatisticsRepository.getTopBrowsers();
    }

    public List<String> getTopLandingPages() {
        return userStatisticsRepository.getTopLandingPages();
    }

    public long getUniqueVisitorsToday() {
        return userStatisticsRepository.getUniqueVisitorsToday();
    }
}
