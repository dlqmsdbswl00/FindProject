package com.board.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import com.board.dtos.AccountDto;

@Mapper
public interface AccountMapper {
	int addAccount(Map<String, Object> map);

	List<AccountDto> getMyAccount(int userseqno);

	int totalMoney(int userseqno);

	String CheckAccount(String fintech_use_num);

	public List<Map<String, Object>> dayUseMoney(String email);

	public List<Map<String, Object>> UpdateUseMoney(Map<String, String> map);

}
