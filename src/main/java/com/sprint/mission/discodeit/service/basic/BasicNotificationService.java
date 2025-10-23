package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtTokenProvider;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> getNotifications(String accessTokenValue) {
        // accessToken -> 유저 정보 추출 -> 해당 유저(receiver)의 알림 목록 조회 -> DTO 변환 후 반환
        log.debug("# 알림 조회 비즈니스 로직 시작, accessTokenValue: {}", accessTokenValue);

        if (!jwtTokenProvider.validateAccessToken(accessTokenValue)) {
            throw new DiscodeitException(ErrorCode.INVALID_TOKEN);
        }

        UUID userId = jwtTokenProvider.getUserId(accessTokenValue);
        User receiver = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.withId(userId));

        List<Notification> notifications = notificationRepository.findByReceiverOrderByCreatedAtDesc(receiver);


        return notifications.stream()
                .map(notificationMapper::toDto)
                .toList();
    }
}
