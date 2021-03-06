import java.nio.channels.AsynchronousSocketChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	public static void main(String[] args) {
		Test t = new Test();
		// t.test1();
		// t.test2();
		// t.test3();
		// t.test4();
		// t.test5();
		// t.t6();
		t.t7();
	}

	public void test1() {
		// 时间输出
		long startTime = System.currentTimeMillis();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println(endTime - startTime);
	}

	public void test2() {
		// 字符串格式化输出
		long time = 7943465465456L;
		System.out.println("Elapsed time: "
				+ String.format("%.3f", (time) / 1000.0 / 3600.0) + " hours");
		System.out.printf("Elapsed time:" + "%.3f" + " hours\n",
				time / 1000.0 / 3600.0);
		System.out.printf("Elapsed time:%.3f hours\n", time / 1000.0 / 3600.0);
	}

	public void test3() {
		// java string转成int
		String s = "564654";
		System.out.println(Integer.parseInt(s));
	}

	public void test4() {
		int col_index = 0;
		Pattern pattern = Pattern.compile("(\\w+)(\\(R)*(\\d)*\\)*");
		// Matcher matcher = pattern.matcher("count");
		Matcher matcher = pattern.matcher("avg(R5)");
		if (matcher.find()) {
			String cmd = matcher.group(1);
			if ((matcher.group(3)) != null)
				col_index = Integer.valueOf(matcher.group(3));
			System.out.println(cmd);
			System.out.println(col_index);
		}
	}

	public void test5() {
		Pattern pattern = Pattern
				.compile("(http://|https://){1}[\\w//.//-/:]+");
		Matcher matcher = pattern.matcher("dsdsds<http://dsds//gfgffdfd>fdf");
		StringBuffer buffer = new StringBuffer();
		while (matcher.find()) {
			buffer.append(matcher.group());
			buffer.append("\r\n");
		}
		System.out.println(buffer.toString());
	}

	public void t6() {
		double a = 3.2133535;
		// String b = new DecimalFormat("######0.00").format(a);
		// System.out.println(b);
		String c = String.valueOf(a);
		System.out.println(c);
	}

	public void t7() {
	}
}
