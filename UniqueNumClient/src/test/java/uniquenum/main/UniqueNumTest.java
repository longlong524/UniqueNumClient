package uniquenum.main;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.me.uniquenum.util.ServerBean;


public class UniqueNumTest{
	static String url="http://localhost:5764/getNum";
	static String scret="kkf";
	
	@org.junit.Test
	public  void Test() {
		long t1=System.currentTimeMillis();

		Set<Long> set=new HashSet<Long>();
		LinkedList<ServerBean> sss=new LinkedList<ServerBean>();
		sss.add(new ServerBean(url, scret));

		UniqueNum.init(sss, 10, 1000000);
		for(int i=0;i<800000;i++) {
			long tt=UniqueNum.getNext();
			if(i==0) {
				t1=System.currentTimeMillis();
			}
			if(set.contains(tt)) {
				System.err.println("nn::"+tt);
				System.exit(0);
			}
			set.add(tt);
			
			System.err.println(tt);
		}
		long t2=System.currentTimeMillis();
		System.err.println(t2-t1);
	}
	
	@org.junit.Test
	public  void Test2() {
		long xx=0;
		for(int i=0;i<1000000;i++) {
			long tt=xx++;
			
			//System.err.println(tt);
		}
	}
	
	
}
