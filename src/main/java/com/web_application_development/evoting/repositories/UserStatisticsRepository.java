package com.web_application_development.evoting.repositories;

import com.web_application_development.evoting.entities.UserStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Integer> {

    @Query(value = "SELECT CASE WHEN count(*) > 0 THEN true ELSE false END FROM user_statistics " +
            "WHERE session_id = :session_id",
            nativeQuery = true)
    boolean sessionExists(@Param("session_id") String session_id);

    @Query(value = "SELECT CASE WHEN count(*) > 0 THEN true ELSE false END FROM user_statistics " +
            "WHERE ip = :ip AND browser = :browser AND DATE_TRUNC('day', timestamp) = :date",
            nativeQuery = true)
    boolean ipLoggedToday(@Param("ip") String ip, @Param("browser") String browser, @Param("date") LocalDate date);

    @Query(value = "SELECT count(*) FROM user_statistics WHERE date(timestamp) = date(now() AT TIME ZONE 'Europe/Tallinn')",
            nativeQuery = true)
    Long getUniqueVisitorsToday();

    @Query(value = "SELECT browser FROM user_statistics GROUP BY browser ORDER BY count(browser) DESC LIMIT 3",
            nativeQuery = true)
    List<String> getTopBrowsers();

    @Query(value = "SELECT landing_page FROM user_statistics GROUP BY landing_page ORDER BY count(landing_page) DESC LIMIT 3",
            nativeQuery = true)
    List<String> getTopLandingPages();
}
