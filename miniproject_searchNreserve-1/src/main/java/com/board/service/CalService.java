package com.board.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.board.mapper.CalMapper;
import com.board.utils.Util;
import com.board.command.InsertCalCommand;
import com.board.command.UserUpdateCommand;
import com.board.dtos.CalDto;
import com.board.dtos.UserDto;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class CalService {
   
   @Autowired
   private CalMapper calmapper;
   
//   @Autowired
//   private CalReplyMapper calReplyMapper;
   public Map<String, Integer> makeCalendar(HttpServletRequest request,String ykiho){
	      Map<String ,Integer> map=new HashMap<>();
	      
	      //달력의 날짜를 바꾸기 위해 전달된 year와 month 파라미터를 받는 코드
	      String paramYear=request.getParameter("year");
	      String paramMonth=request.getParameter("month");
	      Calendar cal=Calendar.getInstance(); // 추상클래스이고, static 메서드 new(X)
	      UserDto udto = (UserDto)request.getSession().getAttribute("ldto");

	      int   year=(paramYear==null)?cal.get(Calendar.YEAR):Integer.parseInt(paramYear) ;
	      int   month=(paramMonth==null)?cal.get(Calendar.MONTH)+1:Integer.parseInt(paramMonth) ;                  
	      
	      //                          기본 오늘날짜로 저장할지  :  요청된 날짜로 저장할지
	      //                         calendar객체에서 month는 0~11월임
	      
	      // 11월,12월,13월.....      오류 처리
	      // -2월, -1월 , 0월 , 1월   오류 처리
	      if(month>12) {
	         month=1;
	         year++;
	      }
	      if(month<1) {
	         month=12;
	         year--;
	      }
	      
	      //1.월의 1일에 대한 요일 구하기
	      cal.set(year, month-1,1);// 원하는 날짜로 셋팅
	      int dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);//1~7중에 반환(1:일요일~7:토요일)
	      
	      //2.월의 마지막 날 구하기
	      int lastDay=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	      String yyyyMM=year+Util.isTwo(month+"");//202311 6자리변환


		  List<CalDto>clist=calViewList(yyyyMM, ykiho);
	      request.setAttribute("clist", clist);
	      System.out.println(clist);
	      map.put("year", year);
	      map.put("month", month);
	      map.put("dayOfWeek", dayOfWeek);
	      map.put("lastDay", lastDay);
	      
	      return map;
	   }
   public Map<String, Integer> usermakeCalendar(HttpServletRequest request){
      Map<String ,Integer> map=new HashMap<>();
      
      //달력의 날짜를 바꾸기 위해 전달된 year와 month 파라미터를 받는 코드
      String paramYear=request.getParameter("year");
      String paramMonth=request.getParameter("month");
      Calendar cal=Calendar.getInstance(); // 추상클래스이고, static 메서드 new(X)
      UserDto udto = (UserDto)request.getSession().getAttribute("ldto");
      String email = udto.getEmail();
      int   year=(paramYear==null)?cal.get(Calendar.YEAR):Integer.parseInt(paramYear) ;
      int   month=(paramMonth==null)?cal.get(Calendar.MONTH)+1:Integer.parseInt(paramMonth) ;                  
      
      //                          기본 오늘날짜로 저장할지  :  요청된 날짜로 저장할지
      //                         calendar객체에서 month는 0~11월임
      
      // 11월,12월,13월.....      오류 처리
      // -2월, -1월 , 0월 , 1월   오류 처리
      if(month>12) {
         month=1;
         year++;
      }
      if(month<1) {
         month=12;
         year--;
      }
      
      //1.월의 1일에 대한 요일 구하기
      cal.set(year, month-1,1);// 원하는 날짜로 셋팅
      int dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);//1~7중에 반환(1:일요일~7:토요일)
      
      //2.월의 마지막 날 구하기
      int lastDay=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
      String yyyyMM=year+Util.isTwo(month+"");//202311 6자리변환

      System.out.println(email);
	  List<CalDto>clist=usercalViewList(yyyyMM, email);
      request.setAttribute("clist", clist);
      System.out.println(clist);
      map.put("year", year);
      map.put("month", month);
      map.put("dayOfWeek", dayOfWeek);
      map.put("lastDay", lastDay);
      
      return map;
   }

   public List<CalDto> calViewList(String yyyyMM,String ykiho){
      return calmapper.calViewList(yyyyMM, ykiho);
   }
   public List<CalDto> usercalViewList(String yyyyMM,String email){
	      return calmapper.usercalViewList(yyyyMM, email);
	   }
   public boolean pay(String fintech_use_num, int money) {
      Map<String, Object> map = new HashMap<>();
      map.put("fintech_use_num", fintech_use_num);
      map.put("remaining_balance", money);
      return calmapper.pay(map);
      
   }
   
   
   public boolean insertCalBoard(InsertCalCommand insertCalCommand, String ykiho, String yadmNm, int useMoney) throws Exception {
      // command --> dto로  값을 이동
      // DB에서는 mdate 컬럼 , command에서는 year, month... : 12자리로 변환작업
      String mdate=insertCalCommand.getYear()
                +Util.isTwo(insertCalCommand.getMonth()+"")
                +Util.isTwo(insertCalCommand.getDate()+"")
                +Util.isTwo(insertCalCommand.getHour()+"")
                +Util.isTwo(insertCalCommand.getMin()+"");
      
      System.out.println(ykiho);
      //command --> dto 값 복사 
      CalDto dto = new CalDto();
      dto.setTitle(insertCalCommand.getTitle());
      dto.setContent(insertCalCommand.getContent());
      dto.setMdate(mdate);
      dto.setEmail(insertCalCommand.getEmail());
      dto.setYkiho(ykiho);
      dto.setYadmNm(yadmNm);
      dto.setUseMoney(useMoney);
      
      int count=calmapper.insertCalBoard(dto);
      
      return count>0?true:false;
   }
   
   public List<CalDto> calBoardList(String ykiho,String yyyyMMdd) {
      return calmapper.calBoardList(ykiho,yyyyMMdd);
   }
