package com.bonc;

import org.junit.Test;

public class MyTest {
	
	@Test
  public void name() {
	Integer a = 5;
	Integer b = 5;
	Integer c = 300;
	Integer d = 300;
	Integer e = new Integer(5);
	Integer f = new Integer(5);
	System.out.println(a==b);
	System.out.println(c==d);
	System.out.println(e==f);
	System.out.println(e==a);
}
	@Test
	public void len () {
		System.out.println("欢迎来到山地公园省，畅游多彩贵州！理性消费，警惕低价旅游陷阱；安全出行，科学购买旅游保险；依法维权，旅游投诉和咨询请拨打旅游服务热线96972。点击 http://t.gog.cn/JTsv7a 参加“多彩贵州满意旅游痛客行”活动，百万大奖等着您。更多优惠信息请关注官方网站、微博、微信。【贵州省旅游发展委员会】".length());
	}
}
