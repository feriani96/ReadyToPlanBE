package com.readytoplanbe.myapp.service;

import java.util.List;

public interface FavoriteService {

    List<String> getFavoritesForUser(String userLogin);
    boolean toggleFavorite(String userLogin, String trainingCourseId);
}
