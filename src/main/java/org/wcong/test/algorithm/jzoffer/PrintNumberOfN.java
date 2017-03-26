package org.wcong.test.algorithm.jzoffer;

/**
 * test number limit boundary
 * give a number n print all the numbers of decimal
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/3/26
 */
public class PrintNumberOfN {

	public static void main(String[] args) {
		printNumberOfN(3);
	}

	public static void printNumberOfN(int n) {
		char[] number = new char[n];
		printInteger(number, 0, n - 1);
	}

	private static void printInteger(char[] number, int index, int n) {
		if (index > n) {
			int startIndex = 0;
			while (startIndex < n) {
				if (number[startIndex] == 48) {
					startIndex += 1;
				} else {
					break;
				}
			}
			while (startIndex <= n) {
				System.out.print(number[startIndex++]);
			}
			System.out.print("\n");
			return;
		}
		for (int j = 48; j < 58; j++) {
			number[index] = (char) j;
			printInteger(number, index + 1, n);
		}
	}

}
