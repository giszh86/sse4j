package org.sse.util;

/**
 * @author dux(duxionggis@126.com)
 */
public class Radix {
	private static char[] rDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	/**
	 * (2-36 radix) to (10 radix)
	 * 
	 * @param value
	 * @param radix
	 *            2-36
	 * @return
	 */
	public static long x2h(String value, int radix) {
		if (value == null)
			return 0L;
		value = value.trim().toUpperCase();
		if (value.isEmpty())
			return 0L;
		boolean n = (value.charAt(0) == '-');
		if (n && value.length() == 1)
			return 0L;
		else if (n)
			value = value.substring(1);

		long result = 0;
		for (int i = 0; i < value.length(); i++) {
			result += (long) Math.pow(radix, i) * charindex(value.charAt(value.length() - i - 1));
		}

		if (n)
			return -result;
		else
			return result;
	}

	private static int charindex(char value) {
		int idx = (int) value;
		if (idx >= 48 && idx <= 57) {
			idx -= 48;
		} else if (idx >= 65 && idx <= 90) {
			idx -= 55;
		} else {
			idx = -1;
		}
		return idx;
	}

	/**
	 * (10 radix) to (2-36 radix)
	 * 
	 * @param value
	 * @param radix
	 *            2-36
	 * @return
	 */
	public static String h2x(long value, int radix) {
		long longPositive = Math.abs(value);
		char[] outDigits = new char[64];

		int digitIndex = 0;
		do {
			outDigits[outDigits.length - digitIndex - 1] = rDigits[(int) (longPositive % radix)];
			longPositive /= radix;
			digitIndex++;
		} while (longPositive != 0);
		if (value < 0) {
			outDigits[outDigits.length - digitIndex - 1] = '-';
			digitIndex++;
		}
		return new String(outDigits, outDigits.length - digitIndex, digitIndex);
	}

	public static void main(String[] args) throws Exception {
		System.out.println((" ").isEmpty());
		System.out.println((int) '0');
		System.out.println("A:" + h2x(-123456, 36));
		System.out.println("B:" + x2h("-", 36));
	}
}