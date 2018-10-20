package com.web_application_development.evoting.repositories;

import com.web_application_development.evoting.entities.UserStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Integer> {

    @Query(value = "SELECT count(*) FROM user_statistics WHERE session_id = :session_id",
            nativeQuery = true)
    long sessionExists(@Param("session_id") String session_id);

    @Query(value = "SELECT count(*) FROM user_statistics " +
            "WHERE ip = :ip AND browser = :browser",
            nativeQuery = true)
    long ipLoggedToday(@Param("ip") String ip, @Param("browser") String browser);

    @Query(value = "SELECT count(*) FROM user_statistics WHERE date(timestamp) = date(now() AT TIME ZONE 'Europe/Tallinn')",
            nativeQuery = true)
    long getUniqueVisitorsToday();

    @Query(value = "SELECT browser FROM user_statistics GROUP BY browser ORDER BY count(browser) DESC",
            nativeQuery = true)
    List<String> getBrowsers();

    @Query(value = "SELECT landing_page FROM user_statistics GROUP BY landing_page ORDER BY count(landing_page) DESC",
            nativeQuery = true)
    List<String> getLandingPages();
}
