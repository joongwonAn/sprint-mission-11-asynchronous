package com.sprint.mission.discodeit.security;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

public class MdcTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> mdcContextMap = MDC.getCopyOfContextMap();

        return () -> {
            try {
                if (mdcContextMap != null) { // 새로운 스레드에 MDC 값 복사
                    MDC.setContextMap(mdcContextMap);
                }
                runnable.run(); // 기존 비동기 작업 진행
            } finally {
                MDC.clear();
            }
        };
    }
}
