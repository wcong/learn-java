package org.wcong.test.hackerrank.string;

import org.eclipse.core.runtime.Assert;
import org.junit.Test;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 04/05/2017
 */
public class BearAndSteadyGeneTest {

	@Test
	public void test() {
		BearAndSteadyGene bearAndSteadyGene = new BearAndSteadyGene();
		Assert.isTrue(bearAndSteadyGene.minimumReplace("GAAATAAA") == 5);

	}

}
