package com.funding.remit;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import org.springframework.data.domain.Page;
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
public class RemitController {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String SECRET_KEY = "test_sk_JQbgMGZzorzl7aMN4D3l5E1em4dK";
    private final RemitService remitService;
    
	//송금하기
	@RequestMapping("/rem/remit")
	public String remit(Model model) {
		String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		model.addAttribute("nowTime", nowTime);
			return "/pay/rem/remit";
	}
	@RequestMapping("/rem/remitRquest")
	public String remitRquest(@RequestParam("subMallId")String subMallId, @RequestParam("payoutAmount")Integer payoutAmount,
			@RequestParam("payoutDate")String payoutDate,Model model) throws Exception {
		HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create("https://api.tosspayments.com/v1/payouts/sub-malls/settlements"))
    		    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))
    		    .header("Content-Type", "application/json")
			    .method("POST", HttpRequest.BodyPublishers.ofString("[{\"subMallId\":\""+subMallId+"\",\"payoutAmount\":"+payoutAmount+","
			    		+ "\"payoutDate\":\""+payoutDate+"\"},{\"subMallId\":\""+subMallId+"\","
			    		+ "\"payoutAmount\":"+payoutAmount+",\"payoutDate\":\""+payoutDate+"\"}]"))
			    .build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
						
    		if(response.statusCode() == 200) {//요청응답코드 200=성공
    			remitService.remitInfo(subMallId, payoutAmount, payoutDate);
    			model.addAttribute("msg","Success");
    			return "/main/nav";
    		}else {
    			model.addAttribute("msg","Fail");
    			return "/main/nav";
    		}
	}

	//계좌조회
	@RequestMapping("/rem/confirm")
	public String confirm(Model model){
		model.addAttribute("msg","진행중");
			return "/pay/rem/confirm";
	}
	@RequestMapping("/rem/confirmRequest")
	public String confirmRequest(@RequestParam("subMallId")String subMallId, Model model,
			@RequestParam(value = "page", defaultValue="0") int page)throws Exception {
		Page<Remit> rList = remitService.findBysubMallId(page,subMallId);
    		if(rList != null) {
    			model.addAttribute("rList",rList);
    			model.addAttribute("page",page);
    		}
		return "/pay/rem/confirmSuccess";
	}
}
