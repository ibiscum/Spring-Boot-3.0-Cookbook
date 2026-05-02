package com.packt.footballmdb.service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.mongodb.MongoException;
import com.mongodb.client.result.UpdateResult;
import com.packt.footballmdb.repository.Card;
import com.packt.footballmdb.repository.CardRepository;
import com.packt.footballmdb.repository.Player;
import com.packt.footballmdb.repository.PlayerRepository;
import com.packt.footballmdb.repository.User;
import com.packt.footballmdb.repository.UserRepository;

@Service
public class UserService {

    private static final int MAX_TRANSACTION_RETRIES = 3;

    private final UserRepository userRepository;
    private final PlayerRepository playersRepository;
    private final CardRepository cardsRepository;
    private final MongoTemplate mongoTemplate;
    private final TransactionTemplate transactionTemplate;

    public UserService(UserRepository userRepository, PlayerRepository playersRepository,
            CardRepository cardsRepository, MongoTemplate mongoTemplate,
            TransactionTemplate transactionTemplate) {
        this.userRepository = userRepository;
        this.playersRepository = playersRepository;
        this.cardsRepository = cardsRepository;
        this.mongoTemplate = mongoTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    public Integer buyTokens(String userId, Integer tokens) {
        Query query = new Query(Criteria.where("id").is(userId));
        Update update = new Update().inc("tokens", tokens);
        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class,
                "users");
        return (int) result.getModifiedCount();
    }

    public Integer buyCards(String userId, Integer count) {
        for (int attempt = 1; attempt <= MAX_TRANSACTION_RETRIES; attempt++) {
            try {
                return transactionTemplate.execute(status -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    if (user.getTokens() < count) {
                        throw new RuntimeException("Not enough tokens");
                    }

                    List<Player> availablePlayers = getAvailablePlayers();
                    Random random = new Random();
                    user.setTokens(user.getTokens() - count);

                    List<Card> cards = Stream.generate(() -> {
                        Card card = new Card();
                        card.setOwner(user);
                        card.setPlayer(availablePlayers.get(random.nextInt(0, availablePlayers.size())));
                        return card;
                    }).limit(count).toList();

                    List<Card> savedCards = cardsRepository.saveAll(cards);
                    userRepository.save(user);
                    return savedCards.size();
                });
            } catch (RuntimeException ex) {
                if (!isRetryableTransactionException(ex) || attempt == MAX_TRANSACTION_RETRIES) {
                    throw ex;
                }
            }
        }
        throw new RuntimeException("Failed to complete buyCards transaction after retries");
    }

    private boolean isRetryableTransactionException(Throwable throwable) {
        Throwable cause = throwable;
        while (cause != null) {
            if (cause instanceof MongoException mongoException) {
                if (mongoException.hasErrorLabel(MongoException.TRANSIENT_TRANSACTION_ERROR_LABEL)
                        || mongoException.hasErrorLabel(MongoException.UNKNOWN_TRANSACTION_COMMIT_RESULT_LABEL)) {
                    return true;
                }
            }
            cause = cause.getCause();
        }
        return false;
    }

    @Cacheable(value = "availablePlayers")
    private List<Player> getAvailablePlayers() {
        return playersRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUser(String id){
        return userRepository.findById(id);
    }

    public List<Card> getUserCards(String id){
        return cardsRepository.findByOwnerId(id);
    }

}
