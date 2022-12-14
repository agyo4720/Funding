package com.funding.fundBoard;


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
import com.funding.fundArtist.FundArtistService;
import com.funding.fundArtistList.FundArtistList;
import com.funding.fundArtistList.FundArtistListService;
import com.funding.fundList.FundList;
import com.funding.fundList.FundListService;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;
import com.funding.sale.Sale;
import com.funding.sale.SaleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/fundBoard")
public class FundBoardController {

	private final FundBoardService fundBoardService;
	private final FundUserService fundUserService;
	private final CategorieService categorieService;
	private final AnswerService answerService;
	private final FileService fileService;
	private final SaleRepository saleRepository;
	private final FundListService fundListService;
	private final FundArtistListService fundArtistListService;
	private final FundArtistService fundArtistService;
	private final CancelsController cancelsController;
	private final CancelsService cancelsService;
	private final AlertService alertService;

	// ????????? ?????? ?????????(?????????)
	// URL??? ????????? ?????? page??? ???????????? ?????? ?????? ????????? ????????? 0??? ????????? ??????
	@RequestMapping("/list")
	public String list(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			Model model) {


		Page<FundBoard> fundBoardList = this.fundBoardService.findAll(page);
		model.addAttribute("fundBoardList", fundBoardList);

		List<Categorie> categorieList = this.categorieService.findAll();
		model.addAttribute("categorieList", categorieList);

		return "fundBoard/fundBoard_list";
	}

	// ????????? ?????? ??????(GET)
	@GetMapping("/create")
	public String create(
			FundBoardForm fundBoardForm,
			Principal principal,
			Model model) {

		Optional<FundUser> fundUser = this.fundUserService.findByuserName(principal.getName());

		List<Categorie> categorieList = this.categorieService.findAll();
		model.addAttribute("categorieList", categorieList);

		String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		model.addAttribute("nowTime", nowTime);

		return "/fundBoard/fundBoard_form";
	}

	// ????????? ?????? ??????(POST)
	@PostMapping("/create")
	public String create(
			@Valid FundBoardForm fundBoardForm,
			@RequestParam(value="imgPath", defaultValue="x") String imgPath,
			@RequestParam(value="file", defaultValue="x") MultipartFile files,
			BindingResult bindingResult,
			Principal principal,
			Model model) throws IllegalStateException, IOException {

		// ?????? ???????????? ?????? ???????????? ????????? ????????? ??????
		// String time = fundBoardForm.getStartDate() + " " + fundBoardForm.getStartTime();

		if(bindingResult.hasErrors()) {

			List<Categorie> categorieList = this.categorieService.findAll();
			model.addAttribute("categorieList", categorieList);

			return "/fundBoard/fundBoard_form";
		}

		Optional<FundUser> fundUser = this.fundUserService.findByuserName(principal.getName());

		if(!imgPath.equals("x") && files.isEmpty()) {
			this.fundBoardService.createImg(
					fundBoardForm.getCategorieName(),
					fundBoardForm.getSubject(),
					fundBoardForm.getContent(),
					fundBoardForm.getPlace(),
					fundBoardForm.getStartDateTime(),
					fundBoardForm.getFundDuration(),
					fundBoardForm.getRuntime(),
					fundBoardForm.getMinFund(),
					fundBoardForm.getFundAmount(),
					imgPath,
					fundBoardForm.getCreateDate(),
					fundUser.get()
					);

		}else if(!files.isEmpty()) {

			String savePath = this.fileService.saveFile(files);

			this.fundBoardService.createFile(
					fundBoardForm.getCategorieName(),
					fundBoardForm.getSubject(),
					fundBoardForm.getContent(),
					fundBoardForm.getPlace(),
					fundBoardForm.getStartDateTime(),
					fundBoardForm.getFundDuration(),
					fundBoardForm.getRuntime(),
					fundBoardForm.getMinFund(),
					fundBoardForm.getFundAmount(),
					savePath,
					fundBoardForm.getCreateDate(),
					fundUser.get()
					);

		}

		return "redirect:/fundBoard/list";

	}
	
