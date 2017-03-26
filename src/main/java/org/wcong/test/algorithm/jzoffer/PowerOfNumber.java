package org.wcong.test.algorithm.jzoffer;

import org.springframework.util.Assert;

/**
 * test for exception and number operation
 * give a base number and a exponent return of power of them
 * Created by wcong on 2017/3/24.
 */
public class PowerOfNumber {

	public static void main(String[] args) {
		Assert.isTrue(powerSlow(3, 9) == powerLogN(3, 9));
		Assert.isTrue(powerSlow(-3, -9) == powerLogN(-3, -9));
		Assert.isTrue(powerSlow(0, 0) == powerLogN(0, 0));
	}

	//
	public static double powerSlow(double base, int exponent) {
		if (base == 0 && exponent <= 0) {
			throw new RuntimeException("no meaning");
		}

		if (exponent == 0)
			return 1;
		boolean positive = exponent >= 0;
		exponent = positive ? exponent : -exponent;
		double result = base;
		for (int i = 1; i < exponent; i++) {
			result *= base;
		}
		if (!positive) {
			result = 1.0 / result;
		}
		return result;
	}

	public static double powerLogN(double base, int exponent) {
		if (base == 0 && exponent <= 0) {
			throw new RuntimeException("no meaning");
		}
		if (exponent == 0)
			return 1;
		boolean positive = exponent >= 0;
		exponent = positive ? exponent : -exponent;
		double result = power(base, exponent);
		if (!positive) {
			result = 1.0 / result;
		}
		return result;
	}

	private static double power(double base, int exponent) {
		double result = 1.0;
		if (exponent <= 0) {
			return result;
		}
		result = power(base, exponent >> 1);
		result *= result;
		if ((exponent & 1) > 0) {
			result *= base;
		}
		return result;
	}

}
