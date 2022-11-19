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
@Controller
public class PaymentController {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PatmentService patmentService;
    private final String SECRET_KEY = "test_sk_JQbgMGZzorzl7aMN4D3l5E1em4dK";

    
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
    
    
    @RequestMapping("/pay/tossPay")
    public String tossPay() {
    	return "/pay/tossPay";
    }
    //결제성공
    @RequestMapping("/pay/success")
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
            return "/pay/fail";
        }
    }
    //결제실패
    @RequestMapping("/pay/fail")
    public String failPayment(@RequestParam String message, @RequestParam String code, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "/pay/fail";
    }
    
    
    
    //조회하기
    @GetMapping("/pay/lookup")
    public String lookup() throws Exception {
    	HttpRequest request = HttpRequest.newBuilder()
    		    .uri(URI.create("https://api.tosspayments.com/v1/payments/R1kZn04DxKBE92LAa5PVb591AaYzP37YmpXyJjg6OwzoeqdW"))
    		    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))
    		    .method("GET", HttpRequest.BodyPublishers.noBody())
    		    .build();
    		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    		System.out.println(response.body());
    		
        log.info("%%%%%%%%%%%%%%%%%%%%%%"+response+"%%%%%%%%%%%%%%%%");
            return "/pay/lookup";
    }
    //조회성공
    @RequestMapping("/pay/lookupSuccess")
    public String lookupSuccess() {
    	return "/pay/lookupSuccess";
    }
   
    
    //환불하기
    @RequestMapping("/pay/cancel")
    public String cancel (
    		@RequestParam String paymentKey,
    		@RequestParam String cancelReason) throws Exception {
    	paymentKey = "R1kZn04DxKBE92LAa5PVb591AaYzP37YmpXyJjg6OwzoeqdW";
    	cancelReason = "걍";
    		HttpRequest request = HttpRequest.newBuilder()
    		    .uri(URI.create("https://api.tosspayments.com/v1/payments/"+paymentKey+"/cancel"))
    		    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))
    		    .header("Content-Type", "application/json")
    		    .method("POST", HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"" + cancelReason + "\"}"))
    		    .build();
    		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    		System.out.println(response.body());
    		log.info("%%%%%%%%%%%%%%%%%%%%%%"+response+"%%%%%%%%%%%%%%%%");
    		
    	/*
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
            return "/pay/fail";
        }
    	*/
    		
    	return "/pay/cancel";
    }
    //환불성공
    @RequestMapping("/pay/cancelSuccess")
    public String cancelSuccess()  {
			return "/pay/cancelSuccess";
    }
    
    //환불실패
    @RequestMapping("/pay/cancelFail")
    public String cancelFail( String message, String code, Model model) {
    	//model.addAttribute("message", "이미취소");
    	System.out.println(message);
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "/pay/cancelFail";
    }
    

}