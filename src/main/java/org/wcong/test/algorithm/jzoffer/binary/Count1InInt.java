package org.wcong.test.algorithm.jzoffer.binary;

import org.springframework.util.Assert;

/**
 * test for binary operate and or xor and smart move to recursive
 * >> remain the sign bit,>>> ignore the sign bit
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/3/26
 */
public class Count1InInt {

	public static void main(String[] args) {
		Assert.isTrue(count1Slow(0xFFFF) == count1Smart(0xFFFF));
		Assert.isTrue(count1Slow(0x4444) == count1Smart(0x4444));
		Assert.isTrue(count1Slow(0x3214) == count1Smart(0x3214));
		Assert.isTrue(count1Slow(0x10) == count1Smart(0x10));
		Assert.isTrue(count1Slow(0x1111) == count1Smart(0x1111));
	}

	public static int count1Slow(int num) {
		int count = 0;
		while (num != 0) {
			if ((num & 1) != 0) {
				count += 1;
			}
			num = num >> 1;
		}
		return count;
	}

	public static int count1Smart(int num) {
		num = num < 0 ? -num : num;
		int count = 0;
		while (num > 0) {
			count += 1;
			num = num & (num - 1);
		}
		return count;
	}

}
