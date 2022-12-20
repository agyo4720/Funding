package com.funding.selfBoard;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.funding.file.FileService;
import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequestMapping("/selfBoard")
@RequiredArgsConstructor
@Controller
public class SelfBoardController {
	
	private final SelfBoardService selfBoardService;
	private final FileService fileService;
	private final FundArtistService fundArtistService;
	
	//pr 폼 생성
	@GetMapping("/form")
	public String getForm(SelfBoardForm selfBoardForm) {
		return "/selfBoard/selfBoardForm";
	}
	
	//pr 작성하기
	@PostMapping("/form")
	public String createForm(@Valid SelfBoardForm selfBoardForm,BindingResult bindingResult, Principal principal, 
			@RequestParam("file")MultipartFile files, Model model) throws IllegalStateException, IOException {
		
		
		
		if(bindingResult.hasErrors()) {
			return "/selfBoard/selfBoardForm";
		}
		String savePath = "aa";
		
		if(!files.isEmpty()) {
			savePath = fileService.saveFile(files);
			
		}
		Optional<FundArtist> art = fundArtistService.findByuserName(principal.getName());
		
		Optional<SelfBoard> selfBoard = this.selfBoardService.findByFundArtist2(art.get());
		
		if(selfBoard.isEmpty()) {
			selfBoardService.create(
					selfBoardForm.getSubject(), 
					selfBoardForm.getContent(), 
					selfBoardForm.getGenre(), 
					savePath, 
					art.get()
					);
		}
		if(selfBoard.isPresent()) {
			log.info("수정작성됨");
			if(files.isEmpty()) {
				log.info("파일 없는 수정작성됨");
				selfBoardService.modify(
						selfBoardForm.getSubject(), 
						selfBoardForm.getContent(), 
						selfBoardForm.getGenre(), 
						selfBoard.get().getFilePath(), 
						art.get()
						);
				return "redirect:/selfBoard/detail";
			}
			selfBoardService.modify(
					selfBoardForm.getSubject(), 
					selfBoardForm.getContent(), 
					selfBoardForm.getGenre(), 
					savePath, 
					art.get()
					);
		}
		return "redirect:/selfBoard/detail";
	}
	
	
//	//디테일 보여주기
//	@RequestMapping("/detail/{id}")
//	public String showDetail(@PathVariable("id")Integer id, Model model) {
//		
//		SelfBoard selfBoard = selfBoardService.findById(id);
//		
//		model.addAttribute("selfBoard", selfBoard);
//		return "/selfBoard/selfBoardDetail";
//	}
	
	//디테일 보여주기 유저네임
	@RequestMapping("/detail/{username}")
	public String showDetail(@PathVariable("username")String username, Model model) {
		
		Optional<SelfBoard> selfBoard = selfBoardService.findByUsername(username);
		if(selfBoard.isEmpty()) {
			return "redirect:/selfBoard/form";
		}
		
		model.addAttribute("selfBoard", selfBoard.get());
		return "/selfBoard/selfBoardDetail";
	}
	
	//디테일 보여주기
	@RequestMapping("/detail")
	public String showDetail2(Principal principal, Model model) {
		
		Optional<SelfBoard> selfBoard = selfBoardService.findByUsername(principal.getName());
		if(selfBoard.isEmpty()) {
			return "redirect:/selfBoard/form";
		}
		
		model.addAttribute("selfBoard", selfBoard.get());
		return "/selfBoard/selfBoardDetail";
	}
	
	//이미지 보여주기
	@GetMapping("/img/{id}")
	@ResponseBody
	public Resource showImg(@PathVariable("id")Integer id) throws IOException {
		SelfBoard selfBoard = selfBoardService.findById(id);
		String imgPath = selfBoard.getFilePath();
		return new UrlResource("file:" + imgPath);
	}
	
	// 자기소개 수정
	@RequestMapping("/modify/{username}")
	public String modifySelfBoard(@PathVariable("username")String username, Model model) {
		
		Optional<SelfBoard> selfBoard = selfBoardService.findByUsername(username);
		model.addAttribute("selfBoardForm", selfBoard.get());
		return "/selfBoard/selfBoardForm";
	}
}