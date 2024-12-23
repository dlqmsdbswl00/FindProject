package com.board.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.board.dtos.AccountDto;
import com.board.dtos.UserDto;
import com.board.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/banking")
public class BankingController {
   
   @Autowired
   UserService userService;

   @GetMapping("/main")
   public String main() {
      return "main";
   }
   
   @ResponseBody // 요청했던 페이지로 응답: return 값을 출력한다.(ajax로 요청했다면 ajax메서드로 값을 전달)
   @GetMapping("/myinfo")
   public JSONObject myInfo(HttpServletRequest request) throws IOException, ParseException {
      System.out.println("나의정보조회[계좌목록]");

      HttpURLConnection conn=null;
      JSONObject result=null;
      
      //사용자 일련 번호를 가져오기 위해 session객체 구함
      HttpSession session=request.getSession();
      UserDto ldto=(UserDto)session.getAttribute("ldto");
      int userSeqNo=ldto.getUserseqno();//사용자 일련번호

      System.out.println(userSeqNo);

      String useraccesstoken=ldto.getUseraccesstoken();//접근할 토큰
//      System.out.println(useraccesstoken);
      
      //openbanking API 요청코드
      URL url=new URL("https://testapi.openbanking.or.kr/v2.0/user/me?"
                  + "user_seq_no="+userSeqNo);
      
      conn=(HttpURLConnection)url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Authorization","Bearer "+useraccesstoken);
      conn.setDoOutput(true);
      
      // java에서 사용할 수 있도록 읽어들이는 코드
      BufferedReader br=new BufferedReader(
               new InputStreamReader(conn.getInputStream(),"utf-8")
            );
      
      StringBuilder response=new StringBuilder();
      String responseLine=null;
      
      while((responseLine=br.readLine())!=null) {
         response.append(responseLine.trim());
      }
      
      // json형태의 문자열을 json객체로 변환 -> 값을 가져오기 편함
      result=(JSONObject)new JSONParser().parse(response.toString());
      System.out.println("result:"+result.get("res_list"));
      
      
      
      
      return result;
   }

//   @GetMapping("/account")
//   public String accountPage(HttpServletRequest request, Model model) throws IOException, ParseException {
//       HttpSession session = request.getSession();
//       String accessToken = (String) session.getAttribute("access_token");
//       String userSeqNo = (String) session.getAttribute("user_seq_no");
//
//       if (accessToken == null || userSeqNo == null) {
//           model.addAttribute("errorMessage", "로그인 상태를 확인해주세요.");
//           return "error";
//       }
//
//       HttpURLConnection conn = null;
//       try {
//           // API 요청
//           @SuppressWarnings("deprecation")
//      URL url = new URL("https://testapi.openbanking.or.kr/v2.0/account/list?user_seq_no=" + userSeqNo);
//           conn = (HttpURLConnection) url.openConnection();
//           conn.setRequestMethod("GET");
//           conn.setRequestProperty("Authorization", "Bearer " + accessToken);
//           conn.setRequestProperty("Content-Type", "application/json");
//
//           int statusCode = conn.getResponseCode();
//           System.out.println("HTTP Status Code: " + statusCode);
//           if (statusCode != 200) {
//               throw new IOException("API 요청 실패. 상태 코드: " + statusCode);
//           }
//
//           BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
//           StringBuilder response = new StringBuilder();
//           String responseLine;
//           while ((responseLine = br.readLine()) != null) {
//               response.append(responseLine.trim());
//           }
//           System.out.println("API Response: " + response.toString());
//
//           // JSON 데이터 파싱
//           JSONObject responseJson = (JSONObject) new JSONParser().parse(response.toString());
//           String rspCode = (String) responseJson.get("rsp_code");
//           if (!"A0000".equals(rspCode)) {
//               model.addAttribute("errorMessage", "계좌 정보를 가져올 수 없습니다.");
//               return "error";
//           }
//
//           JSONArray accountArray = (JSONArray) responseJson.get("account_list");
//           List<Map<String, String>> accountList = new ArrayList<>();
//           for (Object obj : accountArray) {
//               JSONObject account = (JSONObject) obj;
//               Map<String, String> accountMap = new HashMap<>();
//               accountMap.put("account_num", account.get("account_num").toString());
//               accountMap.put("account_name", account.get("account_name").toString());
//               accountMap.put("balance_amt", account.get("balance_amt").toString());
//               accountList.add(accountMap);
//           }
//
//           model.addAttribute("accounts", accountList);
//
//       } catch (IOException | ParseException e) {
//           e.printStackTrace();
//           model.addAttribute("errorMessage", "계좌 정보를 가져오는 중 오류가 발생했습니다.");
//           return "error";
//
//       } finally {
//           if (conn != null) {
//               conn.disconnect();
//           }
//       }
//
//       return "account";
//   }


//   @GetMapping("/user/authresult")
//   public String authResult(HttpServletRequest request, String code, String state, Model model) throws IOException, ParseException {
//       System.out.println("OAuth 인증 결과 처리 중...");
//       System.out.println("code: " + code);
//       System.out.println("state: " + state);
//
//       // 1. Access Token 요청
//       HttpURLConnection conn = null;
//       JSONObject result = null;
//
//       String clientId = "4987e938-f84b-4e23-b0a2-3b15b00f4ffd"; // 본인의 클라이언트 ID
//       String clientSecret = "3ff7570f-fdfb-4f9e-8f5a-7b549bf2adec"; // 본인의 클라이언트 Secret
//       String redirectUri = "http://localhost:8087/user/authresult";
//
//       @SuppressWarnings("deprecation")
//       URL url = new URL("https://testapi.openbanking.or.kr/oauth/2.0/token");
//
//       conn = (HttpURLConnection) url.openConnection();
//       conn.setRequestMethod("POST");
//       conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//       conn.setDoOutput(true);
//
//       String params = "code=" + code +
//                       "&client_id=" + clientId +
//                       "&client_secret=" + clientSecret +
//                       "&redirect_uri=" + redirectUri +
//                       "&grant_type=authorization_code";
//
//       conn.getOutputStream().write(params.getBytes());
//
//       BufferedReader br = new BufferedReader(
//               new InputStreamReader(conn.getInputStream(), "utf-8")
//       );
//
//       StringBuilder response = new StringBuilder();
//       String responseLine = null;
//
//       while ((responseLine = br.readLine()) != null) {
//           response.append(responseLine.trim());
//       }
//
//       // JSON 응답 처리
//       result = (JSONObject) new JSONParser().parse(response.toString());
//       System.out.println("Access Token 응답: " + result);
//
//       // 2. Access Token 및 User Seq No 저장
//       String accessToken = result.get("access_token").toString();
//       String userSeqNo = result.get("user_seq_no").toString();
//
//       // 3. 사용자 정보 세션에 저장
//       HttpSession session = request.getSession();
//       UserDto userDto = new UserDto();
//       userDto.setUseraccesstoken(accessToken);
//       userDto.setUserseqno(Integer.parseInt(userSeqNo));
//       session.setAttribute("ldto", userDto);
//
//       System.out.println("Access Token: " + accessToken);
//       System.out.println("User Seq No: " + userSeqNo);
//
//       // 메인 화면으로 리다이렉트
//       model.addAttribute("message", "인증 성공");
//       return "redirect:/banking/main";
//   }
   

   
   @ResponseBody
   @GetMapping("/balance")
   public JSONObject balance(String fintech_use_num,HttpServletRequest request) throws IOException, ParseException {
      System.out.println("잔액조회하기");
      HttpURLConnection conn=null;
      JSONObject result=null;

      
      HttpSession session=request.getSession();
      UserDto ldto=(UserDto)session.getAttribute("ldto");
      System.out.println(fintech_use_num);
      @SuppressWarnings("deprecation")
   URL url=new URL("https://testapi.openbanking.or.kr/v2.0/account/balance/fin_num?"
                  + "bank_tran_id=M202201886U"+createNum()
                  + "&fintech_use_num="+fintech_use_num
                  + "&tran_dtime="+getDateTime());
      
      conn = (HttpURLConnection)url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Authorization", "Bearer "+ldto.getUseraccesstoken());
      conn.setDoOutput(true);
      
      // java에서 사용할 수 있도록 읽어들이는 코드
      BufferedReader br=new BufferedReader(
               new InputStreamReader(conn.getInputStream(),"utf-8")
            );
      StringBuilder response=new StringBuilder();
      String responseLine=null;
      
      while((responseLine=br.readLine())!=null) {
         response.append(responseLine.trim());
      }
      
      result=(JSONObject)new JSONParser().parse(response.toString());
      String balance_amt=result.get("balance_amt").toString();
      
      return result;
   }
   
