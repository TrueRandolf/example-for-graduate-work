package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;

public interface CommentService {
    Comments getAllCommentsAd(Long adId);

    Comment addCommentToAd(Long adId, CreateOrUpdateComment updateComment, Authentication authentication);

    void deleteComment(Long adId, Long commentId, Authentication authentication);

    Comment updateComment(Long adId, Long commentId, CreateOrUpdateComment comment, Authentication authentication);
}
