package com.cricketscore.service;

import com.cricketscore.dto.LiveMatchUpdateDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class LiveScorePublisher {

    private static final Logger log = LoggerFactory.getLogger(LiveScorePublisher.class);
    private final SimpMessagingTemplate messagingTemplate;

    public LiveScorePublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void publishMatchUpdate(LiveMatchUpdateDto updateDto) {
        if (updateDto == null || updateDto.getMatchId() == null) {
            return;
        }

        // Specific match channel (e.g., /topic/match/1)
        String matchDestination = "/topic/match/" + updateDto.getMatchId();
        messagingTemplate.convertAndSend(matchDestination, updateDto);

        // General matches channel for list dashboards
        messagingTemplate.convertAndSend("/topic/matches", updateDto);

        log.debug("Published live score update to destination: {}", matchDestination);
    }
}