   @ResponseBody
   @GetMapping("/insertaccount")
   public JSONObject insertaccount(String fintech_use_num, String account_num_masked, HttpServletRequest request) throws IOException, ParseException {
      System.out.println("계좌등록");
      HttpURLConnection conn=null;
      JSONObject result=null;

      HttpSession session=request.getSession();
      UserDto ldto=(UserDto)session.getAttribute("ldto");
      System.out.println(account_num_masked);
      int userseqno = ldto.getUserseqno();
      HttpSession session1=request.getSession();
      AccountDto adto=(AccountDto)session1.getAttribute("adto");
      @SuppressWarnings("deprecation")
   URL url=new URL("https://testapi.openbanking.or.kr/v2.0/account/balance/fin_num?"
                  + "bank_tran_id=M202201886U"+createNum()
                  + "&fintech_use_num="+fintech_use_num
                  + "&tran_dtime="+getDateTime());
      
      conn = (HttpURLConnection)url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Authorization", "Bearer "+ldto.getUseraccesstoken());
      conn.setDoOutput(true);
      
      // java에서 사용할 수 있도록 읽어들이는 코드
      BufferedReader br=new BufferedReader(
               new InputStreamReader(conn.getInputStream(),"utf-8")
            );
      StringBuilder response=new StringBuilder();
      String responseLine=null;
      
      while((responseLine=br.readLine())!=null) {
         response.append(responseLine.trim());
      }
      
      result=(JSONObject)new JSONParser().parse(response.toString());
      String money=result.get("balance_amt").toString();
      String bank_name=result.get("bank_name").toString();
      userService.addAccount(money, fintech_use_num, bank_name, userseqno, account_num_masked);

      return result;
   }
   //거래내역 조회
   @GetMapping("/transactionList")
   @ResponseBody
   public JSONObject transactionList(String fintech_use_num
                                  ,HttpServletRequest request) throws IOException, ParseException {
      System.out.println("거래내역 조회하기");
      HttpURLConnection conn=null;
      JSONObject result=null;
      
      HttpSession session=request.getSession();
      UserDto ldto=(UserDto)session.getAttribute("ldto");
      
      @SuppressWarnings("deprecation")
   URL url=new URL("https://testapi.openbanking.or.kr/v2.0/account/transaction_list/fin_num?"
                  + "bank_tran_id=M202201886U"+createNum()
                  + "&fintech_use_num="+fintech_use_num
                  + "&inquiry_type=A"
                  + "&inquiry_base=D"
                  + "&from_date=20190101" 
                  + "&to_date=20190131"
                  + "&sort_order=D"
                  + "&tran_dtime="+getDateTime());
      
      conn = (HttpURLConnection)url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Authorization", "Bearer "+ldto.getUseraccesstoken());
      conn.setDoOutput(true);
      
      // java에서 사용할 수 있도록 읽어들이는 코드
      BufferedReader br=new BufferedReader(
               new InputStreamReader(conn.getInputStream(),"utf-8")
            );
      StringBuilder response=new StringBuilder();
      String responseLine=null;
      
      while((responseLine=br.readLine())!=null) {
         response.append(responseLine.trim());
      }
      
      //읽은 값이 json 형태로 된 문자열 --> json객체로 변환하자
      result=(JSONObject)new JSONParser().parse(response.toString());
      System.out.println("거래내역:"+result.get("res_list"));
      
      return result;
   }
   
