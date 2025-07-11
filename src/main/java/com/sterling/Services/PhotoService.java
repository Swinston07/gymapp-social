package com.sterling.Services;

import java.util.List;

import com.sterling.Interfaces.PhotoDAOInterface;
import com.sterling.Models.Photo;

public class PhotoService {
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
}