	// ????????? ?????? ?????????
	@RequestMapping("/detail/{id}")
	public String detail(
			@PathVariable ("id") Integer id,
			Principal principal,
			Model model,
			Integer alertId) throws Exception{

		FundBoard fundBoard = this.fundBoardService.findById(id);
		model.addAttribute("fundBoard", fundBoard);
		
		List<Answer> answerList = this.answerService.findByFundBoard(fundBoard);
		model.addAttribute("answerList", answerList);
		
		List<FundArtistList> fundArtistList = this.fundArtistListService.findByFundBoard(fundBoard);
		model.addAttribute("fundArtistList", fundArtistList);
		
		//???????????? ???????????? ??? ?????? ??????
		if(alertId != null) {
			alertService.deleteAlert(alertId);
		}

		//?????????????????? ???????????? ??????
		List<FundList> fList = fundListService.findByFundBoard(fundBoard);
		
		//????????????
		FundBoard nick = fundBoardService.findById(id);
		List<Sale> sale = saleRepository.findByFundBoard(nick.getSubject());
		
		for(int i=0; i<sale.size(); i++){
			sale.get(i).getPayCode();
			model.addAttribute("payCode",sale.get(i).getPayCode());
		}
		
		//?????? ?????? ??????
		boolean result = false;
		
		if(principal != null) {
			for(FundList e : fList) {
				String username = e.getFundUser().getUsername();
				String loginName = principal.getName();
				
				if(username.equals(loginName)) {
					result = true;
				}
			}
		}
		
		model.addAttribute("result", result);
		
		//?????? ????????? ???????????? ?????????
		if(fundBoard.getState().equals("100%?????????")) {
			alertService.fundBoardSuccess(fundBoard);
		}

		return "/fundBoard/fundBoard_detail";
	}
	

	// id??? ???????????? ????????? ????????????
	@RequestMapping("/categorie/{id}")
	public String categorie(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@PathVariable("id") Integer id,
			Model model) {

		Categorie categorie = this.categorieService.findById(id);

		Page<FundBoard> fundBoardList = this.fundBoardService.findByCategorie(page, categorie);
		model.addAttribute("fundBoardList", fundBoardList);

		List<Categorie> categorieList = this.categorieService.findAll();
		model.addAttribute("categorieList", categorieList);

		return "/fundBoard/fundBoard_list";
	}

	// ?????? ????????? ?????????
	@GetMapping("/img/{id}")
	@ResponseBody
	public Resource showImg(@PathVariable("id") Integer id) throws IOException{
		FundBoard fundBoard = this.fundBoardService.findById(id);
		String filePath = fundBoard.getFilePath();
		return new UrlResource("file:" + filePath);
	}


	// ????????? ?????? ????????????
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id) throws Exception {

		//????????? ????????? ??????
		FundBoard nick = fundBoardService.findById(id);
		List<Sale> sale = saleRepository.findByFundBoard(nick.getSubject());
		for(int i=0; i<sale.size(); i++){
			if(sale.get(i).getCheckin().equals("????????????")) {
				cancelsController.totalCancel(sale.get(i).getPayCode(),"????????? ??????");
				cancelsService.totalCancelInfo(sale.get(i).getOrederId(), Integer.valueOf(sale.get(i).getPayMoney()).intValue(), sale.get(i).getOrderName(),
						"????????? ??????",sale.get(i).getFundUser(),sale.get(i).getUsername());
			}
		}

		//????????? ????????? ??????
		List<FundList> fList = fundListService.findByFundBoard(nick);
		for(int i=0;i>fList.size();i++) {
			fundListService.deleteFund(fList.get(i).getFundUser(), nick);
		}
		
		//????????? ?????? ??????
		alertService.deleteBoardThenAlert(fList);
		
		this.fundBoardService.delete(id);

		return "redirect:/fundBoard/list";
	}
	
	// 2022/12/21 - 1 ??????

//	????????? ???????????? ?????? ?????? ?????? ?????????
	@GetMapping("/modify/{id}")
	public String modify(@PathVariable ("id") Integer id, Model model) {
		
		log.info(">>> ");
		FundBoard fundBoard = this.fundBoardService.findById(id);
		model.addAttribute("fundBoard", fundBoard);
		return "/fundBoard/fundBoard_modify";
	}
	
	@PostMapping("/modify/{id}")
	public String modifyEnd(@PathVariable ("id") Integer id,String place) {
		
		FundBoard fundBoard = this.fundBoardService.findById(id);
		fundBoard.setPlace(place);
		fundBoardService.modify(fundBoard);
		return "redirect:/fundBoard/list";
	}
}
