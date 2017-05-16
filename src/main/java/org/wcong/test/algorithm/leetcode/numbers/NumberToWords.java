package org.wcong.test.algorithm.leetcode.numbers;

/**
 * Convert a non-negative integer to its english words representation.
 * Given input is guaranteed to be less than 231 - 1.
 * 123 -> "One Hundred Twenty Three"
 * 12345 -> "Twelve Thousand Three Hundred Forty Five"
 * 1234567 -> "One Million Two Hundred Thirty Four Thousand Five Hundred Sixty Seven"
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 16/05/2017
 */
public class NumberToWords {

	private static String[] LEVEL = new String[] { "", "Thousand", "Million", "Billion" };

	private static String[] NUMBERS = new String[] { "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven",
			"Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
			"Eighteen", "Nineteen" };

	private static String[] TEN_LEVEL = new String[] { "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy",
			"Eighty", "Ninety" };

	public String numberToWords(int num) {
		if (num == 0) {
			return "Zero";
		}
		String words = "";
		int level = 0;
		while (num > 0) {
			String result = numberToWords(num % 1000, level);
			if (words.length() > 0 && result.length() > 0) {
				words = result + " " + words;
			} else {
				words = result + words;
			}
			num = num / 1000;
			level += 1;
		}
		return words;
	}

	private String numberToWords(int num, int level) {
		StringBuilder stringBuilder = new StringBuilder();
		if (num >= 100) {
			stringBuilder.append(NUMBERS[num / 100]).append(" ").append("Hundred");
			num = num % 100;
		}
		if (num >= 20) {
			if (stringBuilder.length() > 0) {
				stringBuilder.append(" ");
			}
			stringBuilder.append(TEN_LEVEL[num / 10]);
			num = num % 10;
		}
		if (num > 0) {
			if (stringBuilder.length() > 0) {
				stringBuilder.append(" ");
			}
			stringBuilder.append(NUMBERS[num]);
		}
		if (level > 0 && stringBuilder.length() > 0) {
			stringBuilder.append(" ").append(LEVEL[level]);
		}
		return stringBuilder.toString();
	}

}
