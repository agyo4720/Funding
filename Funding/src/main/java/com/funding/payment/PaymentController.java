package com.funding.payment;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.Principal;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.data.domain.Page;
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
import com.funding.Categorie.Categorie;
import com.funding.fundBoard.FundBoard;
import com.funding.fundBoard.FundBoardService;
import com.funding.fundBoardTarget.FundBoardTarget;
import com.funding.fundBoardTarget.FundTargetService;
import com.funding.fundTargetList.FundTargetListService;
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
    private final FundBoardService fundBoardService;
    private final FundUserRepository fundUserRepository;
    private final CancelsRepository cancelsRepository;
    private final SaleRepository saleRepository;
    private final FundTargetListService fundTargetListService;
    private final RemitRepository remitRepository;
    private String paymentKey;
    
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

    
	//지정 결제
    @RequestMapping("/tossPayTar/{id}")
    public String tossPayTar(Principal principal, Model model, @PathVariable("id")Integer id) {
		FundBoardTarget fundBoardTarget = fundTargetService.findById(id);
		model.addAttribute("fundBoardTarget", fundBoardTarget);//지정공연 번호

		Optional<FundUser> FU = this.fundUserRepository.findByusername(principal.getName());
		model.addAttribute("userData",FU.get());//로그인중인 정보
    	return "pay/tossPayTar";
    }
    //지정 결제성공
    @RequestMapping("/success")
    public String confirmPayment(
            @RequestParam String paymentKey, @RequestParam String orderId, @RequestParam int amount,
            Model model, Principal principal) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
        payloadMap.put("amount", String.valueOf(amount));
        
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        Optional<FundUser> FU = this.fundUserRepository.findByusername(principal.getName());//로그인중인 정보
        
        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);
        log.info("responseEntity: "+responseEntity);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JsonNode successNode = responseEntity.getBody();
            model.addAttribute("status", successNode.get("status").asText());//상태
            model.addAttribute("balanceAmount", successNode.get("balanceAmount").asText());//금액
            model.addAttribute("orderName", successNode.get("orderName").asText());//공연이름
            model.addAttribute("orderId", successNode.get("orderId").asText());//주문번호
            
            String orderName = successNode.get("orderName").asText();
        	patmentService.targetSaveinfo(paymentKey, orderId, amount, orderName, FU);
        	
        	//누적금액증가, 사람 수 증가
        	String tar = successNode.get("orderId").toString();
        	String target = tar.substring(tar.lastIndexOf('-')+1);
        	target = target.replace("\"", "");
        	log.info("target: "+target);	
        	
        	FundBoardTarget targetPk = fundTargetService.findById(Integer.parseInt(target));
        	Integer add = targetPk.getFundCurrent();
        	add += amount;
        	targetPk.setFundCurrent(add);
        	Integer cMem = targetPk.getCurrentMember();
        	cMem++;
        	targetPk.setCurrentMember(cMem);
        	fundTargetService.addTargetFund(targetPk);
        	
        	//유저의 현재 펀딩 목록 추가
        	fundTargetListService.insertList(principal, targetPk);
        	
            return "/pay/success";
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "pay/fail";
        }
    }
    
    
    
    
    //미지정 결제
    @RequestMapping("/tossPay/{id}")
    public String tossPay(Principal principal, Model model, @PathVariable("id")Integer id) {
		FundBoard fundBoard = fundBoardService.findById(id);
		model.addAttribute("fundBoard", fundBoard);//미지정공연 번호
		log.info("fundBoard: "+fundBoard);

		Optional<FundUser> FU = this.fundUserRepository.findByusername(principal.getName());//로그인중인 정보
		model.addAttribute("userData",FU.get());//로그인정보
    	return "pay/tossPay";
    }
    //미지정 결제성공
    @RequestMapping("/success1")
    public String confirmPayment1(
            @RequestParam String paymentKey, @RequestParam String orderId, @RequestParam int amount,
            Model model, Principal principal) throws Exception {
    	
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
        payloadMap.put("amount", String.valueOf(amount));
        
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        Optional<FundUser> FU = this.fundUserRepository.findByusername(principal.getName());//로그인중인 정보
        
        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JsonNode successNode = responseEntity.getBody();
            model.addAttribute("balanceAmount", successNode.get("balanceAmount").asText());//금액
            model.addAttribute("orderName", successNode.get("orderName").asText());//공연이름
            model.addAttribute("orderId", successNode.get("orderId").asText());//주문번호
            model.addAttribute("status", successNode.get("status").asText());//상태
            
            String orderName = successNode.get("orderName").asText();
        	patmentService.saveinfo(paymentKey, orderId, amount, orderName, FU);
        	
        	//누적금액증가
        	String tar = successNode.get("orderId").toString();
        	String target = tar.substring(tar.lastIndexOf('-')+1);
        	target = target.replace("\"", "");
        	log.info("target: "+target);	
        	
        	FundBoard fundBoard = fundBoardService.findById(Integer.parseInt(target));
        	Integer add = fundBoard.getFundCurrent();
        	add += amount;
        	fundBoard.setFundCurrent(add);
        	fundBoardService.addFundBoard(fundBoard);
        	
        	
            return "/pay/success1";
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "pay/fail1";
        }
    }

    
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
    			patmentService.tarCancelInfo(orderId, Integer.valueOf(totalAmount).intValue(), orderName, cancelReason, FU, paymentKey);

    			
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
    			patmentService.cancelInfo(orderId, Integer.valueOf(totalAmount).intValue(), orderName, cancelReason, FU,paymentKey);
    			
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
            	
            	Integer cMem = fundBoard.getCurrentMember();
            	cMem--;
            	fundBoard.setCurrentMember(cMem);
            	fundBoardService.addFundBoard(fundBoard);

    			return "/pay/can/cancelSuccess";
    		}else {
    			String message = (String)jsonObj.get("message");
    			String code = (String)jsonObj.get("code");
    			model.addAttribute("message",message);
    			model.addAttribute("code",code);
    			return "/pay/can/cancelFail";
    		}
    }

	//결제목록
	@GetMapping("/loo/confirm")
	public String confirm(Principal principal, Model model,@RequestParam(value = "page", defaultValue="0") int page,
			@RequestParam(value = "pagee", defaultValue="0") int pagee) throws Exception{
		principal.getName();
		Optional<FundUser> FU =  fundUserRepository.findByusername(principal.getName());
		
		//결제리스트 불러오기
		Page<Sale> sList = patmentService.findByFundUser(page,FU.get().getNickname());
		model.addAttribute("sList",sList);
		model.addAttribute("page",page);

		//환불리스트 불러오기
		Page<Cancels> cList = patmentService.findByCan(pagee,FU.get().getNickname());
		model.addAttribute("cList",cList);
		model.addAttribute("pagee",pagee);
		return "/pay/loo/confirm";
	}
	
	
	
	
	
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
			    .uri(URI.create("https://api.tosspayments.com/v1/payouts/sub-malls"))
    		    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))
    		    .header("Content-Type", "application/json")
			    .method("POST", HttpRequest.BodyPublishers.ofString("{\"subMallId\":\""+subMallId+"\","
			    		+ "\"type\":\"CORPORATE\",\"companyName\":\""+companyName+"\",\"representativeName\":\""+representativeName+"\","
			    		+ "\"businessNumber\":\""+businessNumber+"\",\"account\":{\"bank\":\""+bank+"\",\"accountNumber\":\""+accountNumber+"\"}}"))
			    .build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.body());
    		if(response.statusCode() == 200) {//요청응답코드 200=성공
    			patmentService.enrollInfo(subMallId, companyName, representativeName, businessNumber, bank, accountNumber);
    			return "/pay/rem/enrollSuccess";
    		}else {
    			return "/pay/rem/enrollFail";
    		}
	}
	
	//계좌수정
	@RequestMapping("/rem/revise")
	public String revise(){
			return "/pay/rem/revise";
	}
	@RequestMapping("/rem/reviseRequest")
	public String reviseRequest(@RequestParam("subMallId")String subMallId,@RequestParam("bank")String bank,@RequestParam("companyName")String companyName,
			@RequestParam("representativeName")String representativeName,@RequestParam("businessNumber")String businessNumber,@RequestParam("accountNumber")String accountNumber)throws Exception {
		HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create("https://api.tosspayments.com/v1/payouts/sub-malls/"+subMallId))
    		    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))
    		    .header("Content-Type", "application/json")
			    .method("POST", HttpRequest.BodyPublishers.ofString("{\"subMallId\":\""+subMallId+"\","
			    		+ "\"type\":\"CORPORATE\",\"companyName\":\""+companyName+"\",\"representativeName\":\""+representativeName+"\","
			    		+ "\"businessNumber\":\""+businessNumber+"\",\"account\":{\"bank\":\""+bank+"\",\"accountNumber\":\""+accountNumber+"\"}}"))
			    .build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.body());
    		if(response.statusCode() == 200) {//요청응답코드 200=성공
    			patmentService.reviseInfo(subMallId, companyName, representativeName, businessNumber, bank, accountNumber);
    			return "/pay/rem/reviseSuccess";
    		}else {
    			return "/pay/rem/reviseFail";
    		}
	}
	
	
	//계좌삭제
	@RequestMapping("/rem/deletion")
	public String deletion(){
			return "/pay/rem/deletion";
	}
	@RequestMapping("/rem/deletionRequest")
	public String deletionRequest(@RequestParam("subMallId")String subMallId)throws Exception {
			HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("https://api.tosspayments.com/v1/payouts/sub-malls/"+subMallId+"/delete"))
	    	.header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))
	    	.header("Content-Type", "application/json")
	    	.method("POST", HttpRequest.BodyPublishers.ofString(""))
	    	.build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.body());
    		if(response.statusCode() == 200) {//요청응답코드 200=성공
    			patmentService.deletionInfo(subMallId);
    			return "/pay/rem/deletionSuccess";
    		}else {
    			return "/pay/rem/deletionFail";
    		}
	}

	//송금하기
	@RequestMapping("/rem/remit")
	public String remit() {
			return "/pay/rem/remit";
	}
	@RequestMapping("/rem/remitRquest")
	public String remitRquest(@RequestParam("subMallId")String subMallId, @RequestParam("payoutAmount")Integer payoutAmount,
			@RequestParam("payoutDate")String payoutDate) throws Exception {
		HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create("https://api.tosspayments.com/v1/payouts/sub-malls/settlements"))
    		    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))
    		    .header("Content-Type", "application/json")
			    .method("POST", HttpRequest.BodyPublishers.ofString("[{\"subMallId\":\""+subMallId+"\",\"payoutAmount\":"+payoutAmount+","
			    		+ "\"payoutDate\":\""+payoutDate+"\"},{\"subMallId\":\""+subMallId+"\","
			    		+ "\"payoutAmount\":"+payoutAmount+",\"payoutDate\":\""+payoutDate+"\"}]"))
			    .build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.body());
    		if(response.statusCode() == 200) {//요청응답코드 200=성공
    			patmentService.remitInfo(subMallId, payoutAmount, payoutDate);
    			return "/pay/rem/remitSuccess";
    		}else {
    			return "/pay/rem/remitFail";
    		}
	}
	
	//계좌조회
	@RequestMapping("/rem/confirm")
	public String confirm(){
			return "/pay/rem/confirm";
	}
	@RequestMapping("/rem/confirmRequest")
	public String confirmRequest(@RequestParam("subMallId")String subMallId, Model model,
			@RequestParam(value = "page", defaultValue="0") int page)throws Exception {
		Page<Remit> rList = patmentService.findBysubMallId(page,subMallId);
		model.addAttribute("rList",rList);
		model.addAttribute("page",page);
		log.info("!!rList: "+rList);
    		if(rList != null) {
    			model.addAttribute("rList",rList);
    			return "/pay/rem/confirmSuccess";
    		}else {
    			return "/pay/rem/confirmFail";
    		}
	}
}