//   
   public int calBoardCount(String yyyyMMdd,String ykiho) {
      return calmapper.calBoardCount(yyyyMMdd,ykiho);
   }
//
//   public boolean calMulDel(Map<String, String[]> map) {
//      return calmapper.calMulDel(map);
//      
//   }
//   public boolean calBoardUpdate(UpdateCalCommand updateCalCommand) {
//      //command:year,month,date.. ---> dto: mdate
//      String mdate=updateCalCommand.getYear()
//             +Util.isTwo(updateCalCommand.getMonth()+"")
//             +Util.isTwo(updateCalCommand.getDate()+"")
//             +Util.isTwo(updateCalCommand.getHour()+"")
//             +Util.isTwo(updateCalCommand.getMin()+""); // 12자리
//      
//      //dto <---command값
//      CalDto dto=new CalDto();
//      dto.setSeq(updateCalCommand.getSeq());
//      dto.setTitle(updateCalCommand.getTitle());
//      dto.setContent(updateCalCommand.getContent());
//      dto.setMdate(mdate);
//      
//      return calmapper.calBoardUpdate(dto);
//   }
//   public CalDto calBoardDetail(int seq) {
//      return calmapper.calBoardDetail(seq);
//   }
//   
//   public boolean insertCalReply(InsertCalReplyCommand insertCalCommand) throws Exception {
//        
//         CalDto dto=new CalDto();
//         dto.setSeq(insertCalCommand.getSeq());
//         dto.setId(insertCalCommand.getId());
//         dto.setContent(insertCalCommand.getContent());
//         
//         int count=calReplyMapper.insertCalReplyBoard(dto);
//        
//         return count>0?true:false;
//      }
//   
//   
//   public List<CalDto> showCalReply(int seq) throws Exception{    
//       return calReplyMapper.getCalReplyBoard(seq);
//   }
}









