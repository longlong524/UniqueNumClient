package uniquenum.main;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.me.uniquenum.util.ServerBean;

import com.alibaba.fastjson.JSONObject;


/**
 * UniqueNumService Client, Thread safe.
 * @author haha
 * @
 */
public class UniqueNum{
	private static ThreadLocal<UniqueNum> locals=new ThreadLocal<UniqueNum>() {

		@Override
		protected UniqueNum initialValue() {
			return null;
		}
		
	};
	private  long start;
	private  long end;

	private static long NUM=100000;
	private static ExecutorService SINGLE=Executors.newSingleThreadExecutor();
	private static LinkedBlockingQueue<UniqueNum> queue=new LinkedBlockingQueue<UniqueNum>(1000);
	private static  List<ServerBean> urls;
	private static int index=0;
	private static int serverNum=10;
	
	private UniqueNum() {}

	/**
	 * init the uniquenum service
	 * 
	 * @param urls  
	 * server urls
	 * @param serverNum
	 * the least num of unique numbean
	 * @param NUMONCE
	 * get num once
	 */
	public static void init(final List<ServerBean> urls,int serverNum,long NUMONCE) {
		UniqueNum.urls=urls;
		UniqueNum.serverNum=serverNum;
		UniqueNum.NUM=NUMONCE;
		SINGLE.execute(new Runnable() {
			
			public void run() {
				while(true) {
					if(queue.size()<UniqueNum.serverNum) {
						ServerBean ss=UniqueNum.urls.get(index%UniqueNum.urls.size());
						try {
							UniqueNumApiUtil.getNum(NUM, ss.getUrl(),ss.getSecretKey(),queue);
						} catch (Exception e) {
							e.printStackTrace();
							index++;
						}
					}else {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}
	
	
	public static long getNext() {
		
			UniqueNum un=locals.get();
			
			if(un==null) {
				while(true) {
					try {
						un=queue.take();
						break;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				locals.set(un);
			}
			long nn=un.start++;
			if(nn<=un.end) {
				return nn;
			}else {
				while(true) {
					try {
						un=queue.take();
						break;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				locals.set(un);
				return un.start++;
			}
			
	}
	
	
	
	
	public long getStart() {
		return start;
	}


	public void setStart(long start) {
		this.start = start;
	}


	public long getEnd() {
		return end;
	}


	public void setEnd(long end) {
		this.end = end;
	}

	
	
	static class UniqueNumApiUtil {
		
	     private static CloseableHttpClient HttpClient ;
	    

	    
	    static {
	    		RequestConfig requestConfig = RequestConfig.custom()
	                .setSocketTimeout(5000)
	                .setConnectTimeout(5000).build();
	    		HttpClient = HttpClients.custom()
	    	            .setDefaultRequestConfig(requestConfig)
	    	            .build();
	    }
	    
	    
	    

	    public static void getNum(long num,String url_prefix,
	    		String scretKey,LinkedBlockingQueue<UniqueNum> queue)  throws Exception{

	        		HttpPost httpPost = new HttpPost(url_prefix);
	        		JSONObject jo=new JSONObject();
	        		jo.put("scretKey", scretKey);
	        		jo.put("num", num);
				httpPost.setEntity(new StringEntity(JSONObject.toJSONString(jo),"utf-8"));
					
				org.apache.http.client.methods.CloseableHttpResponse res=null;
	        		try {
	        			res=HttpClient.execute(httpPost);
					String str=EntityUtils.toString(res.getEntity(),"utf-8");
					JSONObject joo=JSONObject.parseObject(str);
					if(joo.getBooleanValue("success")) {
						UniqueNum un=new UniqueNum();
						un.setStart(joo.getLongValue("start"));
						un.setEnd(joo.getLongValue("end"));
						queue.put(un);
					}else {
						throw new Exception(url_prefix+":: not success!"+str);
					}
				} finally {
					if(res!=null) {
						try {
							res.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

	    }
	}

}
