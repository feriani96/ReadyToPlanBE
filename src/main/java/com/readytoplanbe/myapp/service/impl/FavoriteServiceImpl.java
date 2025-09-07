package com.readytoplanbe.myapp.service.impl;

import com.readytoplanbe.myapp.domain.Favorite;
import com.readytoplanbe.myapp.repository.FavoriteRepository;
import com.readytoplanbe.myapp.service.FavoriteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link com.readytoplanbe.myapp.domain.Favorite}.
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public List<String> getFavoritesForUser(String userId) {
        return favoriteRepository.findByUserId(userId)
            .stream()
            .map(Favorite::getTrainingCourseId)
            .collect(Collectors.toList());
    }

    @Override
    public boolean toggleFavorite(String userId, String trainingCourseId) {
        Optional<Favorite> existing = favoriteRepository.findByUserIdAndTrainingCourseId(userId, trainingCourseId);
        if (existing.isPresent()) {
            favoriteRepository.delete(existing.get());
            return false; // retiré des favoris
        } else {
            Favorite fav = new Favorite();
            fav.setUserId(userId);
            fav.setTrainingCourseId(trainingCourseId);
            favoriteRepository.save(fav);
            return true; // ajouté aux favoris
        }
    }
}
