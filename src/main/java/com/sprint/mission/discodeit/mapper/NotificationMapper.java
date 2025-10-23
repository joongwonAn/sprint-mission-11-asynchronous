package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {UserMapper.class, ChannelMapper.class})
public interface NotificationMapper {

    @Mapping(target = "receiverId", source = "receiver.id")
    @Mapping(target = "title", expression = "java(notification.getSender().getUsername() + \" (#\" + notification.getChannel().getName() + \")\")")
    NotificationDto toDto(Notification notification);
}