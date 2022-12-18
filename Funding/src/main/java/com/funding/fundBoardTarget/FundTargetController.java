package com.funding.fundBoardTarget;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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
import com.funding.alert.AlertService;
import com.funding.answer.Answer;
import com.funding.answer.AnswerService;
import com.funding.cancels.CancelsController;
import com.funding.cancels.CancelsService;
import com.funding.file.FileService;
import com.funding.fundTargetList.FundTargetList;
import com.funding.fundTargetList.FundTargetListService;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;
import com.funding.sale.Sale;
import com.funding.sale.SaleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/fundTarget")
@RequiredArgsConstructor
@Controller
public class FundTargetController {

	private final FundTargetService fundTargetService;
	private final FundTargetListService fundTargetListService;
	private final CategorieService categorieService;
	private final AnswerService answerService;
	private final FileService fileService;
	private final FundUserService fundUserService;
	private final AlertService alertService;
	private final SaleRepository saleRepository;
	private final CancelsController cancelsController;
	private final CancelsService cancelsService;

	
	
	//글 작성폼 불러오기
	@GetMapping("/form")
	private String form(TargetForm targetForm, Model model,Principal principal) {
		List<Categorie> cList = categorieService.findAll();
		
		//유저 확인 로직(현재는 권한 기능 없으니 임시로 열어둠)
		if(principal != null) {
			
			log.info("현재 유저 이름 :" + principal.getName());
			Optional<FundUser> user = fundUserService.findByuserName(principal.getName());
			if(user.isEmpty()) {
				return "/fundTarget/notUser";
			}
		}
		
		
		String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		
		model.addAttribute("cList",cList);
		model.addAttribute("nowTime", nowTime);
		return "/fundTarget/fundTargetForm";
	}
	
	
	//글 작성하기
	@PostMapping("/form")
	private String create(@Valid TargetForm targetForm,BindingResult bindingResult,
			@RequestParam("categorie")Integer cid, @RequestParam(value="imgPath", defaultValue = "x")String imgPath
			,@RequestParam(value="file", defaultValue = "x")MultipartFile files, Model model,Principal principal) throws IllegalStateException, IOException {
		
		String startTime = targetForm.getStartDate();
		Categorie categorie = categorieService.findById(cid);
		List<Categorie> cList = categorieService.findAll();		
		
		if(imgPath.equals("x") && files.isEmpty()) {
			bindingResult.reject("noImgError", "이미지를 선택해 주세요");
		}
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("cList",cList);
			return "/fundTarget/fundTargetForm";
		}
		
		Optional<FundUser> user = fundUserService.findByuserName(principal.getName());
	
	
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
					imgPath,
					user.get()
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
					savePath,
					user.get()
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
	public String showDetail(Model model, @PathVariable("id")Integer id,Integer alertId, Principal principal) {
		FundBoardTarget fundBoardTarget = fundTargetService.findById(id);
		List<Answer> aList = answerService.findByFundBoardTarget(fundBoardTarget);
		List<FundTargetList> ftList = fundTargetListService.findByFundBoardTarget(fundBoardTarget);
		
		//환불
		FundBoardTarget nick = fundTargetService.findById(id);
		List<Sale> sale = saleRepository.findByFundBoardTarget(nick.getSubject());
		for(int i=0; i<sale.size(); i++){
			sale.get(i).getPayCode();
			model.addAttribute("payCode",sale.get(i).getPayCode());
		}

		
		//알림삭제
		if(alertId != null) {
			alertService.deleteAlert(alertId);
		}
		
		//펀딩 유무 확인
		boolean result = false;
		if(principal != null) {
			for(FundTargetList e : ftList) {
				String username = e.getFundUser().getUsername();
				String loginName = principal.getName();
				if(username.equals(loginName)) {
					result = true;
				}
			}
		}
		
		
		model.addAttribute("result", result);
		model.addAttribute("aList", aList);
		model.addAttribute("fundBoardTarget", fundBoardTarget);
		return "/fundTarget/fundTargetDetail";
	}
	
	//이미지 보이기
	@GetMapping("/img/{id}")
	@ResponseBody
	public Resource showImg(@PathVariable("id")Integer id) throws IOException {
		FundBoardTarget target = fundTargetService.findById(id);
		String imgPath = target.getFilePath();
		
		return new UrlResource("file:" + imgPath);
	}
	
	
	
	//지정펀딩 삭제
	@RequestMapping("/delete/{id}")
	public String deleteTarget(@PathVariable("id")Integer id) throws Exception {

		//환불
		FundBoardTarget nick = fundTargetService.findById(id);
		List<Sale> sale = saleRepository.findByFundBoardTarget(nick.getSubject());
		for(int i=0; i<sale.size(); i++){
			if(sale.get(i).getCheckin().equals("결제완료")) {
				cancelsController.totalCancel(sale.get(i).getPayCode(),"게시글 삭제");
				cancelsService.totalCancelInfo(sale.get(i).getOrederId(), Integer.valueOf(sale.get(i).getPayMoney()).intValue(), sale.get(i).getOrderName(), 
						"게시글 삭제",sale.get(i).getFundUser(),sale.get(i).getUsername());
			}
		}
		
		log.info("삭제컨트롤로 실행됨");
		//지정리스트 삭제
		List<FundTargetList> fList = fundTargetListService.findByFundBoardTarget(nick);
		for(int i=0;i>fList.size();i++) {
			fundTargetListService.delete(fList.get(i).getFundUser(), nick);
		}
		//삭제 알림 추가
		alertService.deleteTargetThenAlert(fList);
		
		fundTargetService.delete(id);
		return "redirect:/";
	}
	
	
}
