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
import com.funding.fundUser.FundUserRepository;

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
    private final FundUserRepository fundUserRepository;
    private final CancelsService cancelsService;
    private final FundTargetService fundTargetService;
    private final FundTargetListService fundTargetListService;
    private final FundBoardService fundBoardService;
    private final FundListService fundListService;
	
	
    //지정환불하기
    @RequestMapping("/can/tarCancel")
    public String tarCancel(String paymentKey)throws Exception {
    	this.paymentKey =  paymentKey;
    	return "/pay/can/tarCancel";
    }
    //지정환불성공
    @RequestMapping("/can/tarCancelRquest")
    public String tarCancelRquest(String paymentKey,
    		@RequestParam("cancelReason")String cancelReason, Model model, Principal principal)throws Exception{
    		paymentKey = this.paymentKey;

    		HttpRequest request = HttpRequest.newBuilder()
    		    .uri(URI.create("https://api.tosspayments.com/v1/payments/"+paymentKey+"/cancel"))
    		    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))
    		    .header("Content-Type", "application/json")
    		    .method("POST", HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"" + cancelReason + "\"}"))
    		    .build();
    		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

    		JSONParser parser = new JSONParser();
    		Object obj = parser.parse(response.body());
    		JSONObject jsonObj = (JSONObject)obj;
        	log.info("jsonObj: "+jsonObj.toString());
    		if(response.statusCode() == 200) {
    			String orderName = (String)jsonObj.get("orderName");
    			String orderId = (String)jsonObj.get("orderId");
    			String totalAmount = jsonObj.get("totalAmount").toString();

    			model.addAttribute("orderName",orderName);//상품명
    			model.addAttribute("orderId",orderId);//주문번호
    			model.addAttribute("totalAmount",totalAmount);//금액
    			model.addAttribute("cancelReason",cancelReason);//환불사유
    			principal.getName();
    			Optional<FundUser> FU =  fundUserRepository.findByusername(principal.getName());
    			cancelsService.cancelInfo(orderId, Integer.valueOf(totalAmount).intValue(), orderName, cancelReason, FU, paymentKey);


            	//누적금액감소, 인원 감소
    			JSONObject tar = (JSONObject) jsonObj;
    			String userAndTargetNo = (String)tar.get("orderId");

            	String target = userAndTargetNo.substring(userAndTargetNo.lastIndexOf('-')+1);
            	target = target.replace("\"", "");
            	log.info("target: "+target);

            	FundBoardTarget targetPk = fundTargetService.findById(Integer.parseInt(target));
            	Integer sub = targetPk.getFundCurrent();
            	sub -= Integer.valueOf(totalAmount).intValue();
            	targetPk.setFundCurrent(sub);

            	Integer cMem = targetPk.getCurrentMember();
            	cMem--;
            	targetPk.setCurrentMember(cMem);
            	fundTargetService.addTargetFund(targetPk);


            	//지정리스트 삭제
            	fundTargetListService.delete(FU.get(), targetPk);

    			return "/pay/can/cancelSuccess";
    		}else {
    			String message = (String)jsonObj.get("message");
    			String code = (String)jsonObj.get("code");
    			model.addAttribute("message",message);
    			model.addAttribute("code",code);
    			return "/pay/can/cancelFail";
    		}
    }

    
    //게시글삭제 전체환불
    public void totalCancel(String paymentKey,String cancelReason) throws Exception {
		HttpRequest request = HttpRequest.newBuilder()
    		    .uri(URI.create("https://api.tosspayments.com/v1/payments/"+paymentKey+"/cancel"))
    		    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))
    		    .header("Content-Type", "application/json")
    		    .method("POST", HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"" + cancelReason + "\"}"))
    		    .build();
    		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
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
    		    .uri(URI.create("https://api.tosspayments.com/v1/payments/"+paymentKey+"/cancel"))
    		    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))
    		    .header("Content-Type", "application/json")
    		    .method("POST", HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"" + cancelReason + "\"}"))
    		    .build();
    		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

    		JSONParser parser = new JSONParser();
    		Object obj = parser.parse(response.body());
    		JSONObject jsonObj = (JSONObject)obj;
        	log.info("jsonObj: "+jsonObj.toString());
    		if(response.statusCode() == 200) {
    			String orderName = (String)jsonObj.get("orderName");
    			String orderId = (String)jsonObj.get("orderId");
    			String totalAmount = jsonObj.get("totalAmount").toString();

    			model.addAttribute("orderName",orderName);//상품명
    			model.addAttribute("orderId",orderId);//주문번호
    			model.addAttribute("totalAmount",totalAmount);//금액
    			model.addAttribute("cancelReason",cancelReason);//환불사유
    			principal.getName();
    			Optional<FundUser> FU =  fundUserRepository.findByusername(principal.getName());
    			cancelsService.cancelInfo(orderId, Integer.valueOf(totalAmount).intValue(), orderName, cancelReason, FU,paymentKey);

            	//누적금액감소
    			JSONObject tar = (JSONObject) jsonObj;
    			String userAndTargetNo = (String)tar.get("orderId");

            	String target = userAndTargetNo.substring(userAndTargetNo.lastIndexOf('-')+1);
            	target = target.replace("\"", "");
            	log.info("target: "+target);

            	FundBoard fundBoard = fundBoardService.findById(Integer.parseInt(target));
            	Integer sub = fundBoard.getFundCurrent();
            	sub -= Integer.valueOf(totalAmount).intValue();
            	fundBoard.setFundCurrent(sub);

            	// 환불 인원 감소
            	Integer currentMember = fundBoard.getCurrentMember();
            	currentMember -= 1;
            	fundBoard.setCurrentMember(currentMember);
            	fundBoardService.addFundBoard(fundBoard);

            	//지정리스트 삭제
            	fundListService.delete(FU.get(), fundBoard);
            	
    			return "/pay/can/cancelSuccess";
    		}else {
    			String message = (String)jsonObj.get("message");
    			String code = (String)jsonObj.get("code");
    			model.addAttribute("message",message);
    			model.addAttribute("code",code);
    			return "/pay/can/cancelFail";
    		}
    }

}
