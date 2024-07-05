package com.prismworks.prism.domain.email.repository;

import com.prismworks.prism.domain.email.model.EmailSendLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailSendLogRepository extends JpaRepository<EmailSendLog, Integer> {
}
