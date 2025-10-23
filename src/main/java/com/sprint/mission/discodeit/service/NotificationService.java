package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.NotificationDto;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> getNotifications(String accessTokenValue);
}
