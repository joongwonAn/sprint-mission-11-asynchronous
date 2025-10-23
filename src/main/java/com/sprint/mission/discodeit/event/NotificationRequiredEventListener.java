package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationRequiredEventListener {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final NotificationRepository notificationRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void on(MessageCreatedEvent event) {
        // 같은 채널의 참가자 중 sender 제외 각 사용자별 "알림 생성" -> DB에 notification 저장
        log.debug("# MessageCreatedEvent 수신 시작");

        UUID channelId = event.channelId();
        UUID senderId = event.userId();

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> ChannelNotFoundException.withId(channelId));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> UserNotFoundException.withId(senderId));

        List<ReadStatus> readStatuses = readStatusRepository.findByChannelIdAndNotificationEnabledTrue(channelId);
        List<User> receivers = readStatuses.stream()
                .map(ReadStatus::getUser)
                .filter(user -> !user.getId().equals(senderId))
                .toList();

        for (User receiver : receivers) {
            Notification notification = new Notification(receiver, sender, channel, event.content());
            notificationRepository.save(notification);
        }

        log.info(" # MessageCreatedEvent 수신 완료, 알림 저장 완료");
    }

    @TransactionalEventListener
    public void on(RoleUpdatedEvent event) {

    }
}
