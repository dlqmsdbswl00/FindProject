package com.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.board.dtos.AccountDto;
import com.board.mapper.AccountMapper;

@Service
public class AccountService {

    @Autowired
    private AccountMapper accountMapper;

    public boolean addAccount(String money, String fintech_use_num, String bank_name, int userseqno, String account_num_masked) {
        Map<String, Object>map=new HashMap<>();
        map.put("money", money);
        map.put("fintech_use_num", fintech_use_num);
        map.put("bank_name", bank_name);
        map.put("userseqno", userseqno);
        map.put("account_num_masked", account_num_masked);
        int count = accountMapper.addAccount(map);
        return count>0?true:false;
     }

    public List<AccountDto> getMyAccount(int userseqno){
        System.out.println("service까지"+userseqno);
        return accountMapper.getMyAccount(userseqno);
     }
    public int totalMoney(int userSeqNo) {
        return accountMapper.totalMoney(userSeqNo);
    }

    public String checkAccount(String fintechUseNum) {
        return accountMapper.CheckAccount(fintechUseNum);
    }
    
    public List<Map<String, Object>> dayUseMoney(String email){
        return accountMapper.dayUseMoney(email);
     }
     
     public List<Map<String, Object>> UpdateUseMoney(Map<String, String> map){
        return accountMapper.UpdateUseMoney(map);
     }
}
