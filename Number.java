import java.util.ArrayList;
import java.util.Scanner;

public class Number {
	ArrayList<Long> digits = new ArrayList<Long>();
	final long BASE = 100000;
	final int lb = (int) Math.log10(BASE);

	Number(long a) {
		if (a == 0) {
			this.digits.add((long) 0);
		} else {
			if (a < 0) {
				a = Math.abs(a);
				while (a > 0) {
					this.digits.add(-(a % this.BASE));
					a = a / this.BASE;
				}
			} else {
				while (a > 0) {
					this.digits.add(a % this.BASE);
					a = a / this.BASE;
				}
			}
		}
	}

	Number(Number a) {
		for (int i = 0; i < a.digits.size(); i++) {
			this.digits.add(a.getDigit(i));
		}
	}

	public void normalize() {
		for (int i = 0; i < digits.size(); i++) {
			long digit = this.getDigit(i);
			if (Math.abs(digit) >= this.BASE) {
				digits.set(i, (digit % BASE));
				if (i == digits.size() - 1) {
					digits.add((long) 0);
				}
				digits.set(i + 1, digits.get(i + 1) + (digit / BASE));
			}
		}
		long sign = this.getDigit(this.digits.size() - 1);
		for (int i = 0; i < digits.size(); i++) {
			long digit = this.getDigit(i);
			if (digit < 0) {
				if (sign > 0) {
					this.digits.set(i, this.getDigit(i) + this.BASE);
					this.digits.set(i + 1, this.getDigit(i) - 1);
				}
			} else {
				if (sign < 0) {
					this.digits.set(i, this.getDigit(i) - this.BASE);
					this.digits.set(i + 1, this.getDigit(i) + 1);
				}
			}
		}
		this.clearNulls();
	}

	public void Normalize() {
		long sign = this.getDigit(this.digits.size() - 1);
		for (int i = 0; i < digits.size(); i++) {
			long digit = digits.get(i);

			if (sign >= 0 && digit >= 0) {
				if (digit >= BASE) {
					digits.set(i, (digit % BASE));
					if (i == digits.size() - 1) {
						digits.add((long) 0);
					}
					digits.set(i + 1, digits.get(i + 1) + (digit / BASE));
				}
			} else {
				if (sign <= 0 && digit <= 0) {
					digit = Math.abs(digit);
					if (digit >= BASE) {
						digits.set(i, -(digit % BASE));
						if (i == digits.size() - 1) {
							digits.add((long) 0);
						}
						digits.set(i + 1, digits.get(i + 1) - (digit / BASE));
					}
				} else {
					if (sign >= 0 && digit < 0) {
						digit = Math.abs(digit);
						digits.set(i, BASE - (digit % BASE));
						digits.set(i + 1, digits.get(i + 1) - (digit / BASE) - 1);
					} else {
						digits.set(i, ((digit % BASE) - BASE) % BASE);
						digits.set(i + 1, digits.get(i + 1) + ((digit - 1) / BASE) + 1);
					}
				}
			}
		}
	}

	static Number multiply_digit(Number n, long a) {
		Number ans = new Number(n);
		long prev = 0;
		long check = a * ans.digits.get(ans.digits.size() - 1);
		for (int i = 0; i < ans.digits.size(); i++) {
			long digit = Math.abs(ans.digits.get(i));
			ans.digits.set(i, ((digit * Math.abs(a)) % ans.BASE + prev) % ans.BASE);
			if (((digit * Math.abs(a)) % ans.BASE + prev) < ans.BASE) {
				prev = (digit * Math.abs(a)) / ans.BASE;
			} else {
				prev = (digit * Math.abs(a)) / ans.BASE + 1;
			}
		}
		if (prev > 0) {
			ans.digits.add(prev);
		}
		if (check < 0) {
			for (int i = 0; i < ans.digits.size(); i++) {
				ans.digits.set(i, -ans.digits.get(i));
			}
		}
		ans.clearNulls();
		return ans;
	}

	void addNulls(int n) {
		for (int i = 0; i < n; i++) {
			this.digits.add(0, (long) 0);
		}
	}

