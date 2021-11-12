package com.ds.antddun.service;

import com.ds.antddun.entity.UploadFile;
import com.ds.antddun.repository.UploadFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

	@Autowired
	UploadFileRepository uploadFileRepository;
	
	private final Path rootLocation; // c:/upload/

	public ImageService(String uploadPath) {
		this.rootLocation = Paths.get(uploadPath);
		System.out.println(rootLocation.toString());
	}
	
	public UploadFile store(MultipartFile file) throws Exception {
		//		 fileName : 예제1.jpg
		//		 filePath : c:/upload/uuid-예제1.jpg
		//		 saveFileName : uuid-예제1.png
		//		 contentType : image/jpeg
		//		 size : 4994942
		try {
			if(file.isEmpty()) {
				throw new Exception("Failed to store empty file " + file.getOriginalFilename());
			}
			
			String saveFileName = fileSave(rootLocation.toString(), file);
			UploadFile saveFile = new UploadFile();
			saveFile.setFileName(file.getOriginalFilename());
			saveFile.setSaveFileName(saveFileName);
			saveFile.setContentType(file.getContentType());
			saveFile.setSize(file.getResource().contentLength());
			saveFile.setFilePath(rootLocation.toString().replace(File.separatorChar, '/') +'/' + saveFileName);
			uploadFileRepository.save(saveFile);
			return saveFile;
			
		} catch(IOException e) {
			throw new Exception("Failed to store file " + file.getOriginalFilename(), e);
		}
		
		
	}

	public UploadFile load(Long fileId) {
		return uploadFileRepository.findById(fileId).get();
	}
	
	public String fileSave(String rootLocation, MultipartFile file) throws IOException {
		File uploadDir = new File(rootLocation);
		
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		
		// saveFileName 생성
		UUID uuid = UUID.randomUUID();
		String saveFileName = uuid.toString() + file.getOriginalFilename();
		File saveFile = new File(rootLocation, saveFileName);
		FileCopyUtils.copy(file.getBytes(), saveFile);
		
		return saveFileName;
	}
	
	
	
}
