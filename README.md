# Funding

### 사용기술
> <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white"/><img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=JavaScript&logoColor=white"/><img src="https://img.shields.io/badge/JSON-000000?style=flat&logo=JSON&logoColor=white"/><img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat&logo=Spring Boot&logoColor=white"/><img src="https://img.shields.io/badge/jQuery-0769AD?style=flat&logo=jQuery&logoColor=white"/><img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat&logo=Spring Security&logoColor=white"/>  
<img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=flat&logo=Thymeleaf&logoColor=white"/><img src="https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=HTML5&logoColor=white"/><img src="https://img.shields.io/badge/CSS3-1572B6?style=flat&logo=CSS3&logoColor=white"/><img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=GitHub&logoColor=white"/>


---

## 목차  
1.  프로젝트 버전
2.  왜 이 프로젝트를 기획했나요?
2.  주요기능
3.  프로젝트 사용 방법
4.  팀원 및 참고 자료

---

### 1. 프로젝트 버전
* spring-boot : 2.7.5
* java : 17
* mysql : 8.0.29  
* lombok : 1.18.24

### 2. 왜 이 프로젝트를 기획했나요?
> 공연은 항상 주최자들이 열었고 그 티캣을 소비자가 구매하는 방향이었습니다.
> 본 프로젝트에서 그 소비방향에 변화를 주고 싶었습니다.
> 소비자가 보고 싶은 공연에 대해 제안하고, 펀딩 시스템으로 공연 개최 및 여러 아티스트들이 참여할 수 있는 시스템을 기획하게 되었습니다.

### 2. 주요기능
* 회원가입 로그인 기능 (User, Artist 2가지 타입이 있음)
* 유저는 지정 펀딩과 미지정 펀딩 2가지의 방식으로 펀딩 글을 작성
  * 지정 펀딩은 특정 아티스트에 의한 공연이 개최됨
  * 미지정 펀딩은 현재 공연에 참여하고자 하는 아티스트 중 유저들의 투표로 선발되어 공연이 개최됨
* 펀딩을 위한 결제, 취소, 환불 등의 기능이 있음
* 알림 (댓글작성, 펀딩 달성, 마감, 취소 등의 상황에서 알림이 발송됨)
* 유저, 아티스트의 개인 페이지(아티스트는 자신의 소개 페이지를 작성함)

### 3. 프로젝트 사용 방법
* #### 이 프로젝트의 메인 페이지 입니다.  
<img width="70%" src="https://user-images.githubusercontent.com/107646818/207565645-addaa089-d660-4a3e-ac46-dbd389954102.png"/>

* #### 로그인  
<img width="40%" src="https://user-images.githubusercontent.com/110438208/207574355-96f4c325-bc92-47f6-8242-eda863fb4c8e.png"/>

* #### 회원가입  
<img width="50%" height="800" src="https://user-images.githubusercontent.com/107646818/207574982-93d6fc6d-842b-4097-9c40-0098f90d382e.png"/>

* #### 지정펀딩 클릭시  
<img width="70%" src="https://user-images.githubusercontent.com/107646818/207566652-c68a5910-f2f8-4e2a-b1f2-d22b65312825.png"/>

* #### 지정펀딩 클릭하고 카테고리  
<img width="70%" src="https://user-images.githubusercontent.com/107646818/207566803-2a331b46-0661-43d2-9e12-c07fedfbec60.png"/>

* #### 펀딩글 작성화면
<img width="70%" src="https://user-images.githubusercontent.com/110438208/207573145-4b75f077-18fa-432d-a445-b3b4106624c3.png"/>

* #### 펀딩전 디테일화면  
<img width="70%" src="https://user-images.githubusercontent.com/107646818/207567018-2578843f-5ba6-4d16-a96c-09aa7cb406bd.png"/>

* #### 펀딩후 디테일화면  
<img width="70%" src="https://user-images.githubusercontent.com/107646818/207567191-64358636-636c-474b-87d6-062eaac7e8af.png"/>

* #### 기간안에 펀딩 미달성시  
<img width="70%" src="https://user-images.githubusercontent.com/107646818/207567530-ad6dc69e-83a2-4399-a8f8-28e6b09f2ffe.png"/>

* #### 기간안에 펀딩 달성시  
<img width="70%" src="https://user-images.githubusercontent.com/107646818/207568412-af63cc65-f30f-44dd-80f5-7e3f5d265a3c.png"/>

* #### 결제방식  
<img width="80%" src="https://user-images.githubusercontent.com/107646818/207762578-16f44e07-ae00-49c6-8290-06f81bce3a60.png"/>  

* #### 결제 성공시  
<img width="80%" src="https://user-images.githubusercontent.com/107646818/207568979-90c7c83e-fc0c-41b6-941d-089462b6ef83.png"/>  

* #### 결제내역  
<img width="90%" src="https://user-images.githubusercontent.com/107646818/207569086-6409a480-1b2b-44a6-aba3-54b0e39b6296.png"/>  

* #### 유저페이지  
<img width="80%" src="https://user-images.githubusercontent.com/107646818/207569648-da625a67-bc16-4a9b-9d6f-78aaf91b96d9.png"/>  

* #### 유저 정보변경  
<img width="80%" src="https://user-images.githubusercontent.com/107646818/207569781-c34e3225-ba5d-4f91-b882-053ea52a4c90.png"/>  

* #### 유저 댓글  
<img width="80%" src="https://user-images.githubusercontent.com/107646818/207570075-46584221-67d4-4694-933e-b4ceaf261f9c.png"/>  

* #### 미지정펀딩 클릭시  
<img width="80%" src="https://user-images.githubusercontent.com/107646818/207568686-e5ff9afe-a92f-4e87-a552-250a27602603.png"/>  

* #### 아티스트 페이지  
<img width="80%" src="https://user-images.githubusercontent.com/107646818/207570257-15119784-06d7-486b-bc2e-98cbd97aa757.png"/>  

* #### 아티스트 소개 작성 전  
<img width="80%" src="https://user-images.githubusercontent.com/107646818/207570336-33d454b0-9670-4eea-b29a-ef8d220f554c.png"/>  

* #### 아티스트 소개 작성 후  
<img width="80%" src="https://user-images.githubusercontent.com/107646818/207570503-7c8a0414-f392-4424-ad6f-5a494c2f9a2a.png"/>  

* #### 계좌관리  
<img width="80%" src="https://user-images.githubusercontent.com/107646818/207570661-d6ffe36d-0b1c-4162-8356-b6668e66dc0a.png"/>  

* #### 미지정 아티스트 투표  
<img width="80%" src="https://user-images.githubusercontent.com/107646818/207570916-70ebe7e5-d003-4fa9-8d9a-ff35d20968b0.png"/>  


### 4. 팀원 및 참고 자료
* [하은교(팀장)](https://github.com/agyo4720)
  * [Daum 우편번호 서비스](https://postcode.map.daum.net/guide)
* [박남규](https://github.com/namgooo)  
* [박용진](https://github.com/consr2)
  * [Summernote](https://summernote.org/)
* [백진용](https://github.com/qor8005)  
  * [토스페이먼츠 개발자센터](https://docs.tosspayments.com/reference)  
* [이동헌](https://github.com/startyuphoney)  

