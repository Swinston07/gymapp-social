package com.sterling.Controllers;

import java.util.Map;

import com.cloudinary.utils.ObjectUtils;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;

public class PhotoController {
    
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

            byte[] bytes = uploadedFile.content().readAllBytes();

            Map uploadedResult = com.sterling.Utils.CloudinaryConfig.getCloudinary().uploader().upload(
                bytes,
                ObjectUtils.emptyMap()
            );

            String imageUrl = (String) uploadedResult.get("secure_url");
            ctx.status(200).json(Map.of("imageUrl", imageUrl));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Upload failed: " + e.getMessage()));
        }
    }
}
