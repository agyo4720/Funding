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


    @RequestMapping("/pay/tossPay")
    private String tossPay() {
    	return "/pay/tossPay";
    }
    
    @RequestMapping("/pay/cancel")
    private String cancel() {
    	return "/pay/cancel";
    }
    
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

    private final String SECRET_KEY = "test_sk_JQbgMGZzorzl7aMN4D3l5E1em4dK";

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
        
        System.out.println("@@@@@@@"+request+"@@@@@@");

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);
        System.out.println("&&&&&&&&&"+responseEntity+"&&&&&&&&&");
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JsonNode successNode = responseEntity.getBody();
            //log.info(successNode.toString());
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
    
    
    @RequestMapping("/pay/lookup")
    public void lookup() throws Exception {
    	HttpRequest request = HttpRequest.newBuilder()
    		    .uri(URI.create("https://api.tosspayments.com/v1/payments/iuL61eBd-qgYmW-wKZdZ5"))
    		    .header("Authorization", "Basic dGVzdF9za196WExrS0V5cE5BcldtbzUwblgzbG1lYXhZRzVSOg==")
    		    .method("GET", HttpRequest.BodyPublishers.noBody())
    		    .build();
    		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    		System.out.println(response.body());
        log.info("%%%%%%%%%%%%%%%%%%%%%%"+response+"%%%%%%%%%%%%%%%%");

    }
    
    
    
    
    //환불성공
    @RequestMapping("/pay/cancelSuccess")
    public String cancel(@RequestParam String paymentKey, @RequestParam String cancelReason, 
    		@RequestParam String requesterType, Model model) throws Exception {
        
    	HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        System.out.println("$$$$$$"+headers+"$$$$$$$");
        
        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("paymentKey", paymentKey);
        payloadMap.put("cancelReason", cancelReason);
        payloadMap.put("requesterType", requesterType);
        
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);
        
        System.out.println("$$$$$$"+request+"$$$$$$$");
        
        ResponseEntity<JsonNode> responseEntity = restTemplate.getForEntity(
                "https://api.tosspayments.com/v1/payments" + request, JsonNode.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JsonNode successNode = responseEntity.getBody();
            return "/pay/cancelSuccess";
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "/pay/cancelFail";
        }
    }

    /*
    //환불실패
    @RequestMapping("/pay/fail")
    public String failPayment(@RequestParam String message, @RequestParam String code, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "/pay/fail";
    }
    */

}