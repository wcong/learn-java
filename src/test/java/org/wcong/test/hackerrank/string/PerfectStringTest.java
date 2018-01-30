package org.wcong.test.hackerrank.string;

import org.eclipse.core.runtime.Assert;
import org.junit.Test;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 03/05/2017
 */
public class PerfectStringTest {

	@Test
	public void test() {
		PerfectString perfectString = new PerfectString();
		Assert.isTrue(perfectString.perfectNum("abcd") == 3);
		Assert.isTrue(perfectString.perfectNum("abc") == 1);
		Assert.isTrue(perfectString.perfectNum("ddc") == 2);
		Assert.isTrue(perfectString.perfectNum("dddccc") == 19);
	}

}
