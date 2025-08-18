package com.sterling.Services;

import java.util.List;

import com.sterling.Interfaces.PhotoDAOInterface;
import com.sterling.Models.Photo;

public class PhotoService {
    public static final int MAX_PHOTOS = 6; // Adjust as needed
    private PhotoDAOInterface photoDao;

    public PhotoService(PhotoDAOInterface photoDao) {
        this.photoDao = photoDao;
    }

    public void addPhoto(Photo photo) {
        photoDao.addPhoto(photo);
    }

    public List<Photo> getPhotosByUserId(int userId) {
        return photoDao.getPhotosByUserId(userId);
    }

    public Photo getPhotoByPhotoId(int photoId) {
        return photoDao.getPhotoByPhotoId(photoId);
    }

    public boolean deletePhotoByPhotoId(int photoId) {
        return photoDao.deletePhoto(photoId);
    }

    public int countByUserId(int userId) {
        return photoDao.countByUserId(userId);
    }

    public boolean addPhotoEnforcingLimit(Photo photo) {
        return photoDao.insertIfUnderLimit(photo, MAX_PHOTOS);
    }
}
