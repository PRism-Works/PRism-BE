package com.prismworks.prism.domain.post.domain.service;

import com.prismworks.prism.domain.post.domain.model.UserPostBookmark;
import com.prismworks.prism.domain.post.infra.db.repository.UserPostBookmarkRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostBookmarkService {
    private final UserPostBookmarkRepository userPostBookmarkRepository;

    @Transactional
    public UserPostBookmark bookmark(String userId, Long postId) {
        Optional<UserPostBookmark> bookmark = userPostBookmarkRepository.findByUserIdAndPostId(userId, postId);

        if (bookmark.isPresent()) {
            UserPostBookmark existingBookmark = bookmark.get();
            existingBookmark.setActiveFlag(!existingBookmark.isActiveFlag());
            return userPostBookmarkRepository.save(existingBookmark);
        } else {
            UserPostBookmark newBookmark = new UserPostBookmark();
            newBookmark.setUserId(userId);
            newBookmark.setPostId(postId);
            userPostBookmarkRepository.save(newBookmark);
            return newBookmark;
        }
    }
}
