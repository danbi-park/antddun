package com.ds.antddun.service;

import com.ds.antddun.entity.UploadImage;
import com.ds.antddun.repository.UploadImageRepository;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class ImageService {

	@Autowired
	UploadImageRepository uploadImageRepository;

	private final Path rootLocation; // C:/upload

	public ImageService(String uploadPath) {
		this.rootLocation = Paths.get(uploadPath);
		System.out.println(rootLocation.toString());
	}

	//ajax 이미지 로드
	public UploadImage load(Long fileId) {
		return uploadImageRepository.findById(fileId).get();
	}

	//folderPath 생성
	private String makeFolder(){
		String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		String folderPath = str.replace("/", File.separator);
		File uploadPathFolder = new File(rootLocation.toString(), folderPath);
		if (!uploadPathFolder.exists()) {
			uploadPathFolder.mkdirs();
		}
		return folderPath;
	}

	public String fileSave(String rootLocation, MultipartFile file) throws IOException {
		//saveFileName 생성
		UUID uuid = UUID.randomUUID();
		String saveFileName = uuid.toString() + file.getOriginalFilename();
		//이미지 경로 저장
		File saveFile = new File(rootLocation, saveFileName);
		FileCopyUtils.copy(file.getBytes(), saveFile);
		//saveThumbnailName 생성
		String saveThumbnailName = "s_" + uuid.toString() + file.getOriginalFilename();
		//썸네일 경로 저장
		File saveThumbnail = new File(rootLocation, saveThumbnailName);
		FileCopyUtils.copy(file.getBytes(), saveThumbnail);
		Thumbnailator.createThumbnail(saveThumbnail, saveThumbnail, 230, 230);

		return saveFileName;
	}

	//이미지 DB 저장
	public UploadImage store(MultipartFile file) throws Exception {
		//		 fileName : 예제1.jpg
		//		 filePath : c:/upload/21/11/17/uuid-예제1.jpg
		//		 saveFileName : uuid-예제.png
		//		 contentType : image/jpeg
		//		 size : 4994942
		try {
			if(file.isEmpty()) {
				throw new Exception("Failed to store empty file " + file.getOriginalFilename());
			}
			UploadImage saveFile = new UploadImage();

			String folderPath = makeFolder();
			String saveFileName = fileSave(rootLocation.toString()+'/' + folderPath +'/', file);

			saveFile.setFileName(file.getOriginalFilename());
			saveFile.setSaveFileName(saveFileName);
			saveFile.setContentType(file.getContentType());
			saveFile.setSize(file.getResource().contentLength());
			saveFile.setFilePath(rootLocation.toString().replace(File.separatorChar, '/')+'/' + folderPath +'/' + saveFileName);
			uploadImageRepository.save(saveFile);

			return saveFile;
		} catch(IOException e) {
			throw new Exception("Failed to store file " + file.getOriginalFilename(), e);
		}
	}
}