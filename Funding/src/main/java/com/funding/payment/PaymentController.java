package com.funding.payment;



import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.Principal;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.funding.fundBoardTarget.FundBoardTarget;
import com.funding.fundBoardTarget.FundTargetService;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/pay")
@Controller
public class PaymentController {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PatmentService patmentService;
    private final String SECRET_KEY = "test_sk_JQbgMGZzorzl7aMN4D3l5E1em4dK";
    private final FundTargetService fundTargetService;
    private final FundUserRepository fundUserRepository;
    private final CancelsRepository cancelsRepository;
    private final SaleRepository saleRepository;
    
    @PostConstruct
    private void init() {
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }
            @Override
            public void handleError(ClientHttpResponse response) {
            }
        });
    }
    
    
	//결제목록
	@GetMapping("/confirm")
	public String confirm(Principal principal, Model model) throws Exception{
		principal.getName();
		Optional<FundUser> FU =  fundUserRepository.findByusername(principal.getName());
		
		//결제리스트 불러오기
		List<Sale> sList = saleRepository.findByFundUser(FU.get());
		model.addAttribute("sList",sList);
		
		//환불리스트 불러오기
		List<Cancels> cList = cancelsRepository.findByFundUser(FU.get());
		model.addAttribute("sList",sList);
		return "confirm";
	}
    

    @RequestMapping("/tossPay/{id}")
    public String tossPay(Principal principal, Model model, @PathVariable("id")Integer id) {
		FundBoardTarget fundBoardTarget = fundTargetService.findById(id);
		model.addAttribute("fundBoardTarget", fundBoardTarget);
		
		
		Optional<FundUser> FU = this.fundUserRepository.findByusername(principal.getName());
		model.addAttribute("userData",FU.get());
    	return "pay/tossPay";
    }
    //결제성공
    @RequestMapping("/success")
    public String confirmPayment(
            @RequestParam String paymentKey, @RequestParam String orderId, @RequestParam int amount,
            Model model) throws Exception {
    	
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
        payloadMap.put("amount", String.valueOf(amount));
        
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JsonNode successNode = responseEntity.getBody();
            model.addAttribute("balanceAmount", successNode.get("balanceAmount").asText());
            model.addAttribute("orderName", successNode.get("orderName").asText());
            model.addAttribute("orderId", successNode.get("orderId").asText());
            
            String s = successNode.get("orderName").asText();
            String ss = successNode.get("status").asText();
            patmentService.targetSaveinfo(paymentKey, orderId, amount, s, ss);
            return "/pay/success";
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "pay/fail";
        }
    }
    
    
    
    //조회하기
    @RequestMapping("/lookup")
    public String lookup() {
    	return "pay/lookup";
    }
    //조회성공
    @RequestMapping("/lookupRquest")
    public String lookupRquest(@RequestParam("orderId")String orderId,Model model) throws Exception  {
    	HttpRequest request = HttpRequest.newBuilder()
    			.uri(URI.create("https://api.tosspayments.com/v1/payments/orders/"+orderId))
    		    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))
    		    .method("GET", HttpRequest.BodyPublishers.noBody())
    		    .build();
    		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    		log.info("!!!!!!!!!"+response.body()+"!!!!!!!!");

    		//문자열을 json형태 변환
    		JSONParser parser = new JSONParser();
    		Object obj = parser.parse(response.body());
    		JSONObject jsonObj = (JSONObject)obj;

    		if(response.statusCode() == 200) {//요청응답코드 200=성공
    			String orderName = (String)jsonObj.get("orderName");//상품명
    			String status = (String)jsonObj.get("status");//상태
    			String totalAmount = jsonObj.get("totalAmount").toString();//금액
    			
    			model.addAttribute("orderName",orderName);
    			model.addAttribute("orderId",orderId);
    			model.addAttribute("amount",totalAmount);
    			model.addAttribute("status",status);
    			return "/pay/lookupSuccess";
    		}else {
    			String message = (String)jsonObj.get("message");
    			String code = (String)jsonObj.get("code");
    			model.addAttribute("message",message);
    			model.addAttribute("code",code);
    			return "/pay/lookupFail";
    		}
    }
    
    
    
    //환불하기
    @RequestMapping("/cancel")
    public String cancel() {
    	return "/pay/cancel";
    }
    //환불성공
    @RequestMapping("/cancelRquest")
    public String cancelRquest(@RequestParam("paymentKey")String paymentKey, 
    		@RequestParam("cancelReason")String cancelReason, Model model)throws Exception{
    		HttpRequest request = HttpRequest.newBuilder()
    		    .uri(URI.create("https://api.tosspayments.com/v1/payments/"+paymentKey+"/cancel"))
    		    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))
    		    .header("Content-Type", "application/json")
    		    .method("POST", HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"" + cancelReason + "\"}"))
    		    .build();
    		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    		
    		JSONParser parser = new JSONParser();
    		Object obj = parser.parse(response.body());
    		log.info("^^^^^^^^^^^^^"+response.body()+"^^^^^^^^");
    		JSONObject jsonObj = (JSONObject)obj;
        	
    		if(response.statusCode() == 200) {
    			String orderName = (String)jsonObj.get("orderName");//상품명
    			String orderId = (String)jsonObj.get("orderId");//주문번호
    			String totalAmount = jsonObj.get("totalAmount").toString();//금액

    			model.addAttribute("orderName",orderName);
    			model.addAttribute("orderId",orderId);
    			model.addAttribute("totalAmount",totalAmount);
    			model.addAttribute("cancelReason",cancelReason);
    			return "/pay/cancelSuccess";
    		}else {
    			String message = (String)jsonObj.get("message");
    			String code = (String)jsonObj.get("code");
    			model.addAttribute("message",message);
    			model.addAttribute("code",code);
    			return "/pay/cancelFail";
    		}
    }
}