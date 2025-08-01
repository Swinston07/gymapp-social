package com.sterling.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sterling.Interfaces.PhotoDAOInterface;
import com.sterling.Models.Photo;

public class PhotoServiceTest {

    private PhotoDAOInterface photoDAO;
    private PhotoService photoService;

    @BeforeEach
    public void setup() {
        photoDAO = mock(PhotoDAOInterface.class);
        photoService = new PhotoService(photoDAO);
    }

    @Test
    void testAddPhoto() {
        Photo photo = new Photo();
        photo.setUserId(1);
        photo.setImageUrl("http://example.com/photo.jpg");

        photoService.addPhoto(photo);

        verify(photoDAO, times(1)).addPhoto(photo);
    }

    @Test
    void testDeletePhotoByPhotoId() {
        when(photoDAO.deletePhoto(1)).thenReturn(true);
        
        boolean result = photoService.deletePhotoByPhotoId(1);

        assertTrue(result);
        verify(photoDAO, times(1)).deletePhoto(1);
    }

    @Test
    void testGetPhotoByPhotoId() {
        Photo photo = new Photo();
        photo.setImageUrl("http://example.com/photo.jpg");
        photo.setPhotoId(1);

        when(photoDAO.getPhotoByPhotoId(1)).thenReturn(photo);

        Photo result = photoService.getPhotoByPhotoId(1);

        assertNotNull(result);
        assertEquals("http://example.com/photo.jpg", result.getImageUrl());

        verify(photoDAO, times(1)).getPhotoByPhotoId(1);
    }

    @Test
    void testGetPhotosByUserId() {
        Photo photo1 = new Photo();
        photo1.setImageUrl("http://example.com/photo.jpg");
        photo1.setUserId(1);
        photo1.setPhotoId(1);

        Photo photo2 = new Photo();
        photo2.setImageUrl("http://test.com/photo.jpg");
        photo2.setUserId(1);
        photo2.setPhotoId(2);

        List<Photo> photoMockList = List.of(photo1, photo2);

        when(photoDAO.getPhotosByUserId(1)).thenReturn(photoMockList);

        List<Photo> result = photoService.getPhotosByUserId(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("http://example.com/photo.jpg", result.get(0).getImageUrl());
        assertEquals(1, result.get(0).getPhotoId());
        assertEquals(1, result.get(0).getUserId());
        assertEquals("http://test.com/photo.jpg", result.get(1).getImageUrl());
        assertEquals(2, result.get(1).getPhotoId());
        assertEquals(1, result.get(1).getUserId());

        verify(photoDAO, times(1)).getPhotosByUserId(1);
    }
}
