package com.funding.sale;

import java.security.Principal;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.funding.fundBoard.FundBoard;
import com.funding.fundBoard.FundBoardService;
import com.funding.fundBoardTarget.FundBoardTarget;
import com.funding.fundBoardTarget.FundTargetService;
import com.funding.fundList.FundListService;
import com.funding.fundTargetList.FundTargetListService;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserRepository;
import com.funding.fundUser.FundUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/pay")
@Controller
public class SaleController {
	private final String SECRET_KEY = "test_sk_JQbgMGZzorzl7aMN4D3l5E1em4dK";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final FundTargetService fundTargetService;
    private final FundUserService fundUserService;
    private final SaleService saleService;
    private final FundTargetListService fundTargetListService;
    private final FundBoardService fundBoardService;
    private final FundListService fundListService;
    
    
	
	//지정 결제
    @RequestMapping("/tossPayTar/{id}")
    public String tossPayTar(Principal principal, Model model, @PathVariable("id")Integer id) {
		FundBoardTarget fundBoardTarget = fundTargetService.findById(id);
		model.addAttribute("fundBoardTarget", fundBoardTarget);//지정공연 번호

		Optional<FundUser> FU = fundUserService.findByuserName(principal.getName());
		model.addAttribute("userData",FU.get());//로그인중인 정보
    	return "pay/tossPayTar";
    }
    //지정 결제성공
    @RequestMapping("/successTar")
    public String confirmPayment(
            @RequestParam String paymentKey, @RequestParam String orderId, @RequestParam int amount,
            Model model, Principal principal) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));//토스에서 개인식별(시크릿키)
        headers.setContentType(MediaType.APPLICATION_JSON);//json형식 사용

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);//주문번호
        payloadMap.put("amount", String.valueOf(amount));//금액

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);// JSON -> String

        Optional<FundUser> FU = fundUserService.findByuserName(principal.getName());//로그인중인 아이디

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);//토스에서 요구하는 주소 + JsonNode

        if (responseEntity.getStatusCode() == HttpStatus.OK) {//코드 200
            JsonNode successNode = responseEntity.getBody();
            model.addAttribute("status", successNode.get("status").asText());//상태
            model.addAttribute("balanceAmount", successNode.get("balanceAmount").asText());//금액
            model.addAttribute("orderName", successNode.get("orderName").asText());//공연이름
            model.addAttribute("orderId", successNode.get("orderId").asText());//주문번호
            model.addAttribute("method", successNode.get("method").asText());//결제방식

            String orderName = successNode.get("orderName").asText();//공연이름
            saleService.targetSaveinfo(paymentKey, orderId, amount, orderName, FU);

        	//누적금액증가, 사람 수 증가
        	String tar = successNode.get("orderId").toString();//결제한 주문번호를 들고옴
        	String target = tar.substring(tar.lastIndexOf('-')+1);// 주문번호 -    삭제하고 뒤에 번호로 어떤공연인지 식별
        	target = target.replace("\"", "");//문자열 값 변경

        	FundBoardTarget targetPk = fundTargetService.findById(Integer.parseInt(target));// 식별한 번호로 공연정보 찾음
        	Integer add = targetPk.getFundCurrent();//총펀딩금액 들고옴
        	add += amount;//결제한 금액만큼 증가
        	targetPk.setFundCurrent(add);//증가한 펀딩금액을 저장
        	Integer cMem = targetPk.getCurrentMember();//펀딩한 사람수
        	cMem++;//결제했으면 펀딩한 사람수에서 증가
        	targetPk.setCurrentMember(cMem);//증가한 사람수를 저장
        	fundTargetService.addTargetFund(targetPk);

        	//유저의 현재 펀딩 목록 추가
        	fundTargetListService.insertList(principal, targetPk);

            return "/pay/successTar";
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());//실패사유
            model.addAttribute("code", failNode.get("code").asText());//실패코드
            return "pay/failTar";
        }
    }




    //미지정 결제
    @RequestMapping("/tossPay/{id}")
    public String tossPay(Principal principal, Model model, @PathVariable("id")Integer id) {
		FundBoard fundBoard = fundBoardService.findById(id);
		model.addAttribute("fundBoard", fundBoard);//미지정공연 번호

		Optional<FundUser> FU = fundUserService.findByuserName(principal.getName());
		model.addAttribute("userData",FU.get());//로그인정보
    	return "pay/tossPay";
    }
    //미지정 결제성공
    @RequestMapping("/success")
    public String confirmPayment1(
            @RequestParam String paymentKey, @RequestParam String orderId, @RequestParam int amount,
            Model model, Principal principal) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));//토스에서 개인식별(시크릿키)
        headers.setContentType(MediaType.APPLICATION_JSON);//json형식 사용

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);//주문번호
        payloadMap.put("amount", String.valueOf(amount));//금액

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);// JSON -> String

        Optional<FundUser> FU = fundUserService.findByuserName(principal.getName());//로그인중인 아이디

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);//토스에서 요구하는 주소 + JsonNode
        if (responseEntity.getStatusCode() == HttpStatus.OK) {//코드 200
            JsonNode successNode = responseEntity.getBody();
            model.addAttribute("balanceAmount", successNode.get("balanceAmount").asText());//금액
            model.addAttribute("orderName", successNode.get("orderName").asText());//공연이름
            model.addAttribute("orderId", successNode.get("orderId").asText());//주문번호
            model.addAttribute("status", successNode.get("status").asText());//상태
            model.addAttribute("method", successNode.get("method").asText());//결제방식

            String orderName = successNode.get("orderName").asText();//공연이름
            saleService.saveinfo(paymentKey, orderId, amount, orderName, FU);

        	//누적금액증가
        	String tar = successNode.get("orderId").toString();//결제한 주문번호를 들고옴
        	String target = tar.substring(tar.lastIndexOf('-')+1);// 주문번호 -    삭제하고 뒤에 번호로 어떤공연인지 식별
        	target = target.replace("\"", "");//문자열 값 변경

        	FundBoard fundBoard = fundBoardService.findById(Integer.parseInt(target));// 식별한 번호로 공연정보 찾음
        	Integer add = fundBoard.getFundCurrent();//총펀딩금액 들고옴
        	add += amount;//결제한 금액만큼 증가
        	fundBoard.setFundCurrent(add);//증가한 펀딩금액을 저장
        	//누적사람 수 증가
        	Integer cMem = fundBoard.getCurrentMember();//펀딩한 사람수
        	cMem++;//결제했으면 펀딩한 사람수에서 증가
        	fundBoard.setCurrentMember(cMem);//증가한 사람수를 저장
        	fundBoardService.addFundBoard(fundBoard);


        	//유저의 현재 펀딩 목록 추가
        	fundListService.insertList(principal, fundBoard);

            return "/pay/success";
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());//실패사유
            model.addAttribute("code", failNode.get("code").asText());//실패코드
            return "pay/fail";
        }
    }
    
	//결제목록
	@GetMapping("/loo/confirm")
	public String confirm(Principal principal, Model model,@RequestParam(value = "page", defaultValue="0") int page) throws Exception{
		
		Optional<FundUser> FU = fundUserService.findByuserName(principal.getName());//로그인중인 아이디일치하는 정보들

		//결제리스트 불러오기
		Page<Sale> sList = saleService.findByUsername(page,FU.get().getUsername());//로그인중인 아이디일치하는 정보들 페이징
		model.addAttribute("sList",sList);
		model.addAttribute("page",page);
		
		return "/pay/loo/confirm";
	}
}
