package org.wcong.test.spring.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.transaction.annotation.Transactional;
import org.wcong.test.spring.mybatis.model.TestModel;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/4/21
 */
public interface DbTest {

	@Select("select count(*) from db_test")
	@Transactional
	int count();

	@Insert("insert into db_test(id,date,content) values(#{id},now(),#{content})")
	@Transactional
	int add(TestModel testModel);

	@Insert("create table db_test(id int,date time,content varchar)")
	int createTable();

}
