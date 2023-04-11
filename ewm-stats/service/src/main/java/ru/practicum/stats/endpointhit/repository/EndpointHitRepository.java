package ru.practicum.stats.endpointhit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.endpointhit.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.stats.dto.ViewStatsDto(eh.app, eh.uri, COUNT(eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.timestamp BETWEEN :startDate AND :endDate " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT (eh.ip) DESC ")
    List<ViewStatsDto> findByDate(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT new ru.practicum.stats.dto.ViewStatsDto(eh.app, eh.uri, COUNT (eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.timestamp BETWEEN :startDate AND :endDate " +
            "AND eh.uri IN :uris " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT (eh.ip) DESC ")
    List<ViewStatsDto> findByDateAndUris(LocalDateTime startDate, LocalDateTime endDate, List<String> uris);

    @Query("SELECT new ru.practicum.stats.dto.ViewStatsDto(eh.app, eh.uri, COUNT (DISTINCT eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.timestamp BETWEEN :startDate AND :endDate " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT (eh.ip) DESC ")
    List<ViewStatsDto> findByDateWithUniqueIp(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT new ru.practicum.stats.dto.ViewStatsDto(eh.app, eh.uri, COUNT (DISTINCT eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.timestamp BETWEEN :startDate AND :endDate " +
            "AND eh.uri IN :uris " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT (eh.ip) DESC ")
    List<ViewStatsDto> findByDateAndUrisWithUniqueIp(LocalDateTime startDate, LocalDateTime endDate, List<String> uris);
}
