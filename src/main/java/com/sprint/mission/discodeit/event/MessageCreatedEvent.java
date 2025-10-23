package com.sprint.mission.discodeit.event;

import java.util.UUID;

public record MessageCreatedEvent(
        UUID channelId,
        UUID userId,
        String content
) {
}
