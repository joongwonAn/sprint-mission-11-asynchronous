package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class BinaryContentSaveListener {
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentService binaryContentService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleBinaryContentSaved(BinaryContentCreatedEvent event) {
        log.debug("# 바이너리 데이터 저장 시도");
        UUID id = binaryContentStorage.put(event.getBinaryContentId(), event.getBytes());
        if (id.equals(event.getBinaryContentId())) {
            binaryContentService.updateStatus(event.getBinaryContentId(), BinaryContentStatus.SUCCESS);
            log.info("# 바이너리 데이터 저장 성공, status=SUCCESS");
        } else {
            binaryContentService.updateStatus(event.getBinaryContentId(), BinaryContentStatus.FAIL);
            log.info("# 바이너리 데이터 저장 실패, status=FAIL");
        }
    }
}
