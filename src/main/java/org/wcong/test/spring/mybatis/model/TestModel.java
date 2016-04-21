package org.wcong.test.spring.mybatis.model;

import java.util.Date;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/4/21
 */
public class TestModel {

	private Integer id;

	private Date date;

	private String content;

	public TestModel() {
	}

	public TestModel(String content) {
		this.content = content;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
