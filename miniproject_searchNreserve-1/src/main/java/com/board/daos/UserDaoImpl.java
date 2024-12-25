package com.board.daos;

import java.util.Map;
import java.util.HashMap;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.board.dtos.UserDto;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private SqlSession sqlSession;

    private static final String NAMESPACE = "com.board.mapper.UserMapper.";

    @Override
    public UserDto getUserByEmail(String email) {
        // 사용자 이메일로 조회
        return sqlSession.selectOne(NAMESPACE + "getUserByEmail", email);
    }

    @Override
    public boolean updateUser(UserDto user) {
        // 사용자 정보 수정
        int result = sqlSession.update(NAMESPACE + "updateUser", user);
        return result > 0;  // 업데이트가 성공하면 true 반환
    }

    @Override
    public boolean updatePassword(String email, String newPassword) {
        // 비밀번호 변경
        int result = sqlSession.update(NAMESPACE + "updatePassword", Map.of("email", email, "newPassword", newPassword));
        return result > 0;  // 변경이 성공하면 true 반환
    }

    @Override
    public boolean deleteUser(String email) {
        // 사용자 탈퇴 (삭제)
        int result = sqlSession.delete(NAMESPACE + "deleteUser", email);
        return result > 0;  // 삭제가 성공하면 true 반환
    }
}
