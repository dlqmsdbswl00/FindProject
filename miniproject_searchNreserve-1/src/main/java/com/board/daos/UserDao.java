package com.board.daos;

import com.board.dtos.UserDto;

public interface UserDao {
    UserDto getUserByEmail(String email);  // 사용자 정보 조회
    boolean updateUser(UserDto user);   // 사용자 정보 수정
    boolean updatePassword(String email, String newPassword);  // 비밀번호 변경
    boolean deleteUser(String email);  // 회원 탈퇴
}
