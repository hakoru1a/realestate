package com.dpdc.realestate.models.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "notification", schema = "realestate", indexes = {
        @Index(name = "user_id", columnList = "user_id"),
        @Index(name = "notification_content_id", columnList = "notification_content_id")
})
public class Notification {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_content_id")
    private Notificationcontent notificationContent;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "modified_at")
    private Instant modifiedAt;

}