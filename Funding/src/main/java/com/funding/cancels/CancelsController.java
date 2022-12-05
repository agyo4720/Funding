package com.funding.cancels;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.Principal;
import java.util.Base64;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.funding.fundBoard.FundBoard;
import com.funding.fundBoard.FundBoardService;
import com.funding.fundBoardTarget.FundBoardTarget;
import com.funding.fundBoardTarget.FundTargetService;
import com.funding.fundList.FundListService;
import com.funding.fundTargetList.FundTargetListService;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/pay")
@Controller
public class CancelsController {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String SECRET_KEY = "test_sk_JQbgMGZzorzl7aMN4D3l5E1em4dK";
    private String paymentKey;
   
    private final FundUserService fundUserService;
    private final CancelsService cancelsService;
    private final FundTargetService fundTargetService;
    private final FundTargetListService fundTargetListService;
    private final FundBoardService fundBoardService;
    private final FundListService fundListService;
	
	
    //지정환불하기
    @RequestMapping("/can/tarCancel")
    public String tarCancel(String paymentKey)throws Exception {
    	this.paymentKey =  paymentKey; //환불시 개인키가 필요(클릭으로 고유개인키 넘기기)
    	return "/pay/can/tarCancel";
    }
    //지정환불성공
    @RequestMapping("/can/tarCancelRquest")
    public String tarCancelRquest(String paymentKey,
    		@RequestParam("cancelReason")String cancelReason, Model model, Principal principal)throws Exception{
    		paymentKey = this.paymentKey;

    		HttpRequest request = HttpRequest.newBuilder()
    		    .uri(URI.create("https://api.tosspayments.com/v1/payments/"+paymentKey+"/cancel"))//환불시 토스에서 요구하는 주소
    		    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))//토스에서 개인식별(시크릿키)
    		    .header("Content-Type", "application/json")//json형식 사용
    		    .method("POST", HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"" + cancelReason + "\"}"))//필수입력 값 환불사유
    		    .build();
    		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());// JSON -> String

    		//심플JSON 사용
    		JSONParser parser = new JSONParser();
    		Object obj = parser.parse(response.body());
    		JSONObject jsonObj = (JSONObject)obj;
    		if(response.statusCode() == 200) { //코드값 200이면 성공
    			//JSON -> String
    			String orderName = (String)jsonObj.get("orderName");
    			String orderId = (String)jsonObj.get("orderId");
    			String totalAmount = jsonObj.get("totalAmount").toString();

    			model.addAttribute("orderName",orderName);//상품명
    			model.addAttribute("orderId",orderId);//주문번호
    			model.addAttribute("totalAmount",totalAmount);//금액
    			model.addAttribute("cancelReason",cancelReason);//환불사유
    			
    			Optional<FundUser> FU =  fundUserService.findByuserName(principal.getName()); //로그인중인 아이디하고 일치하는 정보들을 찾음
    			cancelsService.cancelInfo(orderId, Integer.valueOf(totalAmount).intValue(), orderName, cancelReason, FU, paymentKey);//환불시 저장할 데이터들을 넘김


            	//누적금액감소, 인원 감소
    			JSONObject tar = (JSONObject) jsonObj;
    			String userAndTargetNo = (String)tar.get("orderId"); //환불한 주문번호를 들고옴

            	String target = userAndTargetNo.substring(userAndTargetNo.lastIndexOf('-')+1); // 주문번호 -    삭제하고 뒤에 번호로 어떤공연인지 식별  
            	target = target.replace("\"", ""); //문자열 값 변경

            	FundBoardTarget targetPk = fundTargetService.findById(Integer.parseInt(target)); // 식별한 번호로 공연정보 찾음
            	Integer sub = targetPk.getFundCurrent(); //총펀딩금액 들고옴
            	sub -= Integer.valueOf(totalAmount).intValue(); //환불한 금액만큼 감소
            	targetPk.setFundCurrent(sub); //감소한 펀딩금액을 저장

            	Integer cMem = targetPk.getCurrentMember(); //펀딩한 사람수
            	cMem--; //환불했으면 펀딩한 사람수에서 감소
            	targetPk.setCurrentMember(cMem); //감소한 사람수를 저장
            	fundTargetService.addTargetFund(targetPk); //레포지토리에 저장


            	//지정리스트 삭제
            	fundTargetListService.delete(FU.get(), targetPk);

    			return "/pay/can/cancelSuccess";
    		}else {
    			String message = (String)jsonObj.get("message"); //토스에서 실패한 사유를 String 변경
    			String code = (String)jsonObj.get("code"); //토스에서 실패한 코드를 String 변경
    			model.addAttribute("message",message); //실패사유
    			model.addAttribute("code",code); //실패코드
    			return "/pay/can/cancelFail";
    		}
    }

    
    //게시글삭제시 전체환불
    public void totalCancel(String paymentKey,String cancelReason) throws Exception {
		HttpRequest request = HttpRequest.newBuilder()
    		    .uri(URI.create("https://api.tosspayments.com/v1/payments/"+paymentKey+"/cancel"))//환불시 토스에서 요구하는 주소
    		    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))//토스에서 개인식별(시크릿키)
    		    .header("Content-Type", "application/json")//json형식 사용
    		    .method("POST", HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"" + cancelReason + "\"}")) //직접입력 안하고 삭제시 환불사유가 "게시글 삭제" 날아가게함
    		    .build();
    		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());// JSON -> String
    }
    
    

    //미지정환불하기
    @RequestMapping("/can/cancel")
    public String cancel(String paymentKey)throws Exception {
    	this.paymentKey =  paymentKey;
    	return "/pay/can/cancel";
    }
    //미지정환불성공
    @RequestMapping("/can/cancelRquest")
    public String cancelRquest(String paymentKey,
    		@RequestParam("cancelReason")String cancelReason, Model model, Principal principal)throws Exception{
    		paymentKey = this.paymentKey;

    		HttpRequest request = HttpRequest.newBuilder()
    		    .uri(URI.create("https://api.tosspayments.com/v1/payments/"+paymentKey+"/cancel"))//환불시 토스에서 요구하는 주소
    		    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))//토스에서 개인식별(시크릿키)
    		    .header("Content-Type", "application/json")//json형식 사용
    		    .method("POST", HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"" + cancelReason + "\"}"))//필수입력 값 환불사유
    		    .build();
    		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());// JSON -> String

    		//심플JSON 사용
    		JSONParser parser = new JSONParser();
    		Object obj = parser.parse(response.body());
    		JSONObject jsonObj = (JSONObject)obj;
    		if(response.statusCode() == 200) {
    			//JSON -> String
    			String orderName = (String)jsonObj.get("orderName");
    			String orderId = (String)jsonObj.get("orderId");
    			String totalAmount = jsonObj.get("totalAmount").toString();

    			model.addAttribute("orderName",orderName);//상품명
    			model.addAttribute("orderId",orderId);//주문번호
    			model.addAttribute("totalAmount",totalAmount);//금액
    			model.addAttribute("cancelReason",cancelReason);//환불사유
    			
    			Optional<FundUser> FU =  fundUserService.findByuserName(principal.getName());//로그인중인 아이디하고 일치하는 정보들을 찾음
    			cancelsService.cancelInfo(orderId, Integer.valueOf(totalAmount).intValue(), orderName, cancelReason, FU,paymentKey);

            	//누적금액감소
    			JSONObject tar = (JSONObject) jsonObj;
    			String userAndTargetNo = (String)tar.get("orderId");//환불한 주문번호를 들고옴

            	String target = userAndTargetNo.substring(userAndTargetNo.lastIndexOf('-')+1);// 주문번호 -    삭제하고 뒤에 번호로 어떤공연인지 식별
            	target = target.replace("\"", "");//문자열 값 변경

            	FundBoard fundBoard = fundBoardService.findById(Integer.parseInt(target));// 식별한 번호로 공연정보 찾음
            	Integer sub = fundBoard.getFundCurrent();//총펀딩금액 들고옴
            	sub -= Integer.valueOf(totalAmount).intValue(); //환불한 금액만큼 감소
            	fundBoard.setFundCurrent(sub);//감소한 펀딩금액을 저장

            	// 환불 인원 감소
            	Integer currentMember = fundBoard.getCurrentMember();//펀딩한 사람수
            	currentMember -= 1;//환불했으면 펀딩한 사람수에서 감소
            	fundBoard.setCurrentMember(currentMember);//감소한 사람수를 저장
            	fundBoardService.addFundBoard(fundBoard);

            	//지정리스트 삭제
            	fundListService.deleteFund(FU.get(), fundBoard);
            	
    			return "/pay/can/cancelSuccess";
    		}else {
    			//JSON -> String
    			String message = (String)jsonObj.get("message");
    			String code = (String)jsonObj.get("code");
    			model.addAttribute("message",message);//실패사유
    			model.addAttribute("code",code);//실패코드
    			return "/pay/can/cancelFail";
    		}
    }

}
