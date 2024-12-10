package com.board.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.board.command.InsertCalCommand;
import com.board.dtos.CalDto;
import com.board.dtos.UserDto;
import com.board.service.CalService;
import com.board.service.UserService;
import com.board.utils.Util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/cal")
public class CalController {
   
   @Autowired
   CalService calService;
   @Autowired
   UserService userService;
    @GetMapping(value="/calendar")
       public String calendar(Model model, HttpServletRequest request,  @RequestParam("ykiho") String ykiho, String yadmNm) {
          System.out.println("병원의 예약 현황 보기");
          System.out.println(ykiho + ", " + yadmNm);
          //달력에서 일일별 일정목록 구하기
          String year=request.getParameter("year");
          String month=request.getParameter("month");
         
          if(year==null||month==null) {
             Calendar cal=Calendar.getInstance();
             year=cal.get(Calendar.YEAR)+"";
             month=(cal.get(Calendar.MONTH)+1)+"";
          }
          System.out.println("year:"+year);
          System.out.println("month:"+month);
          //달력만들기위한 값 구하기
          Map<String, Integer>map=calService.makeCalendar(request,ykiho);
          model.addAttribute("calMap", map);
          model.addAttribute("ykiho", ykiho);
          model.addAttribute("yadmNm", yadmNm);
          String yyyyMM=year+Util.isTwo(month);//202311 6자리변환
//          List<CalDto>clist=calService.calViewList(yyyyMM, ykiho);
//          model.addAttribute("clist", clist);
         
          return "cal/Calendar";
       }
    @GetMapping(value="/userCal")
    public String usercalendar(Model model, HttpServletRequest request) {
    	HttpSession session=request.getSession();
    	String email = ((UserDto) session.getAttribute("ldto")).getEmail();
       //달력에서 일일별 일정목록 구하기
       String year=request.getParameter("year");
       String month=request.getParameter("month");
      
       if(year==null||month==null) {
          Calendar cal=Calendar.getInstance();
          year=cal.get(Calendar.YEAR)+"";
          month=(cal.get(Calendar.MONTH)+1)+"";
       }
       System.out.println("year:"+year);
       System.out.println("month:"+month);
       //달력만들기위한 값 구하기
       Map<String, Integer>map=calService.usermakeCalendar(request);
       model.addAttribute("calMap", map);
       model.addAttribute("email",email);
       String yyyyMM=year+Util.isTwo(month);//202311 6자리변환
//       List<CalDto>clist=calService.usercalViewList(yyyyMM, email);
//       System.out.println(clist);
//       
//       
//       model.addAttribute("clist", clist);
      
       return "user/userCal";
    }
       @ResponseBody
       @GetMapping(value = "/cal", produces = "application/json")
       public ResponseEntity<List<Map<String,String>>> cal(String sgguCd, String dgsbjtCd, String ykiho) throws IOException, ParserConfigurationException {
           System.out.println("검색목록 보기");
           HttpURLConnection conn = null;
           List<Map<String, String>> resultList = new ArrayList<>();
           
           try {
               URL url = new URL("https://apis.data.go.kr/B551182/hospAsmInfoService/getHospAsmInfo?"
                       + "pageNo=1"
                       + "&numOfRows=10"
                       + "&ServiceKey=%2BDfDMIhG5kDUTz24ii%2FSw6DK5tVQExSNQmQQy7eyHU2tebzs14UXPh%2FWhQEluGOQ97nb3NBaelLCkgHRfvVgSQ%3D%3D"
                       + "&ykiho=" + ykiho);

               conn = (HttpURLConnection) url.openConnection();
               conn.setRequestMethod("GET");
               conn.setRequestProperty("Content-Type", "application/xml");
               conn.setDoOutput(true);

               // Parse the XML response
               DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
               DocumentBuilder builder = factory.newDocumentBuilder();
               Document doc = builder.parse(conn.getInputStream());

               // Extract data from XML
               NodeList itemList = doc.getElementsByTagName("item");
               
               for (int i = 0; i < itemList.getLength(); i++) {
                   Node itemNode = itemList.item(i);
                   Map<String, String>map = new HashMap<>();  
                   if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                       Element itemElement = (Element) itemNode;
                       String yadmNm = itemElement.getElementsByTagName("yadmNm").item(0).getTextContent();
//                       String ykiho = itemElement.getElementsByTagName("ykiho").item(0).getTextContent();
                       map.put("yadmNm",yadmNm);
//                       map.put("ykiho",ykiho);
                       resultList.add(map);
                   }
               }

               // Set the Content-Type header to application/json
               HttpHeaders headers = new HttpHeaders();
               headers.setContentType(MediaType.APPLICATION_JSON);

               return new ResponseEntity<>(resultList, headers, HttpStatus.OK);
           } catch (Exception e) {
               e.printStackTrace();
               return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
           } finally {
               if (conn != null) {
                   conn.disconnect();
               }
           }
       }
       
       
       @GetMapping(value = "/addCalBoardForm")
       public String addCalBoardForm(Model model, InsertCalCommand insertCalCommand, String ykiho, String yadmNm) {
          System.out.println("일정추가 폼 이동");
          System.out.println("병원이름: " + yadmNm);
          System.out.println(insertCalCommand);
          //addCalBoardfForm 페이지에서 유효값 처리를 위해 insertCalCommand 받고 있기때문에
          model.addAttribute("insertCalCommand", insertCalCommand);
          model.addAttribute("ykiho",ykiho);
          model.addAttribute("yadmNm", yadmNm);
          return "cal/addCalBoardForm";
       }
       
       @GetMapping(value = "payForm")
       public String payForm() {
          System.out.println("결제 폼 이동");
          return "cal/calPay";
       }
       
       @PostMapping(value = "/addCalBoard")
       public String addCalBoard(@Validated InsertCalCommand insertCalCommand,
                           BindingResult result, String ykiho, String yadmNm, int useMoney) throws Exception {
          System.out.println("일정추가하기");
          System.out.println(insertCalCommand);
          if(result.hasErrors()) {
             System.out.println("글을 모두 입력해야 함");
             return "cal/addCalBoardForm";
          }
          calService.insertCalBoard(insertCalCommand, ykiho, yadmNm, useMoney);
          System.out.println("성공");
          return "redirect:/cal/calendar?ykiho=" + ykiho;
       }
       @GetMapping(value = "/calBoardList")
       public String calBoardList(@RequestParam Map<String, String>map
                         , HttpServletRequest request
                         , Model model,  @RequestParam("ykiho") String ykiho,String yadmNm) {


          HttpSession session=request.getSession();
          

          if(map.get("year")==null) {
             //조회한 상태이기때문에 ymd가 저장되어 있어서 값을 가져옴
             map=(Map<String, String>)session.getAttribute("ymdMap");
          }else {
             //일정을 처음 조회했을때 ymd를 저장함
             session.setAttribute("ymdMap", map);
          }
          
          //달력에서 전달받은 파라미터 year, month, date를 8자리로 만든다.
          String yyyyMMdd=map.get("year")
                       +Util.isTwo(map.get("month"))
                       +Util.isTwo(map.get("date"));
          List<CalDto> list= calService.calBoardList(ykiho,yyyyMMdd);
          model.addAttribute("list", list);
          model.addAttribute("ykiho", ykiho);
          model.addAttribute("yadmNm", yadmNm);
          System.out.println(ykiho);
          System.out.println(yadmNm);
          return "cal/calBoardList";
       }
       
       @PostMapping("/payinfo")
       public ResponseEntity<String> payInfo(@RequestBody JSONObject requestData) throws IOException {
           System.out.println("결제하기[계좌]");

           HttpURLConnection conn = null;

           try {
               URL apiUrl = new URL("https://testapi.openbanking.or.kr/v2.0/transfer/withdraw/fin_num");
               conn = (HttpURLConnection) apiUrl.openConnection();

               conn.setRequestMethod("POST");
               conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
               conn.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIxMTAxMDQxNTk0Iiwic2NvcGUiOlsiaW5xdWlyeSIsImxvZ2luIiwidHJhbnNmZXIiXSwiaXNzIjoiaHR0cHM6Ly93d3cub3BlbmJhbmtpbmcub3Iua3IiLCJleHAiOjE3MTA2NjM2MzEsImp0aSI6ImI5OTdhOTgzLTM2NDgtNDAzZC05MThjLWU4N2IwMTA5MTk3YyJ9.Te5QQX6ZqRoyZBQHweKiqHtmnr1xSTcjQWJZEPvcENo");

               conn.setDoOutput(true);

               try (OutputStream os = conn.getOutputStream()) {
                   byte[] input = requestData.toJSONString().getBytes("utf-8");
                   os.write(input, 0, input.length);
               }

               try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                   StringBuilder response = new StringBuilder();
                   String responseLine;
                   while ((responseLine = br.readLine()) != null) {
                       response.append(responseLine.trim());
                   }

                   System.out.println("Response from Open Banking API: " + response.toString());

                   return new ResponseEntity<>(response.toString(), HttpStatus.OK);
               }
           } catch (Exception e) {
               e.printStackTrace();
               return new ResponseEntity<>("Error during payment", HttpStatus.INTERNAL_SERVER_ERROR);
           } finally {
               if (conn != null) {
                   conn.disconnect();
               }
           }
       }
       

       
       @ResponseBody
       @GetMapping(value = "/pay")
       public boolean pay(String fintech_use_num, String remaining_balance) {
          System.out.println("결제하기");
          int money = Integer.parseInt(remaining_balance);
          System.out.println(fintech_use_num + ", " + money);
          
          boolean isS = calService.pay(fintech_use_num, money);
          
          return isS;
       }
       

       @ResponseBody
       @GetMapping(value="/calCountAjax")
       public Map<String,Integer> calCountAjax(String yyyyMMdd,String ykiho){
    	  System.out.println(ykiho);
          Map<String, Integer>map=new HashMap<>();
          int count=calService.calBoardCount(yyyyMMdd,ykiho);
          map.put("count", count);
          return map;
       }
      //이용기관 부여번호 9자리를 생성하는 메서드
      public String createNum() {
         String createNum="";
         for (int i = 0; i < 9; i++) {
            createNum+=((int)(Math.random()*10))+"";
         }
         System.out.println("이용기관부여번호9자리생성:"+createNum);
         return createNum;
      }
      //현재시간 구하는 메서드
      public String getDateTime() {
         LocalDateTime now=LocalDateTime.now(); //현재시간 구하기
         String formatNow=now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
         return formatNow;
      }
}