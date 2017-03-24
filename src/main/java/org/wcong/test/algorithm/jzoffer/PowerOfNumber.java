package org.wcong.test.algorithm.jzoffer;

/**
 * test for exception and number operation
 * give a base number and a exponent return of power of them
 * Created by wcong on 2017/3/24.
 */
public class PowerOfNumber {

    public static void main(String[] args) {

    }

    //
    public static double powerSlow(double base, int exponent) {
        if (base == 0 && exponent <= 0) {
            throw new RuntimeException("no meaning");
        }

        if (exponent == 0) return 1;
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
        if (exponent == 0) return 1;
        boolean positive = exponent >= 0;
        double result = 1.0;
        while (exponent > 0) {
            result = power(base, exponent >> 1);
            result *= result;
            if ((exponent & 1) > 0) {
                result *= base;
            }
        }
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
        while (exponent > 0) {
            result = power(base, exponent >> 1);
            result *= result;
            if ((exponent & 1) > 0) {
                result *= base;
            }
        }
        return result;
    }

}
