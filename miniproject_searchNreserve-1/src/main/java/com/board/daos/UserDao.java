package com.board.daos;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import com.board.dtos.UserDto;

@Mapper
public interface UserDao {

    @Select("SELECT * FROM users WHERE email = #{email}")
    UserDto getUserByEmail(String email); // 사용자 정보 조회

    // 사용자 정보 수정
    @Update("UPDATE users SET name = #{name}, address = #{address}, birth = #{birth}, phone = #{phone} WHERE email = #{email}")
    boolean updateUser(UserDto user); // 사용자 정보 수정

    @Update("UPDATE users SET password = #{newPassword} WHERE email = #{email}")
    void updatePassword(String email, String newPassword); // 비밀번호 변경

    @Delete("DELETE FROM users WHERE email = #{email}")
    void deleteUser(String email); // 회원 탈퇴
}
