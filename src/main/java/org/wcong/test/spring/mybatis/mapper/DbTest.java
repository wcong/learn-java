package org.wcong.test.spring.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.wcong.test.spring.MyTransactional;
import org.wcong.test.spring.mybatis.model.TestModel;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/4/21
 */
public interface DbTest {

	@Select("select count(*) from db_test")
	int count();

	@Insert("insert into db_test(id,date,content) values(#{id},now(),#{content})")
	@MyTransactional
	int add(TestModel testModel);

	@Insert("create table db_test(id int,date time,content varchar)")
	int createTable();

}
