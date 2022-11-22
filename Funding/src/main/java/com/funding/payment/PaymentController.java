package com.funding.payment;



import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    private String paymentKey;
    private String cancelReason;

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
    
    
    
    @RequestMapping("/tossPay")
    public String tossPay() {
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
        System.out.println("&&&&&&&&&"+responseEntity+"&&&&&&&&&");
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JsonNode successNode = responseEntity.getBody();
            model.addAttribute("orderId", successNode.get("orderId").asText());
            String s = successNode.get("orderName").asText();
            String ss = successNode.get("transactionKey").asText();
            patmentService.savecreditinfo(paymentKey, orderId, amount, s, ss);
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
    @RequestMapping("/lookupSuccess")
    public String lookupSuccess(@RequestParam("paymentKey")String paymentKey) throws Exception  {
    	HttpRequest request = HttpRequest.newBuilder()
    			.uri(URI.create("https://api.tosspayments.com/v1/payments/"+paymentKey))
    		    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))
    		    .method("GET", HttpRequest.BodyPublishers.noBody())
    		    .build();
    		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    		System.out.println(response.body());
        	
    		if(response.statusCode() == 200) {
    			return "redirect:/pay/lookupSuccess";
    		}else {
    			return "redirect:/pay/lookupFail";
    		}
        //return "pay/lookupSuccess";
    }
    //조회실패
    @RequestMapping("/lookupFail")
    public String lookupFail( String message, String code, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "pay/lookupFail";
    }
    
    
    
    
    
    
   
    
    

    //환불하기
    @RequestMapping("/cancel")
    public String cancel(String paymentKey, String cancelReason) throws Exception{
    	HttpRequest request = HttpRequest.newBuilder()
		    .uri(URI.create("https://api.tosspayments.com/v1/payments/"+paymentKey+"/cancel"))
		    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))
		    .header("Content-Type", "application/json")
		    .method("POST", HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"" + cancelReason + "\"}"))
		    .build();
		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    		System.out.println(response.body());
    	
		if(response.statusCode() == 200) {
			return "redirect:/pay/cancelSuccess";
		}else {
			return "redirect:/pay/cancelFail";
		}
    }

    //환불성공
    @RequestMapping("/cancelSuccess")
    public String cancelSuccess(){
			return "/pay/cancelSuccess";
    }
    //환불실패
    @RequestMapping("/cancelFail")
    public String cancelFail( String message, String code, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "pay/cancelFail";
    }
}