package com.sterling.Interfaces;

import java.util.List;

import com.sterling.Models.Photo;

public interface PhotoDAOInterface {
    void addPhoto(Photo photo);
    List<Photo> getPhotosByUserId(int userId);
    Photo getPhotoByPhotoId(int photo);
    boolean deletePhoto(int photoId);
    int countByUserId(int userId);
    boolean insertIfUnderLimit(Photo photo, int limit);
}
