package com.funding.file;

import java.io.IOException;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/file")
@RequiredArgsConstructor
@Controller
public class FileController {

	private final FileService fileService;
	
	
	@RequestMapping("/summernote/file")
	@ResponseBody
	public String summernoteImageFile(MultipartHttpServletRequest request) {
		
		Map<String, MultipartFile> mf = request.getFileMap();
		MultipartFile file = mf.get("file");
		
		try {
			Integer fileId = fileService.saveFileId(file);
			String path = "/file/show/" + fileId;
			log.info(path);
			return path;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return "반환 실패";
	}
	
	//이미지 보이기
	@GetMapping("/show/{id}")
	@ResponseBody
	public Resource showImage(@PathVariable("id")Integer id) throws IOException{
		FileDto file = fileService.findById(id);
		String filePath = file.getSavePath();
		
		return new UrlResource("file:" + filePath);
		
	}
	
}
