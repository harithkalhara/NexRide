package com.nexride.Service;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MediaService {

    // Instance Variable
    private final Path dir;

    // Constructor Injection & Directory Initialization
    public MediaService(@Value("${nexride.data-dir:./data}") String data) throws IOException {
        dir = Path.of(data).toAbsolutePath().resolve("uploads");
        Files.createDirectories(dir);
    }

    // Validate, process, and save uploaded image files
    public String save(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty() || file.getSize() > 5 * 1024 * 1024)
            throw ApiException.bad("Choose a JPG or PNG image up to 5 MB.");
        try (ImageInputStream in = ImageIO.createImageInputStream(file.getInputStream())) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (!readers.hasNext()) throw ApiException.bad("The selected file is not an image.");
            ImageReader r = readers.next();
            try {
                r.setInput(in);
                if (!Set.of("jpeg", "jpg", "png").contains(r.getFormatName().toLowerCase(Locale.ROOT)))
                    throw ApiException.bad("Use a JPG or PNG photo.");
                int w = r.getWidth(0), h = r.getHeight(0);
                if (w > 6000 || h > 6000 || (long) w * h > 16_000_000)
                    throw ApiException.bad("Image exceeds the 16 megapixel limit.");
                BufferedImage image = r.read(0);
                String name = UUID.randomUUID() + ".png";
                if (!ImageIO.write(image, "png", dir.resolve(name).toFile()))
                    throw ApiException.bad("Image could not be processed.");
                return "/uploads/" + name;
            } finally {
                r.dispose();
            }
        }
    }

    // Return the base uploads directory path
    public Path directory() {
        return dir;
    }
}
