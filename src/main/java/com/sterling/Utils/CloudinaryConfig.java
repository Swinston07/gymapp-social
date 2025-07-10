package com.sterling.Utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;

public class CloudinaryConfig {
    private static Cloudinary cloudinary;

    static {
        Dotenv dotenv = Dotenv.load();
        cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", dotenv.get("CLOUDINARY_CLOUD_NAME"),
            "api_key", dotenv.get("CLOUDINARY_API_KEY"),
            "api_secret", dotenv.get("CLOUDINARY_API_SECRET")
        ));

        System.out.println("=============================");
        System.out.println("Cloud name: " + dotenv.get("CLOUDINARY_CLOUD_NAME"));
        System.out.println("API key: " + dotenv.get("CLOUDINARY_API_KEY"));
        System.out.println("=============================");
    }

    public static Cloudinary getCloudinary() {
        return cloudinary;
    }
}
