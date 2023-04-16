package ru.practicum.stats.endpointhit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.stats.endpointhit.model.EndpointHit;
import ru.practicum.stats.endpointhit.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.stats.endpointhit.model.ViewStats(eh.app, eh.uri, COUNT(eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.timestamp BETWEEN :startDate AND :endDate " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT (eh.ip) DESC ")
    List<ViewStats> findByDate(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new ru.practicum.stats.endpointhit.model.ViewStats(eh.app, eh.uri, COUNT (eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.timestamp BETWEEN :startDate AND :endDate " +
            "AND eh.uri IN :uris " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT (eh.ip) DESC ")
    List<ViewStats> findByDateAndUris(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate,
                                      @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.stats.endpointhit.model.ViewStats(eh.app, eh.uri, COUNT (DISTINCT eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.timestamp BETWEEN :startDate AND :endDate " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT (eh.ip) DESC ")
    List<ViewStats> findByDateWithUniqueIp(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new ru.practicum.stats.endpointhit.model.ViewStats(eh.app, eh.uri, COUNT (DISTINCT eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.timestamp BETWEEN :startDate AND :endDate " +
            "AND eh.uri IN :uris " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT (eh.ip) DESC ")
    List<ViewStats> findByDateAndUrisWithUniqueIp(@Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate,
                                                  @Param("uris") List<String> uris);
}
