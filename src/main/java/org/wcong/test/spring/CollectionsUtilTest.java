package org.wcong.test.spring;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.NotNullPredicate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 15/12/9
 */
public class CollectionsUtilTest {

	public static void main(String[] args) {
		List<Integer> test = new ArrayList<Integer>();
		test.add(0);
		test.add(null);
		test.add(2);
		test.add(10);
		System.out.println(test);
		CollectionUtils.filter(test, NotNullPredicate.getInstance());
		System.out.println(test);
	}

}
