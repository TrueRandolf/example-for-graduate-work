package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;
import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;
import ru.skypro.homework.entities.AdEntity;
import ru.skypro.homework.entities.CommentEntity;
import ru.skypro.homework.entities.UserEntity;
import ru.skypro.homework.mappers.CommentMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.security.AccessService;
import ru.skypro.homework.service.CommentService;

@Slf4j
@AllArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

//    boolean deleteComment(Long adId, Long commentId);
//
//    Comment updateComment(Long adId, Long commentId, CreateOrUpdateComment comment);


    private final CommentRepository commentRepository;
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final AccessService accessService;


    @Transactional(readOnly = true)
    public Comments getAllCommentsAd(Long adId) {
        log.info("invoked comment service get all comments");
        if (!adsRepository.existsById(adId)) {
            throw new NotFoundException("Not found ad");
        }
        return commentMapper.toComments(commentRepository.findByAd_Id(adId));
    }

    @Transactional
    public Comment addCommentToAd(Long adId, CreateOrUpdateComment updateComment, Authentication authentication) {
        log.info("invoked comment service add comment");

        AdEntity adEntity = adsRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException("Not found ad"));

        UserEntity userEntity = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));

        CommentEntity commentEntity = commentMapper.toEntity(updateComment);
        commentEntity.setUser(userEntity);
        commentEntity.setAd(adEntity);
        commentEntity.setCreatedAt(System.currentTimeMillis());
        commentRepository.save(commentEntity);
        return commentMapper.toCommentDto(commentEntity);

    }

    @Transactional
    public void deleteComment(Long adId, Long commentId, Authentication authentication) {
        log.info("invoked comment service delete comment");
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));

        if (!adsRepository.existsById(adId)) {
            throw new NotFoundException("Not found ad");
        }

        if (!accessService.isOwner(commentEntity.getUser().getUserName(), authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        if (!commentEntity.getAd().getId().equals(adId)) {
            throw new NotFoundException("Wrong relation ad->comment");
        }
        commentRepository.delete(commentEntity);
    }


    @Transactional
    public Comment updateComment(Long adId, Long commentId, CreateOrUpdateComment updateComment, Authentication authentication) {
        log.info("invoked comment service update comment");

        AdEntity adEntity = adsRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException("Not found ad"));

        CommentEntity commentEntity = commentRepository.findById(commentId)
                        .orElseThrow(() -> new NotFoundException("Comment not found"));

        if(!commentEntity.getAd().getId().equals(adId)){
            throw new NotFoundException("Wrong relation ad->comment");
        }

        if (!accessService.isOwner(commentEntity.getUser().getUserName(), authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        commentMapper.updateCommentEntity(updateComment,commentEntity);
        commentRepository.save(commentEntity);
        return commentMapper.toCommentDto(commentEntity);
    }


}
