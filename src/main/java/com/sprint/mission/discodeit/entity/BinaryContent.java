package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "binary_contents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent extends BaseEntity {

    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private Long size;
    @Column(length = 100, nullable = false)
    private String contentType;
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private BinaryContentStatus status = BinaryContentStatus.PROCESSING;

    public BinaryContent(String fileName, Long size, String contentType, BinaryContentStatus status) {
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.status = status;
    }

    public void updateStatus(BinaryContentStatus newStatus) {
        if (this.status != newStatus) {
            this.status = newStatus;
        }
    }
}
