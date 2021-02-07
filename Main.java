import java.util.Scanner;

public class Main {
	static Number[] gcdext(Number a, Number b, Number x, Number y) {
		Number res[] = new Number[2];
		if (Number.comprasion_zero(b) == 0) {
			res[0] = new Number(1);
			res[1] = new Number(0);
			return res;
		}
		Number tmp[] = Number.division_and_remainder(a, b);
		res = gcdext(b, tmp[1], x, y);
		Number s = res[1];
		res[1] = Number.normdifference(res[0], Number.sign_multiply(tmp[0], res[1]));
		res[0] = s;
		return res;
	}

	public static Number gcd(Number a, Number b) {
		while (Number.comprasion_zero(b) != 0) {
			Number arr[] = Number.division_and_remainder(a, b);Number tmp = arr[1];
			a = b;
			b = tmp;
		}
		return a;

	}

	public static long multmod(long x, long y, long n) {
		if (x == 1 || y == 1) {
			return (x * y) % n;
		}
		long tmp1 = Math.max(x, y);
		long tmp2 = Math.min(x, y);
		if (tmp1 % 2 == 0) {

			return (2 * (multmod(tmp2, tmp1 / 2, n)) % n);
		}
		return (tmp2 + 2 * (multmod(tmp2, tmp1 / 2, n))) % n;
	}

	public static long powmod(long a, long n, long k) {
		if (n == 0)
			return 1;
		long res = powmod(a, n / 2, k);
		res = multmod(res, res, k);
		if (n % 2 == 0) {
			return res;
		}
		return multmod(a, res, k);
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		Number arr[] = { new Number("618970019642690137449562111"), new Number("19175002942688032928599"),
				new Number("1066340417491710595814572169"), new Number("11111111111111111111111"),
				new Number("900900900900990990990991"), new Number("265252859812191058636308479999999"),
				new Number("8683317618811886495518194401279999999"),
				new Number("123426017006182806728593424683999798008235734137469123231828679"),
		};
		int l = (int) (Math.random() * 8);
		int m = 0;
		while (m == l) {
			m = (int) (Math.random() * 8);
		}
		Number p = arr[l];
		Number q = arr[m];
		Number n = Number.multiply(p, q);
		Number phi = Number.multiply(Number.normdifference(p, new Number(1)), Number.normdifference(q, new Number(1)));
		System.out.println("p=" + p.toString());
		System.out.println("q=" + q.toString());
		System.out.println("n=" + n.toString());
		System.out.println("phi=" + phi.toString());
		Number e = arr[(int) (Math.random() * 8)];
		while (Number.Comprasion(gcd(e, phi), new Number(1)) != 0) {
			e = arr[(int) (Math.random() * 8)];
		}
		System.out.println("e=" + e.toString());
		Number x = new Number(-1);
		Number y = new Number(-1);
		Number res[] = gcdext(e, phi, x, y);
		Number d = res[0];
		while (Number.Comprasion(d, new Number(0)) <= 0) {
			d = Number.normsum(d, phi);
		}
		System.out.println("d=" + d.toString());
		Number mes = new Number(sc.nextLine());
		Number mes1 = Number.powmod(mes, e, n);
		System.out.print(mes1.toString() + " ");
		Number mes2 = Number.powmod(mes1, d, n);
		System.out.println(mes2.toString());
		sc.close();
	}
}

