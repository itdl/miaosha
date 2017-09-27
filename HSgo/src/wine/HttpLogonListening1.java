package wine;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.yundama.ydm.CYDM1;
import com.yundama.ydm.CYDM1.YDM;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class HttpLogonListening1 implements Runnable {
	
	
	public static CloseableHttpClient httpclient = null;

	static{
	PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		// Increase max total connection to 200
		cm.setMaxTotal(200);
		// Increase default max connection per route to 20
		cm.setDefaultMaxPerRoute(200);
		// Increase max connections for localhost:80 to 50
		HttpHost localhost = new HttpHost("https://passport.11185.cn:8001");
		cm.setMaxPerRoute(new HttpRoute(localhost), 200);
		cm.setValidateAfterInactivity(5000);
		httpclient = HttpClients.custom()
				.setConnectionManager(cm).setRetryHandler(new DefaultHttpRequestRetryHandler()).build();
	}
	
	
	public  String username = "";
	public  String password = "";
	public  String lt = "";
	public  String execution = "";
	public  String _eventId = "submit";
	public  String picName = "";
	public  int id = 0;
	

	public  String code = null;
	public HttpClientContext localContext = null;
	

	
	
//	public CookieStore cookieStore = null;

	
	public HttpLogonListening1(String username , String password,String picName, int id){
//		try {
//			this.username = URLEncoder.encode(username ,"UTF-8");
//			this.password = URLEncoder.encode(password ,"UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		};
		this.username = username;
		this.password = password;
		this.picName = picName;
		this.id = id;
		setCookiePoliy();
	}
	
	HttpLogonListening1(){
		setCookiePoliy();
	}
	
	protected void setCookiePoliy(){
		  // Create local HTTP context
		  this.localContext = HttpClientContext.create();
		 
		  CookieStore cookieStore = new BasicCookieStore();
          // Bind custom cookie store to the local context
//		  HttpClientContext clientContext = HttpClientContext.adapt(localContext);
          localContext.setCookieStore(cookieStore);
	}
	
	public void saveToFile(String name) {
		
		String destUrl = "http://www.emaotai.cn/VerifyCodeImage.aspx";
		//  CloseableHttpClient httpclient = HttpClients.createDefault();
		  CloseableHttpResponse response = null;
		  int BUFFER_SIZE = 1024;
		  FileOutputStream fos = null;
		  BufferedInputStream bis = null;
		  HttpGet httpget = null;
		   int size = 0;
			byte[] buf = new byte[BUFFER_SIZE];
	        try {
	            // Create a local instance of cookie store
	             httpget = new HttpGet(destUrl);
	             response = httpclient.execute(httpget, localContext);
	             HttpEntity entity = response.getEntity();  
	             bis = new BufferedInputStream(entity.getContent());
	 			fos = new FileOutputStream(name);
	 			while ((size = bis.read(buf)) != -1) {
	 				fos.write(buf, 0, size);
	 			}
	 			fos.flush();
	        } catch (IOException e) {
				e.printStackTrace();
			} catch (ClassCastException e) {
				e.printStackTrace();
			} finally {
				try {
					if(fos!=null)
					fos.close();
					if(bis!=null)
					bis.close();
					//httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}	
	}
	
	
//	public String getCode(String name) throws Exception {
//		saveToFile(name);
//		System.out.println("正在登录识别...");	
//		byte[] byteResult = new byte[30];
//		int cid = YDM.INSTANCE.YDM_EasyDecodeByPath(CYDM1.username, CYDM1.password, CYDM1.appid, CYDM1.appkey, name, CYDM1.codetype, CYDM1.timeOutPic, byteResult);
////		System.out.println("识别返回代码:" + cid);
//		if(cid < 0){
//			 YDM.INSTANCE.YDM_Report(cid, false);
//		}else{
//			String strResult = new String(byteResult, "UTF-8").trim();
//			return strResult;
//		}
//		return null;
//	}

	public  String getCode(String name) throws Exception {
		// ImageIO.scanForPlugins(); // for server environment
		saveToFile(name);
		File imageFile = new File(name);

		ITesseract instance = new Tesseract(); // JNA Interface Mapping

		try {
			String result = instance.doOCR(imageFile);
			 String b = "";
				for(char temp : result.toCharArray()){
					if(temp == ' '){
						continue;
					}
					b += temp;
				}
			System.out.println(b);
			//int a = Integer.parseInt(b.trim());
			return b;
		} catch (TesseractException e) {	
			System.err.println(e.getMessage());
		}catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;

	}
	

	public int logon(int prodid, String filePic) {
		
		String thisLine;
	    String logonURL = "";
		String url = "http://www.emaotai.cn/login.aspx";
		System.out.println("1:" + url);
		BufferedReader theHTML = null;
	
		//  CloseableHttpClient httpclient = HttpClients.createDefault();
		  CloseableHttpResponse response = null;
		  HttpEntity entity = null;
	        try {
	            // Create a local instance of cookie store
	            HttpGet httpget = new HttpGet(url);
	            
	            httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
				httpget.setHeader("Accept-Encoding", "gzip, deflate, sdch");
				httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
				httpget.setHeader("Cache-Control", "no-cache");
				httpget.setHeader("Connection", "keep-alive");
				httpget.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.154 Safari/537.36 LBBROWSER");
				
		   //         System.out.println("Executing request " + httpget.getRequestLine());
	            // Pass local context as a parameter
				RequestConfig  reqConfig =	RequestConfig.custom().setSocketTimeout(33000).setConnectTimeout(33000).setConnectionRequestTimeout(33000).build();
				httpget.setConfig(reqConfig);
	             response = httpclient.execute(httpget, localContext);
	             entity = response.getEntity();  
	              theHTML = new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"));
	             
	              while ((thisLine = theHTML.readLine()) != null) {
	    
	      				if(thisLine.indexOf("__VIEWSTATE\" value=")!=-1){
	      					  lt = thisLine.substring(thisLine.indexOf("value=")+7, thisLine.indexOf("\" />"));
	      				//	lt = URLEncoder.encode(lt ,"UTF-8");
	      					  System.out.println(lt);
	      				}
	      			}
	  

			      	saveToFile(filePic);
					System.out.println("正在登录识别...");	
					byte[] byteResult = new byte[30];
					int cid = YDM.INSTANCE.YDM_EasyDecodeByPath(CYDM1.username, CYDM1.password, CYDM1.appid, CYDM1.appkey, filePic, CYDM1.codetype, CYDM1.timeOutPic, byteResult);
//					System.out.println("识别返回代码:" + cid);
					if(cid < 0){
						 YDM.INSTANCE.YDM_Report(cid, false);
						 return -1;
					}else{
						String strResult = new String(byteResult, "UTF-8").trim();
						this.code = strResult;
					}  
					System.out.println("code: " + code);
					
//					__LASTFOCUS:
//						__VIEWSTATE:/wEPDwULLTE3MzA1ODMyMTVkZA==
//						__EVENTTARGET:
//						__EVENTARGUMENT:
//						login$txtUserName:芬外美丽
//						login$txtPassword:520520
//						login$txtCode:3qs3
//						login$txtMark:0
//						login$btnLogin:立即登录
//						login$txtIsYZPhone:0
					
					
				    List <NameValuePair> list = new ArrayList<NameValuePair>();
			    	list.add(new BasicNameValuePair("__LASTFOCUS", ""));
			    	list.add(new BasicNameValuePair("__VIEWSTATE", lt));
			    	list.add(new BasicNameValuePair("__EVENTTARGET",""));
			    	list.add(new BasicNameValuePair("__EVENTARGUMENT",""));
			    	list.add(new BasicNameValuePair("login$txtUserName",this.username));
			    	list.add(new BasicNameValuePair("login$txtPassword",this.password));  
			    	list.add(new BasicNameValuePair("login$txtCode",this.code));
			    	list.add(new BasicNameValuePair("login$txtMark","0"));  
			    	list.add(new BasicNameValuePair("login$btnLogin","立即登录"));
			    	list.add(new BasicNameValuePair("login$txtIsYZPhone","0"));  
			    	UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(list, Consts.UTF_8);
				    
				  System.out.println("2: " + logonURL);
			  
				  HttpPost httppost = new HttpPost(url);
				  httppost.setEntity(entity1);

				  httppost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
				  httppost.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
				  httppost.setHeader("Cache-Control", "max-age=0");
				  httppost.setHeader("Connection", "keep-alive");
				  httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
				  httppost.setHeader("Accept-Encoding", "gzip, deflate");
				  httppost.setHeader("Host","www.emaotai.cn");
				  httppost.setHeader("Origin","http://www.emaotai.cn");
				  httppost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36"); 
				
					 response = httpclient.execute(httppost,localContext);
		              entity = response.getEntity();  
		              theHTML = new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"));	
		              if ((thisLine = theHTML.readLine()) != null) {
		            	  if(thisLine.indexOf("moved") != -1){
		            	     List<Cookie>  cookies = localContext.getCookieStore().getCookies();
				              for (int i = 0; i < cookies.size(); i++) {
				            	  System.out.println(cookies.get(i).getDomain());
				            	  System.out.println(cookies.get(i).getValue());
				            	 if("www.emaotai.cn".equals(cookies.get(i).getDomain())){
									 System.out.println(this.username +"logon successfully");
							       	return 1;
								 }	 
				              }	
		            	  }else{
		            		  int reportWrongcodeid = YDM.INSTANCE.YDM_Report(cid, false);
		    		    	  System.out.println(username + " Submit wrong validate for pic" + reportWrongcodeid);
		    		    	  return -1;		
		            	  }
		            
			           }         
		      		
 								
			
	        }catch(StringIndexOutOfBoundsException e) {
	        	 List<Cookie>  cookies = localContext.getCookieStore().getCookies();
	              for (int i = 0; i < cookies.size(); i++) {
	                  System.out.println("Local cookie: " + cookies.get(i));
	              }	
			    System.out.println(this.username +" :logon successfully");	
	        	return 1;
		    }catch(Exception e) {
			       System.out.println(e);
			       return -1;
		    }
	        finally {
			 	try {
			 	if(response!=null)
			 		EntityUtils.consume(entity);
			 	}catch (IOException e) {
					e.printStackTrace();
				}
		 }
			return -1;
	}

	
	@Override
	public void run() {
	//	logon(id, picName);
		while( true){
			
			if(logon(id, picName) ==1){
				
				break;
			}			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
	}
	
	public static void main(String[] args){
//		HttpLogonListening1 a = 	 new HttpLogonListening1("芬外美丽","520520","10.gif",29842); //ok
//		new Thread(a).start();
//		
//		HttpLogonListening1 b = 	 new HttpLogonListening1("老詹","zcl070718","19.gif",29842); //ok
//		new Thread(b).start();
//		
//		HttpLogonListening1 c = 	 new HttpLogonListening1("黄瑞梅","520520","29.gif",29842); // ok
//		new Thread(c).start();
//
//		HttpLogonListening1 d = 	 new HttpLogonListening1("阿朱","zcl070718","39.gif",29842); //ok
//		new Thread(d).start();
//		
//		HttpLogonListening1 f = 	 new HttpLogonListening1("Rosefinch001","520520","39.gif",29842); //  ok
//		new Thread(f).start();
//		
////		HttpLogonListening a1 = new HttpLogonListening("CM811183309","10131023","39.gif",29842);
////		a1.run();
//		
//		
//		HttpLogonListening1 a2 = new HttpLogonListening1("CM652023230","10131023","39.gif",29842); // ok
//		new Thread(a2).start();
		
//		HttpLogonListening a3 = new HttpLogonListening("Rosefinch","520520","39.gif",29842);
//		a3.run();

		HttpLogonListening1 a4 = new HttpLogonListening1("Rosefinch003","520520","39.gif",29842);
		new Thread(a4).start();
		
		HttpLogonListening1 a5 = new HttpLogonListening1("Rosefinch004","520520","59.gif",29842);
		a5.run();
			
		
//		ProductDetail pd7 = new ProductDetail(a3.localContext,a3.username);
//		pd7.SubmmitOrder$txtShipTo = "陈先生";
//		pd7.ddlRegions1 = "1944";
//		pd7.ddlRegions2 = "2048";
//		pd7.ddlRegions3 = "2049";
//		pd7.regionSelectorValue = "2049";
//		pd7.SubmmitOrder$txtAddress = "二横街美苑路汉兴炖品村";
//		pd7.SubmmitOrder$txtZipcode = "514000";
//		pd7.SubmmitOrder$txtCellPhone = "15914925861";
//		pd7.SubmmitOrder$inputInvoiceId = "118";
//		new Thread(pd7,a3.username).start();
	
		ProductDetail pd8 = new ProductDetail(a4.localContext,a4.username);
		pd8.SubmmitOrder$txtShipTo = "张思伟";
		pd8.ddlRegions1 = "1944";
		pd8.ddlRegions2 = "2048";
		pd8.ddlRegions3 = "2049";
		pd8.regionSelectorValue = "2049";
		pd8.SubmmitOrder$txtAddress = "二横街美苑路水星家纺旁";
		pd8.SubmmitOrder$txtZipcode = "514000";
		pd8.SubmmitOrder$txtCellPhone = "13411201782";
		pd8.SubmmitOrder$inputInvoiceId = "108";
		new Thread(pd8,a4.username).start();
		
		ProductDetail pd9 = new ProductDetail(a5.localContext,a5.username);
		pd9.SubmmitOrder$txtShipTo = "王敏";
		pd9.ddlRegions1 = "1944";
		pd9.ddlRegions2 = "2048";
		pd9.ddlRegions3 = "2049";
		pd9.regionSelectorValue = "2049";
		pd9.SubmmitOrder$txtAddress = "二横街美苑路水星家纺";
		pd9.SubmmitOrder$txtZipcode = "514000";
		pd9.SubmmitOrder$txtCellPhone = "13825954415";
		pd9.SubmmitOrder$inputInvoiceId = "108";
		new Thread(pd8,a5.username).start();
		
		
		  SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		  java.util.Date fixTime = null;


			try {
				fixTime = dateFormat.parse("2017-04-13 14:59:00");
			} catch (ParseException e) {
			}
			
	
		
		while (true) {
		//	System.out.println(ProductDetail.productList.size());
		 if(System.currentTimeMillis() >= fixTime.getTime()){
			for (Map.Entry<String, SubmitOrder> entry : ProductDetail.productList
					.entrySet()) {
					cachedThreadPool.execute(entry.getValue());
			}		
		//	java.awt.Toolkit.getDefaultToolkit().beep();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		 }
		}
	}
	public static ExecutorService cachedThreadPool= Executors.newCachedThreadPool();
	
}