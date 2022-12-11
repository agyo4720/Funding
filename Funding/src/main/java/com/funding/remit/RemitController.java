package com.funding.remit;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
		Calendar cal = Calendar.getInstance();//달력클래스
        cal.setTime(new Date());//지금시간을 가져옴
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");//표시할 DATE형식

        cal.add(Calendar.DATE, +3);//지금 날짜에서 +3일
        String nowTime = df.format(cal.getTime()); //오늘로 부터 3일후 부터 클릭가능
        model.addAttribute("nowTime", nowTime);
			return "/pay/rem/remit";
	}
	@RequestMapping("/rem/remitRquest")
	public String remitRquest(@RequestParam("subMallId")String subMallId, @RequestParam("payoutAmount")Integer payoutAmount,
			@RequestParam("payoutDate")String payoutDate,Model model) throws Exception {
		HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create("https://api.tosspayments.com/v1/payouts/sub-malls/settlements"))//토스에서 요구하는 주소
    		    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))//토스에서 개인식별(시크릿키)
    		    .header("Content-Type", "application/json")//json형식 사용
			    .method("POST", HttpRequest.BodyPublishers.ofString("[{\"subMallId\":\""+subMallId+"\",\"payoutAmount\":"+payoutAmount+","
			    		+ "\"payoutDate\":\""+payoutDate+"\"},{\"subMallId\":\""+subMallId+"\","
			    		+ "\"payoutAmount\":"+payoutAmount+",\"payoutDate\":\""+payoutDate+"\"}]"))//필수입력 값
			    .build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());// JSON -> String			

    		if(response.statusCode() == 200) {//요청응답코드 200=성공
    			remitService.remitInfo(subMallId, payoutAmount, payoutDate);
    			model.addAttribute("msg","Success");//alert 사용
    			return "/pay/loo/lookup";
    		}else {
    			//심플JSON 사용
        		JSONParser parser = new JSONParser();
        		Object obj = parser.parse(response.body());
        		JSONObject jsonObj = (JSONObject)obj;
    			String message = (String)jsonObj.get("message");//토스에서 실패한 코드를 String 변경
    			model.addAttribute("message",message);//실패사유
    			model.addAttribute("msg","Fail");//alert 사용
    			return "/pay/loo/lookup";
    		}
	}

	
	//계좌관리, 계좌조회
	@RequestMapping("/loo/lookup")
	public String lookup(String subMallId, Model model,
			@RequestParam(value = "page", defaultValue="0") int page)throws Exception {
		Page<Remit> rList = remitService.findBysubMallId(page,subMallId);//페이징(고유ID 일치)
		model.addAttribute("rList",rList);
		model.addAttribute("page",page);
		model.addAttribute("subMallId",subMallId);
		return "pay/loo/lookup";
	}
}
