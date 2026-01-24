package ru.skypro.homework.mappers;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Value;
import ru.skypro.homework.dto.ads.Ad;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;
import ru.skypro.homework.entities.AdEntity;

import java.util.List;

/**
 * Маппер для преобразования сущностей объявлений AdEntity и DTO.
 * <p>
 * Класс нормализует пути к изображениям, добавляя префикс из конфигурации
 * {@code app.images.base-url}, чтобы фронтенд, запущенный в Docker, мог корректно
 * отображать ресурсы по абсолютным путям.
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class AdMapper {
    @Value("${app.images.base-url}")
    protected String baseUrl;


    public abstract AdEntity toEntity(CreateOrUpdateAd createOrUpdateAd);

    /**
     * Маппинг в краткое DTO объявления.
     * Использует префикс из конфига для формирования полного пути к изображению.
     */

    @Mapping(target = "author", source = "user.id")
    @Mapping(target = "image", source = "adImage", qualifiedByName = "adImageToPath")
    @Mapping(target = "pk", source = "id")
    public abstract Ad toAdDto(AdEntity adEntity);

    @Named("adImageToPath")
    protected String mapImage(String adImage) {
        if (adImage == null) return null;
        return baseUrl + adImage;
    }

    protected abstract List<Ad> toAdList(List<AdEntity> adEntityList);

    public Ads toAds(List<AdEntity> adEntityList) {
        Ads ads = new Ads();
        ads.setCount(adEntityList.size());
        ads.setResults(toAdList(adEntityList));
        return ads;
    }

    /**
     * Маппинг в развернутое DTO объявления.
     * Использует префикс из конфига для формирования полного пути к изображению.
     */

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName", source = "user.firstName")
    @Mapping(target = "authorLastName", source = "user.lastName")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "email", source = "user.userName")
    @Mapping(target = "image", source = "adImage", qualifiedByName = "adImageToPath")
    @Mapping(target = "phone", source = "user.phone")
    public abstract ExtendedAd toExtendedAd(AdEntity adEntity);

    public abstract void updateAdEntity(CreateOrUpdateAd dto, @MappingTarget AdEntity entity);

}
