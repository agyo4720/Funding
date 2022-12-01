package com.funding.fundBoard;


import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
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
import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistService;
import com.funding.fundBoardTarget.FundBoardTarget;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;
import com.funding.payment.PaymentController;
import com.funding.payment.Sale;
import com.funding.payment.SaleRepository;

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
	private final PaymentController paymentController;



	// 미지정 펀드 리스트(페이징)
	// URL에 페이지 변수 page가 전달되지 않은 경우 디폴트 값으로 0이 되도록 설정
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

	// 미지정 펀드 등록(GET)
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

	// 미지정 펀드 등록(POST)
	@PostMapping("/create")
	public String create(
			@Valid FundBoardForm fundBoardForm,
			@RequestParam(value="imgPath", defaultValue="x") String imgPath,
			@RequestParam(value="file", defaultValue="x") MultipartFile files,
			BindingResult bindingResult,
			Principal principal,
			Model model) throws IllegalStateException, IOException {

		// 날짜 데이터와 시간 데이터를 합쳐서 데이터 넣기
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

	// 미지정 펀드 답변등록
	@RequestMapping("/detail/{id}")
	public String detail(@PathVariable ("id") Integer id, Model model) {

		FundBoard fundBoard = this.fundBoardService.findById(id);
		model.addAttribute("fundBoard", fundBoard);

		List<Answer> answerList = this.answerService.findByFundBoard(fundBoard);
		model.addAttribute("answerList", answerList);

		return "/fundBoard/fundBoard_detail";
	}

	// id로 카테고리 리스트 가져오기
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

	// 파일 이미지 보이기
	@GetMapping("/img/{id}")
	@ResponseBody
	public Resource showImg(@PathVariable("id") Integer id) throws IOException{
		FundBoard fundBoard = this.fundBoardService.findById(id);
		String filePath = fundBoard.getFilePath();
		return new UrlResource("file:" + filePath);
	}


	// 미지정 펀드 삭제하기
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id) throws Exception {

		//환불
		FundBoard nick = fundBoardService.findById(id);
		List<Sale> sale = saleRepository.findByFundBoardTarget(nick.getSubject());
		for(int i=0; i<sale.size(); i++){
			sale.get(i).getPayCode();
			sale.get(i).setCheckin("게시글 삭제");

			paymentController.totalCancel(sale.get(i).getPayCode(),"게시글 삭제");
		}


		this.fundBoardService.delete(id);

		return "redirect:/fundBoard/list";
	}

	// 2022/11/30 - 4 작업중


}
