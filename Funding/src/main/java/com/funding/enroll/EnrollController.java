package com.funding.enroll;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/pay")
@Controller
public class EnrollController {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String SECRET_KEY = "test_sk_JQbgMGZzorzl7aMN4D3l5E1em4dK";
    private final EnrollService enrollService;
    
    
	//계좌등록
	@RequestMapping("/rem/enroll")
	public String enroll(){
			return "/pay/rem/enroll";
	}
	@RequestMapping("/rem/enrollRquest")
	public String enrollRquest(@RequestParam("subMallId")String subMallId, @RequestParam("companyName")String companyName,
			@RequestParam("representativeName")String representativeName, @RequestParam("businessNumber")String businessNumber,
			@RequestParam("bank")String bank, @RequestParam("accountNumber")String accountNumber,Model model) throws Exception {
			HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create("https://api.tosspayments.com/v1/payouts/sub-malls"))//토스에서 요구하는 주소
    		    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))//토스에서 개인식별(시크릿키)
    		    .header("Content-Type", "application/json")//json형식 사용
			    .method("POST", HttpRequest.BodyPublishers.ofString("{\"subMallId\":\""+subMallId+"\","
			    		+ "\"type\":\"CORPORATE\",\"companyName\":\""+companyName+"\",\"representativeName\":\""+representativeName+"\","
			    		+ "\"businessNumber\":\""+businessNumber+"\",\"account\":{\"bank\":\""+bank+"\",\"accountNumber\":\""+accountNumber+"\"}}"))//필수입력 값
			    .build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());// JSON -> String

    		if(response.statusCode() == 200) {//요청응답코드 200=성공
    			enrollService.enrollInfo(subMallId, companyName, representativeName, businessNumber, bank, accountNumber);
    			model.addAttribute("msg","SUCcess");//alert 사용
    			return "/pay/loo/lookup";
    		}else {
    			//심플JSON 사용
        		JSONParser parser = new JSONParser();
        		Object obj = parser.parse(response.body());
        		JSONObject jsonObj = (JSONObject)obj;
    			String message = (String)jsonObj.get("message");//토스에서 실패한 코드를 String 변경
    			model.addAttribute("message",message);//실패사유
    			model.addAttribute("msg","FAIl");//alert 사용
    			return "/pay/loo/lookup";
    		}
	}
	
	//계좌수정
	@RequestMapping("/rem/revise")
	public String revise(){
			return "/pay/rem/revise";
	}
	@RequestMapping("/rem/reviseRequest")
	public String reviseRequest(@RequestParam("subMallId")String subMallId,@RequestParam("bank")String bank,@RequestParam("companyName")String companyName,
			@RequestParam("representativeName")String representativeName,@RequestParam("businessNumber")String businessNumber,
			@RequestParam("accountNumber")String accountNumber, Model model)throws Exception {
		HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create("https://api.tosspayments.com/v1/payouts/sub-malls/"+subMallId))//토스에서 요구하는 주소
    		    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))//토스에서 개인식별(시크릿키)
    		    .header("Content-Type", "application/json")//json형식 사용
			    .method("POST", HttpRequest.BodyPublishers.ofString("{\"subMallId\":\""+subMallId+"\","
			    		+ "\"type\":\"CORPORATE\",\"companyName\":\""+companyName+"\",\"representativeName\":\""+representativeName+"\","
			    		+ "\"businessNumber\":\""+businessNumber+"\",\"account\":{\"bank\":\""+bank+"\",\"accountNumber\":\""+accountNumber+"\"}}"))//필수입력 값
			    .build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());// JSON -> String

    		if(response.statusCode() == 200) {//요청응답코드 200=성공
    			enrollService.reviseInfo(subMallId, companyName, representativeName, businessNumber, bank, accountNumber);
    			model.addAttribute("msg","SUccess");//alert 사용
    			return "/pay/loo/lookup";
    		}else {
    			//심플JSON 사용
        		JSONParser parser = new JSONParser();
        		Object obj = parser.parse(response.body());
        		JSONObject jsonObj = (JSONObject)obj;
    			String message = (String)jsonObj.get("message"); //토스에서 실패한 코드를 String 변경
    			model.addAttribute("message",message);//실패사유
    			model.addAttribute("msg","FAil");//alert 사용
    			return "/pay/loo/lookup";
    		}
	}

	//계좌삭제
	@RequestMapping("/rem/deletionRequest")
	public String deletionRequest(@RequestParam("subMallId")String subMallId,Model model)throws Exception {
			HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("https://api.tosspayments.com/v1/payouts/sub-malls/"+subMallId+"/delete"))//토스에서 요구하는 주소
	    	.header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))//토스에서 개인식별(시크릿키)
	    	.header("Content-Type", "application/json")//json형식 사용
	    	.method("POST", HttpRequest.BodyPublishers.ofString(""))// JSON -> String
	    	.build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			
    		if(response.statusCode() == 200) {//요청응답코드 200=성공
    			enrollService.deletionInfo(subMallId);
    			model.addAttribute("msg","SUCCess");//alert 사용
    			return "/pay/loo/lookup";
    		}else {
    			//심플JSON 사용
        		JSONParser parser = new JSONParser();
        		Object obj = parser.parse(response.body());
        		JSONObject jsonObj = (JSONObject)obj;
    			String message = (String)jsonObj.get("message");//토스에서 실패한 코드를 String 변경
    			model.addAttribute("message",message);//실패사유
    			model.addAttribute("msg","FAIL");//alert 사용
    			return "/pay/loo/lookup";
    		}
	}
	
}
