// package com.packt.football.service;

// import com.packt.football.domain.MatchEvent;
// import com.packt.football.domain.Player;
// import com.packt.football.mapper.PlayerMapper;
// import com.packt.football.repo.MatchEventEntity;
// import com.packt.football.repo.PlayerEntity;
// import jakarta.persistence.EntityManager;
// import jakarta.persistence.EntityManagerFactory;
// import jakarta.persistence.Query;
// import jakarta.persistence.TypedQuery;
// import jakarta.persistence.criteria.CriteriaBuilder;
// import jakarta.persistence.criteria.CriteriaQuery;
// import jakarta.persistence.criteria.Predicate;
// import jakarta.persistence.criteria.Root;
// import org.springframework.stereotype.Service;

// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;

// @Service
// public class DynamicQueriesService {

//     private final EntityManager em;
//     private final PlayerMapper playerMapper;

//     public DynamicQueriesService(EntityManagerFactory emFactory, PlayerMapper playerMapper) {
//         this.em = emFactory.createEntityManager();
//         this.playerMapper = playerMapper;
//     }

//     public List<PlayerEntity> searchTeamPlayers(Integer teamId, Optional<String> name, Optional<Integer> minHeight,
//             Optional<Integer> maxHeight,
//             Optional<Integer> minWeight, Optional<Integer> maxWeight) {
//         CriteriaBuilder cb = em.getCriteriaBuilder();
//         CriteriaQuery<PlayerEntity> cq = cb.createQuery(PlayerEntity.class);
//         Root<PlayerEntity> player = cq.from(PlayerEntity.class);
//         List<Predicate> predicates = new ArrayList<>();
//         predicates.add(cb.equal(player.get("team").get("id"), teamId));
//         if (name.isPresent()) {
//             predicates.add(cb.like(player.get("name"), "%" + name.get() + "%"));
//         }
//         if (minHeight.isPresent()) {
//             predicates.add(cb.ge(player.get("height"), minHeight.get()));
//         }
//         if (maxHeight.isPresent()) {
//             predicates.add(cb.le(player.get("height"), maxHeight.get()));
//         }
//         if (minWeight.isPresent()) {
//             predicates.add(cb.ge(player.get("weight"), minWeight.get()));
//         }
//         if (maxWeight.isPresent()) {
//             predicates.add(cb.le(player.get("weight"), maxWeight.get()));
//         }
//         cq.where(predicates.toArray(new Predicate[0]));
//         TypedQuery<PlayerEntity> query = em.createQuery(cq);
//         return query.getResultList();
//     }

//     public List<Player> searchTeamPlayersAndMap(Integer teamId, Optional<String> name, Optional<Integer> minHeight,
//             Optional<Integer> maxHeight,
//             Optional<Integer> minWeight, Optional<Integer> maxWeight) {
//         return searchTeamPlayers(teamId, name, minHeight, maxHeight, minWeight, maxWeight)
//                 .stream()
//                 .map(p -> playerMapper.map(p))
//                 .toList();
//     }

//     public List<MatchEventEntity> searchMatchEventsRange(Integer matchId, Optional<LocalDateTime> minTime,
//             Optional<LocalDateTime> maxTime) {
//         String command = "SELECT e FROM MatchEventEntity e WHERE e.match.id=:matchId ";
//         if (minTime.isPresent() && maxTime.isPresent()) {
//             command += " AND e.time BETWEEN :minTime AND :maxTime";
//         } else if (minTime.isPresent()) {
//             command += " AND e.time >= :minTime";
//         } else if (maxTime.isPresent()) {
//             command += " AND e.time <= :maxTime";
//         }
//         TypedQuery<MatchEventEntity> query = em.createQuery(command, MatchEventEntity.class);
//         query.setParameter("matchId", matchId);
//         if (minTime.isPresent()) {
//             query.setParameter( "minTime", minTime.get());
//         }
//         if (maxTime.isPresent()) {
//             query.setParameter("maxTime", maxTime.get());
//         }
//         return query.getResultList();
//     }

//     public List<MatchEvent> searchMatchEventsRangeAndMap(Integer matchId, Optional<LocalDateTime> minMinute,
//             Optional<LocalDateTime> maxMinute) {
//         return searchMatchEventsRange(matchId, minMinute, maxMinute)
//                 .stream()
//                 .map(e -> new MatchEvent(e.getTime(), e.getDetails()))
//                 .toList();
//     }

//     public void deleteEventRange(Integer matchId, LocalDateTime start, LocalDateTime end) {
//         try {
//             em.clear();
//             em.getTransaction().begin();
//             Query query = em.createQuery(
//                     "DELETE FROM MatchEventEntity e WHERE e.match.id=:matchId AND e.time BETWEEN :start AND :end");
//             query.setParameter("matchId", matchId);
//             query.setParameter("start", start);
//             query.setParameter("end", end);
//             query.executeUpdate();
//             em.getTransaction().commit();
//         } catch (Exception e) {
//             em.getTransaction().rollback();
//             throw e;
//         }
//     }

//     public List<PlayerEntity> searchUserMissingPlayers(Integer userId) {
//         Query query = em.createNativeQuery(
//                 "SELECT p1.* FROM players p1 WHERE p1.id NOT IN (SELECT c1.player_id FROM cards c1 WHERE c1.owner_id=?1)",
//                 PlayerEntity.class);
//         query.setParameter(1, userId);
//         return query.getResultList();
//     }


