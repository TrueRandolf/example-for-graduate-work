package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;
import ru.skypro.homework.dto.ads.Ad;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;
import ru.skypro.homework.entities.AdEntity;
import ru.skypro.homework.entities.UserEntity;
import ru.skypro.homework.mappers.AdMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.security.AccessService;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;

@AllArgsConstructor
@Slf4j
@Service
public class AdServiceImpl implements AdService {

    private final UserRepository userRepository;
    private final AdsRepository adsRepository;
    private final AdMapper mapper;
    private final AccessService accessService;
    private final ImageService imageService;


    // DONE!!!
    @Transactional(readOnly = true)
    public Ads getAds() {
        log.info("invoked ad service getAllAds");
        return mapper.toAds(adsRepository.findAll());
    }

    // DONE !!!!
    @Transactional
    public Ad addSimpleAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image, Authentication authentication) {
        log.info("invoked ad service add ad");

        UserEntity userEntity = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        AdEntity adEntity = mapper.toEntity(createOrUpdateAd);
        adEntity.setUser(userEntity);
        adEntity.setAdImage("/image.png");  // !!!MOCK
        adsRepository.save(adEntity);  // <- to sleep peacefully!
        return mapper.toAdDto(adEntity);

    }

    // DONE !!!
    @Transactional(readOnly = true)
    public ExtendedAd getAdInfo(Long id, Authentication authentication) {
        log.info("invoked ad service get ad info");
        //String login = authentication.getName();

        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return mapper.toExtendedAd(adEntity);
    }

    // DONE !!!
    @Transactional
    public void deleteSimpleAd(Long id, Authentication authentication) {
        log.info("invoked ad service delete ad");
        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ad not found"));

        if (!accessService.isOwner(adEntity.getUser().getUserName(), authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        String filePath = adEntity.getAdImage();
        adsRepository.deleteById(id);
        if (filePath != null) imageService.deleteImage(filePath);

    }

    // DONE !!!
    @Transactional
    public Ad updateSingleAd(Long id, CreateOrUpdateAd ad, Authentication authentication) {
        log.info("invoked ad service update ad");
        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ad not found"));

        if (!accessService.isOwner(adEntity.getUser().getUserName(), authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        mapper.updateAdEntity(ad, adEntity);
        adsRepository.save(adEntity);
        return mapper.toAdDto(adEntity);

    }

    // DONE !!!
    @Transactional(readOnly = true)
    public Ads getAllAdsAuthUser(Authentication authentication) {
        log.info("invoked ad service getAllAds user");
        String login = authentication.getName();
        return mapper.toAds(adsRepository.findByUser_UserNameAndUserDeletedAtIsNull(login));
    }


    public boolean updateAdImage(Long id) {
        log.info("invoked ad service update image");
        return true;
    }

}
