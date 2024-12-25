package com.board.config;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = { "com.board.mapper", "com.board.daos" }) // UserDao 위치 포함
public class MyBatisConfig {

	private final DataSource dataSource;

	public MyBatisConfig(DataSource dataSource) {
		this.dataSource = dataSource; // Spring Boot가 자동 설정한 DataSource 주입
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		return factoryBean.getObject(); // SqlSessionFactory 객체 반환
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory); // SqlSessionTemplate 객체 반환
	}

}