   //계좌등록
   @ResponseBody
   @GetMapping("/addaccount")
   public String addAccount(String code) {
      System.out.println("계좌등록하기");
      System.out.println("code:"+code);
      //팝업창을 닫아 주기 위해서
      String str="<script type='text/javascript'>"
              +"     self.close();"
              +"</script>";
      return str;
   }
   
   @ResponseBody
  @GetMapping("/getMyAccount")
   public List<AccountDto> getMyAccount(Model model, HttpServletRequest request) {
     System.out.println("내 계좌 불러오기");
     HttpSession session = request.getSession();
     UserDto ldto=(UserDto)session.getAttribute("ldto");
     int userseqno = ldto.getUserseqno();
     System.out.println(userseqno);
     
     List<AccountDto> list = userService.getMyAccount(userseqno);
     System.out.println(list);
     model.addAttribute("list", list);
     
     return list; 
  }
   
   
   @ResponseBody
   @GetMapping("/total")
   public Integer totalmoney(HttpServletRequest request) {
      HttpSession session=request.getSession();
      UserDto ldto=(UserDto)session.getAttribute("ldto");
      int userseqno = ldto.getUserseqno();
      
      Integer totalMoney = userService.totalMoney(request);
      System.out.println(userService.totalMoney(request));
      return totalMoney;
   }

   
   @ResponseBody
   @GetMapping(value = "/CheckAccount")
   public Map<String, String> idChk(String fintech_use_num){
      System.out.println("계좌중복체크");
      
      String resultAccount = userService.CheckAccount(fintech_use_num);
      
      // json 객체로 보내기 위해 Map에 담아서 응답
      // text라면 그냥 String 으로 보내도 됨
      Map<String, String> map = new HashMap<>();
      map.put("fintech_use_num", resultAccount);
      
      return map;
   }
   
