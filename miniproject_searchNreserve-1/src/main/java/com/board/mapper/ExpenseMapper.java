package com.board.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import com.board.dtos.ExpenseDto;
import com.board.dtos.ExpenseMonDto;

@Mapper
public interface ExpenseMapper {
	boolean insertExpense(ExpenseDto dto);

	ExpenseMonDto monthlyExpense(Map<String, String> map);

	List<ExpenseDto> monthlyExpenseList(Map<String, String> map);
}
