package com.sterling.Controllers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.cloudinary.utils.ObjectUtils;
import com.sterling.Models.Photo;
import com.sterling.Services.PhotoService;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;

public class PhotoController {
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }


    public void uploadPhoto(Context ctx) {
        try {
            UploadedFile uploadedFile = ctx.uploadedFile("image");
            System.out.println("=============================="); 
            System.out.println("Received file: " + uploadedFile);
            System.out.println("==============================");

            if(uploadedFile == null) {
                ctx.status(400).result("No file uploaded");
                return;
            }

            int requesterId = ctx.attribute("userId");
            int userId = Integer.parseInt(ctx.pathParam("id"));

            if(userId != requesterId) {
                ctx.status(403).result("Unauthorized to upload a photo for another user");
                return;
            }

            //Upload to cloudianry
            byte[] bytes = uploadedFile.content().readAllBytes();

            Map uploadedResult = com.sterling.Utils.CloudinaryConfig.getCloudinary().uploader().upload(
                bytes,
                ObjectUtils.emptyMap()
            );

            String imageUrl = (String) uploadedResult.get("secure_url");

            //Save photo to DB
            Photo photo = new Photo();
            photo.setUserId(userId);
            photo.setImageUrl(imageUrl);
            photo.setUploadedAt(Timestamp.from(Instant.now()));
            photoService.addPhoto(photo);

            ctx.status(201).json(Map.of("imageUrl", imageUrl));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Upload failed: " + e.getMessage()));
        }
    }

    public void getPhotosByUserId(Context ctx) {
        try {
            int userId = Integer.parseInt(ctx.pathParam("id"));
            List<Photo> photos = photoService.getPhotosByUserId(userId);
            ctx.status(200).json(photos);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Failed to fetch photos: " + e.getMessage()));
        }
    }

    public void getPhotoByPhotoId(Context ctx) {
        try {
            int photoId = Integer.parseInt(ctx.pathParam("photoId"));
            Photo photo = photoService.getPhotoByPhotoId(photoId);
            if(photo == null) {
                ctx.status(404).result("Photo not found");
            } else {
                ctx.status(200).json(photo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Failed to fetch photo: " + e.getMessage()));
        }
    }

    public void deletePhotoByPhotoId(Context ctx) {
        try {
            int requesterId = ctx.attribute("userId");
            int photoId = Integer.parseInt(ctx.pathParam("photoId"));
            
            Photo photo = photoService.getPhotoByPhotoId(photoId);

            if(photo == null) {
                ctx.status(404).result("Photo not found");
                return;
            }

            if(photo.getUserId() != requesterId){
                ctx.status(403).result("Unauthorized to delete a photo for another user");
                return;
            }

            boolean deleted = photoService.deletePhotoByPhotoId(photoId);

            if(deleted) {
                ctx.status(200).result("Photo deleted.");
            } else {
                ctx.status(500).result("Failed to delete photo");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "error deleting photo: " + e.getMessage()));
        }
    }
}