   @ResponseBody
   @GetMapping("/dayUseMoney")
   public List<Map<String, Object>> dayUseMoney(HttpServletRequest request) {
       HttpSession session = request.getSession();
       UserDto ldto = (UserDto) session.getAttribute("ldto");
       String email = ldto.getEmail();

       List<Map<String, Object>> result = userService.dayUseMoney(email);
       System.out.println(result);
       return result;
   }
   
   @ResponseBody
   @GetMapping("/UpdateUseMoney")
   public List<Map<String, Object>> UpadateUseMoney(HttpServletRequest request, String startDate, String endDate) {
       HttpSession session = request.getSession();
       UserDto ldto = (UserDto) session.getAttribute("ldto");
       System.out.println( startDate+ ", "+endDate);
       
       String email = ldto.getEmail();
       Map<String, String> map = new HashMap<>();
       map.put("email", email);
       map.put("startDate", startDate);
       map.put("endDate", endDate);
       List<Map<String, Object>> result = userService.UpdateUseMoney(map);
       System.out.println(result);
       return result;
   }
   
   //이용기관 부여번호 9자리를 생성하는 메서드
   public String createNum() {
      String createNum="";
      for (int i = 0; i < 9; i++) {
         createNum+=((int)(Math.random()*10))+"";
      }
      return createNum;
   } 
   //현재시간 구하는 메서드
   public String getDateTime() {
      LocalDateTime now=LocalDateTime.now(); //현재시간 구하기
      String formatNow=now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
      return formatNow;
   }
   
   
   
   
   
   
}




