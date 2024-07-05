package com.prismworks.prism.domain.email.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = EmailSendLog.TABLE_NAME)
@Entity
public class EmailSendLog {
    public static final String TABLE_NAME = "email_send_log";

    @Column(name = "log_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer logIdx;

    @Column(name = "sender_email")
    private String senderEmail;

    @Column(name = "receiver_email")
    private String receiverEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "send_path")
    private EmailTemplate path;

    @Column(name = "status")
    private EmailSendStatus status;

    @Column(name = "fail_reason")
    private String failReason;

    @Builder.Default
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