//     public List<Player> searchUserMissingPlayersAndMap(Integer userId) {
//         return searchUserMissingPlayers(userId)
//                 .stream()
//                 .map(playerMapper::map)
//                 .toList();
//     }
// }

package com.packt.football.service;

import com.packt.football.domain.MatchEvent;
import com.packt.football.domain.Player;
import com.packt.football.mapper.PlayerMapper;
import com.packt.football.repo.MatchEventEntity;
import com.packt.football.repo.PlayerEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true) // Standardmäßig schreibgeschützt für Performance
public class DynamicQueriesService {

    @PersistenceContext
    private final EntityManager em;
    private final PlayerMapper playerMapper;

    // Spring Boot 4 injiziert den proxy-basierten, thread-sicheren EntityManager direkt
    public DynamicQueriesService(EntityManager em, PlayerMapper playerMapper) {
        this.em = em;
        this.playerMapper = playerMapper;
    }

    public List<PlayerEntity> searchTeamPlayers(Integer teamId, Optional<String> name, Optional<Integer> minHeight,
            Optional<Integer> maxHeight, Optional<Integer> minWeight, Optional<Integer> maxWeight) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PlayerEntity> cq = cb.createQuery(PlayerEntity.class);
        Root<PlayerEntity> player = cq.from(PlayerEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(player.get("team").get("id"), teamId));

        name.ifPresent(n -> predicates.add(cb.like(player.get("name"), "%" + n + "%")));
        minHeight.ifPresent(h -> predicates.add(cb.ge(player.get("height"), h)));
        maxHeight.ifPresent(h -> predicates.add(cb.le(player.get("height"), h)));
        minWeight.ifPresent(w -> predicates.add(cb.ge(player.get("weight"), w)));
        maxWeight.ifPresent(w -> predicates.add(cb.le(player.get("weight"), w)));

        cq.where(predicates.toArray(new Predicate[0]));
        TypedQuery<PlayerEntity> query = em.createQuery(cq);
        return query.getResultList();
    }

    public List<Player> searchTeamPlayersAndMap(Integer teamId, Optional<String> name, Optional<Integer> minHeight,
            Optional<Integer> maxHeight, Optional<Integer> minWeight, Optional<Integer> maxWeight) {
        return searchTeamPlayers(teamId, name, minHeight, maxHeight, minWeight, maxWeight)
                .stream()
                .map(playerMapper::map)
                .toList();
    }

    public List<MatchEventEntity> searchMatchEventsRange(Integer matchId, Optional<LocalDateTime> minTime,
            Optional<LocalDateTime> maxTime) {
        StringBuilder command = new StringBuilder("SELECT e FROM MatchEventEntity e WHERE e.match.id=:matchId ");

        if (minTime.isPresent() && maxTime.isPresent()) {
            command.append(" AND e.time BETWEEN :minTime AND :maxTime");
        } else if (minTime.isPresent()) {
            command.append(" AND e.time >= :minTime");
        } else if (maxTime.isPresent()) {
            command.append(" AND e.time <= :maxTime");
        }

        TypedQuery<MatchEventEntity> query = em.createQuery(command.toString(), MatchEventEntity.class);
        query.setParameter("matchId", matchId);
        minTime.ifPresent(t -> query.setParameter("minTime", t));
        maxTime.ifPresent(t -> query.setParameter("maxTime", t));

        return query.getResultList();
    }

    public List<MatchEvent> searchMatchEventsRangeAndMap(Integer matchId, Optional<LocalDateTime> minMinute,
            Optional<LocalDateTime> maxMinute) {
        return searchMatchEventsRange(matchId, minMinute, maxMinute)
                .stream()
                .map(e -> new MatchEvent(e.getTime(), e.getDetails()))
                .toList();
    }

    @Transactional // Überschreibt readOnly für schreibende/löschende Operationen
    public void deleteEventRange(Integer matchId, LocalDateTime start, LocalDateTime end) {
        em.clear();
        // Das Transaktionshandling übernimmt Spring Boot 4 nun atomar im Hintergrund
        em.createQuery("DELETE FROM MatchEventEntity e WHERE e.match.id=:matchId AND e.time BETWEEN :start AND :end")
          .setParameter("matchId", matchId)
          .setParameter("start", start)
          .setParameter("end", end)
          .executeUpdate();
    }

    public List<PlayerEntity> searchUserMissingPlayers(Integer userId) {
        // Bereinigte Variante über die Hibernate-Session (Lösung aus vorigem Schritt)
        Session session = em.unwrap(Session.class);

        return session.createNativeQuery(
                "SELECT p1.* FROM players p1 WHERE p1.id NOT IN (SELECT c1.player_id FROM cards c1 WHERE c1.owner_id=?1)",
                PlayerEntity.class)
                .setParameter(1, userId)
                .getResultStream() // Liefert typsicheren Stream<PlayerEntity> in SB 4
                .toList();
    }

    public List<Player> searchUserMissingPlayersAndMap(Integer userId) {
        return searchUserMissingPlayers(userId)
                .stream()
                .map(playerMapper::map)
                .toList();
    }
}
