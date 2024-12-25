package com.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.board.dtos.AccountDto;
import com.board.dtos.CalDto;
import com.board.dtos.ExpenseDto;
import com.board.dtos.ExpenseMonDto;
import com.board.dtos.UserDto;

@Mapper
public interface UserMapper {
   public int addUser(UserDto dto);
   public UserDto loginUser(UserDto dto);
   public UserDto userInfo(String email);
   
   
        // 정보 수정
      public boolean updateUser(UserDto dto);
      
      public List<CalDto> userReserve(String email);
      
      public int addAccount(Map<String, Object>map);
      
      public List<AccountDto> getMyAccount(int userseqno);
      
      
      public int totalMoney(int userseqno);

      public String CheckAccount(String fintech_use_num);
      
      public List<Map<String, Object>> dayUseMoney(String email);
      
      public List<Map<String, Object>>UpdateUseMoney(Map<String, String> map);
      
      public boolean insertExpense(ExpenseDto dto);
      
      public ExpenseMonDto monthlyExpense(Map<String, String> map);
      
      public List<ExpenseDto> monthlyExpenseList(Map<String, String> map);
}
     