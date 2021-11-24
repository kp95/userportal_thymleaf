package com.userportal.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {

	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(FileUploadUtil.class);
	
	public static void saveImage(String uploadDir, String fileName, MultipartFile image) throws IOException {
		Path uploadPath = Paths.get(uploadDir);
		
		if(!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
			LOGGER.info("Directory Created " + uploadPath);
		}
		try(InputStream inputStream = image.getInputStream())
		{
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath,StandardCopyOption.REPLACE_EXISTING);
		}
		catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("Error in save image");
			throw new IOException();
		}
		
	}
	
	
	public static void cleanDir(String dir) {
		Path dirPath = Paths.get(dir);
		
		try {
			Files.list(dirPath).forEach(file -> {
				if(!Files.isDirectory(file)) {
					try {
						Files.delete(file);
						LOGGER.debug("Deleted File Succussfully "+ file);
					} catch (Exception e) {
						// TODO: handle exception
						LOGGER.error("Error in Deleting File "+ file);
					}
				}
			});
		}
		catch (IOException e) {
			// TODO: handle exception
			LOGGER.error("Error in CleanDir Getting Paths "+dir);
		}
	}
	
	public static void removeDir(String dir) {
		cleanDir(dir);
		try {
			Files.delete(Paths.get(dir));
			LOGGER.debug("Deleted the Directory "+dir);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("ERROR in removeDIR "+dir);
		}
	}
	
}
