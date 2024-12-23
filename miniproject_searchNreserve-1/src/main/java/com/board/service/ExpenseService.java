package com.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.board.dtos.ExpenseDto;
import com.board.dtos.ExpenseMonDto;
import com.board.mapper.ExpenseMapper;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseMapper expenseMapper;

    public boolean saveExpense(ExpenseDto expense) {
        return expenseMapper.insertExpense(expense);
    }

    public ExpenseMonDto monExpense(String year, String month, String email) {
        Map<String, String> map = new HashMap<>();
        map.put("year", year);
        map.put("month", month);
        map.put("email", email);
        return expenseMapper.monthlyExpense(map);
    }

    public List<ExpenseDto> monthlyExpenseList(String year, String month, String email) {
        Map<String, String> map = new HashMap<>();
        map.put("year", year);
        map.put("month", month);
        map.put("email", email);
        return expenseMapper.monthlyExpenseList(map);
    }
}
