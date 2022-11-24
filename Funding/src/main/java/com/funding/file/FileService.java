package com.funding.file;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FileService {

	private final FileRepository fileRepository;
	
	@Value("${fileDir}")
	private String fileDir;
	
	//파일 저장(경로 c\fundFiles)
	public String saveFile(MultipartFile getfile) throws IllegalStateException, IOException {
		if(getfile.isEmpty()) {
			System.out.println("파일 없어요");
			return null;
		}
		
		String originName = getfile.getOriginalFilename();
		String extension = originName.substring(originName.lastIndexOf("."));
		String uuid = UUID.randomUUID().toString();
		String saveName = uuid + extension;
		String savePath = fileDir + "\\" + saveName;
		
		FileDto file = new FileDto(originName,saveName,savePath);
		
		//저장공간 생성
		File fileFolder = new File(fileDir);
		fileFolder.mkdir();
		
		getfile.transferTo(new File(savePath));
		fileRepository.save(file);
		
		return savePath;
	}
	
	//파일 id로 리턴 받기
	public Integer saveFileId(MultipartFile getfile) throws IllegalStateException, IOException {
		if(getfile.isEmpty()) {
			System.out.println("파일 없어요");
			return null;
		}
		
		String originName = getfile.getOriginalFilename();
		String extension = originName.substring(originName.lastIndexOf("."));
		String uuid = UUID.randomUUID().toString();
		String saveName = uuid + extension;
		String savePath = fileDir + "\\" + saveName;
		
		FileDto file = new FileDto(originName,saveName,savePath);
		
		//저장공간 생성
		File fileFolder = new File(fileDir);
		fileFolder.mkdir();
		
		getfile.transferTo(new File(savePath));
		fileRepository.save(file);
		
		return file.getId();
	}
	
	
	//findById
	public FileDto findById(Integer id) {
		Optional<FileDto> file = fileRepository.findById(id);
		
		return file.get();
	}
	
}