	void clearNulls() {
		for (int i = this.digits.size() - 1; i > 0; i--) {
			if (this.getDigit(i) != 0) {
				break;
			} else {
				this.digits.remove(i);
			}
		}
	}

	Number leftDigits(int l) {
		StringBuilder sb = new StringBuilder();
		for (int i = this.digits.size() - 1; i > l - 1; i--) {
			if (Math.abs(this.getDigit(i)) != 0) {
				if (i != Math.abs(this.digits.size()) - 1) {
					long tmp = Math.abs(this.getDigit(i));
					while (tmp < this.BASE / 10) {
						sb.insert(sb.length(), "0");
						tmp *= 10;
					}
				}
				sb.insert(sb.length(), Long.toString(Math.abs(this.getDigit(i))));
			} else {
				for (int j = 0; j < this.lb; j++) {
					sb.insert(sb.length(), "0");
				}
			}
		}
		if (sb.toString().equals("")) {
			return new Number("0");
		}
		Number ans = new Number(sb.toString());
		return ans;
	}

	Number rightDigits(int l) {
		StringBuilder sb = new StringBuilder();
		for (int i = l - 1; i > -1; i--) {
			if (Math.abs(this.getDigit(i)) != 0) {
				if (i != Math.abs(this.digits.size()) - 1) {
					long tmp = Math.abs(this.getDigit(i));
					while (tmp < this.BASE / 10) {
						sb.insert(sb.length(), "0");
						tmp *= 10;
					}
				}
				sb.insert(sb.length(), Long.toString(Math.abs(this.getDigit(i))));
			} else {
				for (int j = 0; j < this.lb; j++) {
					sb.insert(sb.length(), "0");
				}
			}
		}
		if (sb.toString().equals("")) {
			return new Number("0");
		}
		Number ans = new Number(sb.toString());
		return ans;
	}

	static int Comprasion(Number a, Number b) {

		for (int i = Math.max(a.digits.size(), b.digits.size()) - 1; i > -1; i--) {
			if (a.getDigit(i) != b.getDigit(i)) {
				if (a.getDigit(i) > b.getDigit(i)) {
					return 1;
				} else {
					return -1;
				}
			}
		}

		return 0;
	}

	static int comprasion_zero(Number a) {
		long sign = a.getDigit(a.digits.size() - 1);
		if (sign > 0) {
			return 1;
		}
		if (sign < 0) {
			return -1;
		}
		return 0;
	}

	static Number sign_multiply(Number a, Number b) {
		Number ans = multiply(a, b);
		if ((a.getDigit(a.digits.size() - 1) < 0 && b.getDigit(b.digits.size() - 1) > 0)
				|| (a.getDigit(a.digits.size() - 1) > 0 && b.getDigit(b.digits.size() - 1) < 0)) {
			for (int i = 0; i < ans.digits.size(); i++) {
				ans.digits.set(i, (-ans.getDigit(i)));
			}
		}
		return ans;
	}

	static Number multiply(Number first, Number second) {
		if ((first.digits.size() == 1 && first.getDigit(0) == 0)
				|| (second.digits.size() == 1 && second.getDigit(0) == 0)) {
			Number res = new Number("0");
			return res;
		}
		if (first.getDigit(first.digits.size() - 1) < 0) {

		}
		int size = Math.max(first.digits.size(), second.digits.size());
		if (size <= 2) {
			Number a = first.leftDigits((size + 1) / 2);
			Number b = first.rightDigits((size + 1) / 2);
			Number c = second.leftDigits((size + 1) / 2);
			Number d = second.rightDigits((size + 1) / 2);
			Number ans1 = multiply_digit(new Number(a.getDigit(0)), c.getDigit(0));
			Number ans2 = multiply_digit(new Number(b.getDigit(0)), d.getDigit(0));
			Number ans3 = new Number("0");
			Number x = normsum(c, d);
			Number y = normsum(a, b);
			ans3 = multiply_digit(y, x.getDigit(0));
			if (x.getDigit(1) != 0) {
				y.addNulls(1);
				ans3 = normsum(ans3, y);
			}
			ans3 = normdifference(normdifference(ans3, ans1), ans2);
			ans1.addNulls(2);
			ans3.addNulls(1);
			ans1.clearNulls();
			ans2.clearNulls();
			ans3.clearNulls();
			return (normsum(normsum(ans1, ans2), ans3));
		}
		Number a = first.leftDigits((size + 1) / 2);
		Number b = first.rightDigits((size + 1) / 2);
		Number c = second.leftDigits((size + 1) / 2);
		Number d = second.rightDigits((size + 1) / 2);
		Number ans1 = multiply(a, c);
		Number ans2 = multiply(b, d);
		Number ans3 = multiply(normsum(a, b), normsum(c, d));
		ans3 = normdifference(normdifference(ans3, ans1), ans2);
		ans3.addNulls((size + 1) / 2);
		ans1.addNulls(((size + 1) / 2) * 2);
		return (normsum(normsum(ans1, ans2), ans3));

	}

