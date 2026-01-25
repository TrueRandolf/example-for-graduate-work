package ru.skypro.homework.mappers;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Value;
import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;
import ru.skypro.homework.entities.CommentEntity;
import ru.skypro.homework.entities.UserEntity;

import java.util.List;

/**
 * Маппер для преобразования сущностей комментариев CommentEntity и DTO.
 * <p>
 * Класс нормализует пути к изображениям, добавляя префикс из конфигурации
 * {@code app.images.base-url}, чтобы фронтенд, запущенный в Docker, мог корректно
 * отображать ресурсы по абсолютным путям.
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CommentMapper {
    @Value("${app.images.base-url}")
    protected String baseUrl;

    /**
     * Маппинг в DTO комментария.
     * Использует префикс из конфига для формирования полного пути к изображению.
     * Отслеживает удаленных (soft-delete) пользователей
     * <ul>
     *     <li>запрещает показ аватара (возвращает {@code null})</li>
     *     <li>подменяет имя автора на {@code "Deleted user"}</li>
     * </ul>
     */

    @Mapping(target = "author", source = "user.id")
    @Mapping(target = "authorImage", source = "user", qualifiedByName = "mapAvatar")
    @Mapping(target = "authorFirstName", source = "user", qualifiedByName = "mapName")
    @Mapping(target = "pk", source = "id")
    public abstract Comment toCommentDto(CommentEntity comment);

    @Named("mapName")
    protected String mapName(UserEntity user) {
        if (user.getDeletedAt() != null) {
            return "Deleted user";
        }
        return user.getFirstName();
    }

    @Named("mapAvatar")
    protected String mapAvatar(UserEntity user) {
        if (user.getDeletedAt() != null || user.getUserImage() == null) {
            return null;
        }
        return baseUrl + user.getUserImage();
    }


    protected abstract List<Comment> toCommentList(List<CommentEntity> commentEntities);

    public Comments toComments(List<CommentEntity> commentEntities) {
        Comments comments = new Comments();
        comments.setCount(commentEntities.size());
        comments.setResults(toCommentList(commentEntities));
        return comments;
    }

    public abstract void updateCommentEntity(CreateOrUpdateComment dto, @MappingTarget CommentEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ad", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract CommentEntity toEntity(CreateOrUpdateComment createOrUpdateComment);

}
