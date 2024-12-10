package com.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.board.dtos.CalDto;

import groovy.transform.Undefined.EXCEPTION;

@Mapper
public interface CalMapper {

   //일정 추가
   public int insertCalBoard(CalDto dto) throws Exception;
   //일정 목록
   public List<CalDto> calBoardList(String ykiho,String yyyyMMdd);
   //일정 상세조회
   public CalDto calBoardDetail(int seq);
   //일정 수정하기
   public boolean calBoardUpdate(CalDto dto);
   //일정 삭제하기
   public boolean calMulDel(Map<String, String[]>map);
   //한달의 일정보여주기
   public List<CalDto> calViewList(String yyyyMM, String ykiho);
   public List<CalDto> usercalViewList(String yyyyMM, String email);
   //일일의 일정개수 보여주기
   public int calBoardCount(String yyyyMMdd, String ykiho);
   
   public boolean pay(Map<String, Object>map);
   
}