	static Number[] division_and_remainder(Number a, Number b) {
		Number arr[] = new Number[2];
		arr[0] = new Number("0");
		arr[1] = new Number("0");
		boolean asign = true;
		boolean bsign = true;
		Number x = new Number(a);
		Number y = new Number(b);
		if (a.getDigit(a.digits.size() - 1) < 0) {
			asign = false;
			for (int i = 0; i < a.digits.size(); i++) {
				x.digits.set(i, Math.abs(a.getDigit(i)));
			}
		}
		if (b.getDigit(b.digits.size() - 1) < 0) {
			bsign = false;
			for (int i = 0; i < b.digits.size(); i++) {
				y.digits.set(i, Math.abs(b.getDigit(i)));
			}
		}
		Number tmp = new Number("0");
		for (int i = x.digits.size() - 1; i > -1; i--) {
			tmp.addNulls(1);
			tmp = normsum(tmp, new Number(x.getDigit(i)));
			if (comprasion_zero(tmp) == 0) {
				arr[0].addNulls(1);
			} else {
				if (Comprasion(tmp, y) < 0) {
					arr[0].addNulls(1);
				} else {
					if (Comprasion(tmp, y) > -1) {
						long left = 0;
						long right = x.BASE;
						while (left <= right) {
							Number check = multiply_digit(y, (left + right) / 2);
							int cnt = Comprasion(check, tmp);
							if (cnt == -1) {
								left = (left + right) / 2;
							} else {
								if (cnt == 1) {
									right = (left + right) / 2;
								} else {
									arr[0].addNulls(1);
									arr[0] = normsum(arr[0], new Number((left + right) / 2));
									tmp = new Number("0");
									break;
								}
							}
							if (right - left <= 1) {
								arr[0].addNulls(1);
								arr[0] = normsum(arr[0], new Number((left + right) / 2));
								check = multiply_digit(y, left);
								tmp = normdifference(tmp, check);
								break;
							}
						}
					}
				}
			}
			tmp.clearNulls();
			arr[0].clearNulls();
		}
		if ((asign == false && bsign == true) || (asign == true && bsign == false)) {
			for (int i = 0; i < arr[0].digits.size(); i++) {
				arr[0].digits.set(i, -arr[0].getDigit(i));
			}
		}
		if (bsign == false) {
			for (int i = 0; i < y.digits.size(); i++) {
				y.digits.set(i, -y.getDigit(i));
			}
		}
		Number ans = sign_multiply(arr[0], y);
		ans.clearNulls();
		arr[1] = normdifference(a, ans);
		arr[1].clearNulls();
		return arr;
	}

	long getDigit(int i) {
		if (i >= digits.size()) {
			return 0;
		} else {
			return digits.get(i);
		}
	}

	static Number normsum(Number a, Number b) {
		Number ans = new Number(a);
		for (int i = a.digits.size(); i < Math.max(a.digits.size(), b.digits.size()); i++) {
			ans.digits.add((long) 0);
		}
		for (int i = 0; i < Math.max(a.digits.size(), b.digits.size()); i++) {
			ans.digits.set(i, ans.getDigit(i) + b.getDigit(i));
			ans.Normalize();
		}
		
		ans.clearNulls();
		return ans;
	}

