package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getNotifications(@RequestHeader("Authorization") String authorization) {
        log.debug("# 알림 목록 조회 시작");
        if (!authorization.startsWith("Bearer ")) {
            log.error("# Authorization header not found");
            throw new DiscodeitException(ErrorCode.INVALID_TOKEN);
        }

        String accessTokenValue = authorization.replace("Bearer ", "");
        List<NotificationDto> dtos = notificationService.getNotifications(accessTokenValue);
        log.info("# 알림 목록 조회 성공");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dtos);

    }
}
