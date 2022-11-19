package com.funding.fundBoardTarget;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
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

import com.funding.Categorie.Categorie;
import com.funding.Categorie.CategorieService;
import com.funding.answer.Answer;
import com.funding.answer.AnswerService;
import com.funding.file.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/fundTarget")
@RequiredArgsConstructor
@Controller
public class FundTargetController {

	private final FundTargetService fundTargetService;
	private final CategorieService categorieService;
	private final AnswerService answerService;
	private final FileService fileService;
	
	//글 작성폼 불러오기
	@GetMapping("/form")
	private String form(TargetForm targetForm, Model model) {
		List<Categorie> cList = categorieService.findAll();
		model.addAttribute("cList",cList);
		return "/fundTarget/fundTargetForm";
	}
	
	
	//글 작성하기
	@PostMapping("/form")
	private String create(@Valid TargetForm targetForm,BindingResult bindingResult,
			@RequestParam("categorie")Integer cid, @RequestParam(value="imgPath", defaultValue = "x")String imgPath
			,@RequestParam(value="file", defaultValue = "x")MultipartFile files, Model model) throws IllegalStateException, IOException {
		
		String startTime = targetForm.getStartDate() + " " +  targetForm.getStartTime();
		Categorie categorie = categorieService.findById(cid);
		List<Categorie> cList = categorieService.findAll();
		
		if(imgPath.equals("x") && files.isEmpty()) {
			bindingResult.reject("noImgError", "이미지를 선택해 주세요");
		}
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("cList",cList);
			return "/fundTarget/fundTargetForm";
		}
		
		if(!imgPath.equals("x") && files.isEmpty()) {
			fundTargetService.createimg(
					targetForm.getSubject(), 
					targetForm.getContent(), 
					targetForm.getAertiest(),
					targetForm.getPlace(),
					targetForm.getRuntime(),
					targetForm.getFundDurationE(),
					startTime,
					targetForm.getMinFund(),
					targetForm.getFundAmount(),
					categorie,
					imgPath
					);
		}else if(!files.isEmpty()){
			
			String savePath = fileService.saveFile(files);
			
			fundTargetService.createfile(
					targetForm.getSubject(), 
					targetForm.getContent(), 
					targetForm.getAertiest(),
					targetForm.getPlace(),
					targetForm.getRuntime(),
					targetForm.getFundDurationE(),
					startTime,
					targetForm.getMinFund(),
					targetForm.getFundAmount(),
					categorie,
					savePath
					);
		}
		
		
		return "redirect:/";
	}
	
	//글 목록 보기
	@RequestMapping("")
	public String showList(Model model,@RequestParam(value = "page", defaultValue="0") int page,
			@RequestParam(value = "cate", defaultValue="0")Integer cateId) {
		//모든 카테고리 표시
		if(cateId == 0) {
			Page<FundBoardTarget> targetList = fundTargetService.findAll(page);
			List<Categorie> cList = categorieService.findAll();	
			
			model.addAttribute("page",page);
			model.addAttribute("cate",cateId);
			model.addAttribute("cList", cList);
			model.addAttribute("targetList", targetList);
			return "fundTarget/fundTargetList";
		//해당 카테고리 표시
		}else {
			Categorie categorie = categorieService.findById(cateId);
			Page<FundBoardTarget> targetList = fundTargetService.findByCategorie(categorie, page);
			List<Categorie> cList = categorieService.findAll();	
			
			model.addAttribute("page",page);
			model.addAttribute("cate",cateId);
			model.addAttribute("cList", cList);
			model.addAttribute("targetList", targetList);
			return "fundTarget/fundTargetList";
		}
		
	}
	
	//디테일 창으로
	@RequestMapping("/detail/{id}")
	public String showDetail(Model model, @PathVariable("id")Integer id) {
		FundBoardTarget fundBoardTarget = fundTargetService.findById(id);
		List<Answer> aList = answerService.findByFundBoardTarget(fundBoardTarget);
		
		model.addAttribute("aList", aList);
		model.addAttribute("fundBoardTarget", fundBoardTarget);
		return "/fundTarget/fundTargetDetail";
	}
	
	@GetMapping("/img/{id}")
	@ResponseBody
	public Resource showImg(@PathVariable("id")Integer id) throws IOException {
		FundBoardTarget target = fundTargetService.findById(id);
		String imgPath = target.getFilePath();
		
		return new UrlResource("file:" + imgPath);
	}
	
	
	
	
	
}