	static Number normdifference(Number a, Number b) {
		Number ans = new Number(a);
		for (int i = a.digits.size(); i < Math.max(a.digits.size(), b.digits.size()); i++) {
			ans.digits.add((long) 0);
		}
		for (int i = 0; i < Math.max(a.digits.size(), b.digits.size()); i++) {
			ans.digits.set(i, ans.getDigit(i) - b.getDigit(i));
			ans.Normalize();
		}

		ans.clearNulls();
		return ans;
	}

	public String toString() {
		if (this.digits.size() == 1 && this.digits.get(0) == (long) 0) {
			return "0";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = this.digits.size() - 1; i > -1; i--) {
			if (Math.abs(this.digits.get(i)) != 0) {
				if (i != Math.abs(this.digits.size()) - 1) {
					long tmp = Math.abs(this.digits.get(i));
					while (tmp < this.BASE / 10) {
						sb.insert(sb.length(), "0");
						tmp *= 10;
					}
				}
				sb.insert(sb.length(), Long.toString(Math.abs(this.digits.get(i))));
			} else {
				for (int j = 0; j < this.lb; j++) {
					sb.insert(sb.length(), "0");
				}
			}
		}
		if (this.digits.get(this.digits.size() - 1) < 0) {
			sb.insert(0, "-");
		}
		return sb.toString();
	}

	Number(String a) {

		if (a.charAt(0) != '-') {
			int steps = 0;
			if (a.length() % this.lb == 0) {
				steps = a.length() / this.lb;
			} else {
				steps = a.length() / this.lb + 1;
			}
			for (int i = 0; i < steps; i++) {
				this.digits.add(Long
						.parseLong(a.substring(Math.max(0, a.length() - (i + 1) * this.lb), a.length() - i * this.lb)));
			}
		} else {
			int steps = 0;
			if ((a.length() - 1) % this.lb == 0) {
				steps = (a.length() - 1) / this.lb;
			} else {
				steps = (a.length() - 1) / this.lb + 1;
			}
			for (int i = 0; i < steps - 1; i++) {
				this.digits.add(-Long.parseLong(a.substring(Math.max(1, (a.length() - 1) - (i + 1) * this.lb + 1),
						(a.length() - 1) - i * this.lb + 1)));
			}
			this.digits.add(-Long.parseLong(a.substring(Math.max(1, (a.length() - 1) - (steps) * this.lb + 1),
					(a.length() - 1) - (steps - 1) * this.lb + 1)));

		}
	}

	public static Number multmod(Number x, Number y, Number n) {
		if (Comprasion(x, new Number(1)) == 0 || Comprasion(y, new Number(1)) == 0) {
			Number ans = multiply(x, y);
			Number arr[] = division_and_remainder(ans, n);
			return arr[1];
		}
		Number tmp1;
		Number tmp2;
		tmp1 = new Number(x);
		tmp2 = new Number(y);
		Number arr1[] = division_and_remainder(tmp1, new Number(2));
		if (comprasion_zero(arr1[1]) == 0) {
			Number tmp = multiply(new Number(2), multmod(tmp2, arr1[0], n));
			Number end[] = division_and_remainder(tmp, n);
			return end[1];
		} else {
			Number tmp = multiply(new Number(2), multmod(tmp2, arr1[0], n));
			Number finish = normsum(tmp, tmp2);
			Number res[] = division_and_remainder(finish, n);
			return res[1];
		}
	}

	public static Number powmod(Number a, Number n, Number k) {
		if (comprasion_zero(n) == 0) {
			return new Number("1");
		}
		Number arr[] = division_and_remainder(n, new Number(2));
		Number res = powmod(a, arr[0], k);
		res = multmod(res, res, k);
		if (comprasion_zero(arr[1]) == 0) {
			return res;
		}
		Number ans = multmod(a, res, k);
		return ans;
	}

}
