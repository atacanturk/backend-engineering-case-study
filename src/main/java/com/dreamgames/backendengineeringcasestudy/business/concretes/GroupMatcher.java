package com.dreamgames.backendengineeringcasestudy.business.concretes;

import com.dreamgames.backendengineeringcasestudy.business.constants.GroupMatcherDefaultValues;
import com.dreamgames.backendengineeringcasestudy.entities.Country;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
@Service
@Component
public class GroupMatcher {

    private final BlockingQueue<Long> trQueue;
    private final BlockingQueue<Long> usaQueue;
    private final BlockingQueue<Long> ukQueue;
    private final BlockingQueue<Long> frQueue;
    private final BlockingQueue<Long> deQueue;


    public GroupMatcher() {
        this.trQueue = new ArrayBlockingQueue<>(GroupMatcherDefaultValues.QUEUE_SIZE);
        this.usaQueue = new ArrayBlockingQueue<>(GroupMatcherDefaultValues.QUEUE_SIZE);
        this.ukQueue = new ArrayBlockingQueue<>(GroupMatcherDefaultValues.QUEUE_SIZE);
        this.frQueue = new ArrayBlockingQueue<>(GroupMatcherDefaultValues.QUEUE_SIZE);
        this.deQueue = new ArrayBlockingQueue<>(GroupMatcherDefaultValues.QUEUE_SIZE);
    }

    public Optional<List<Long>> addUserToQueueAndTryMatch(Long userId, Country country){
        switch (country) {
            case TURKEY:
                trQueue.add(userId);
                break;
            case UNITED_STATES:
                usaQueue.add(userId);
                break;
            case UNITED_KINGDOM:
                ukQueue.add(userId);
                break; // Added for UK
            case FRANCE:
                frQueue.add(userId);
                break;
            default:
                deQueue.add(userId);
        }
        return tryMatch();
    }

    public Optional<List<Long>> tryMatch(){

        List<Long> userIdsOfNewGroup = new ArrayList<>();

        if (!trQueue.isEmpty() &&!usaQueue.isEmpty() &&!ukQueue.isEmpty() && !frQueue.isEmpty() && !deQueue.isEmpty()){
            userIdsOfNewGroup.add(trQueue.poll());
            userIdsOfNewGroup.add(usaQueue.poll());
            userIdsOfNewGroup.add(ukQueue.poll());
            userIdsOfNewGroup.add(frQueue.poll());
            userIdsOfNewGroup.add(deQueue.poll());
        }

        return userIdsOfNewGroup.isEmpty() ? Optional.empty() : Optional.of(userIdsOfNewGroup);
    }
}
