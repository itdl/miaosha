package wine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class ProductDetail extends TimerTask {

	
	
	public HttpClientContext localContext = null;
	int flag = 1; //0 order ready no run  ; if = 1 , it's running, need wait for change to 0,could order
	public SubmitOrder submitOrder = null;
	public static Map<String,SubmitOrder> productList = new ConcurrentHashMap <String,SubmitOrder> ();
	public String userid;
	
	public String SubmmitOrder$txtShipTo =null;
	public String ddlRegions1=null;
	public String ddlRegions2=null;
	public String ddlRegions3=null;
	public String regionSelectorValue=null;
	public String SubmmitOrder$txtAddress=null;
	public String SubmmitOrder$txtZipcode=null;
	public String SubmmitOrder$txtCellPhone=null;
	public String SubmmitOrder$inputInvoiceId=null;
	
	 
	public ProductDetail(HttpClientContext localContext, String userid){
		this.localContext = localContext;
		this.userid = userid;
	}


	public SubmitOrder initOrderStatus(){
		SubmitOrder order = new SubmitOrder();
		order.localContext = this.localContext;
		
		BufferedReader theHTML = null;
		String thisLine = null;

		String url = "http://www.emaotai.cn/SubmmitOrder.aspx";
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		  CloseableHttpResponse response = null;
		  HttpPost httppost = new HttpPost("http://www.emaotai.cn/SubmmitOrder.aspx");
		  HttpEntity entity = null;
	        try {
	            // Create a local instance of cookie store
	            HttpGet httpget = new HttpGet(url);
	            httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
				httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
				httpget.setHeader("Cache-Control", "max-age=0");
				httpget.setHeader("Connection", "keep-alive");
				httpget.setHeader("Content-Type", "application/x-www-form-urlencoded");
				httpget.setHeader("Upgrade-Insecure-Requests", "1");
					
	   //         System.out.println("Executing request " + httpget.getRequestLine());
	            // Pass local context as a parameter
				
	             response = httpclient.execute(httpget, localContext);
	             entity = response.getEntity();  
	             theHTML = new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"));

				while ((thisLine = theHTML.readLine()) != null) {
						if(thisLine.indexOf("__VIEWSTATE") != -1)
						{
						order.__VIEWSTATE = thisLine.substring(thisLine.indexOf("__VIEWSTATE\" value=")+20 ,thisLine.indexOf("\" />"));
							System.out.println(userid+ " __VIEWSTATE: " +order.__VIEWSTATE);	
							break;
						}
				}
				
				if(SubmmitOrder$txtShipTo !=null ){
					order.SubmmitOrder$txtShipTo = this.SubmmitOrder$txtShipTo;
					order.ddlRegions1 = this.ddlRegions1;
					order.ddlRegions2 = this.ddlRegions2;
					order.ddlRegions3 = this.ddlRegions3;
					order.regionSelectorValue = this.regionSelectorValue;
					order.SubmmitOrder$txtAddress = this.SubmmitOrder$txtAddress;
					order.SubmmitOrder$txtZipcode = this.SubmmitOrder$txtZipcode;
					order.SubmmitOrder$txtCellPhone = this.SubmmitOrder$txtCellPhone;
					order.SubmmitOrder$inputInvoiceId = this.SubmmitOrder$inputInvoiceId;
				}
				
			
				 List <NameValuePair> list = new ArrayList<NameValuePair>();
			    	list.add(new BasicNameValuePair("__VIEWSTATE", order.__VIEWSTATE));
			    	list.add(new BasicNameValuePair("radaddresstype", order.radaddresstype));
			    	list.add(new BasicNameValuePair("SubmmitOrder$txtShipTo",order.SubmmitOrder$txtShipTo));
			    	list.add(new BasicNameValuePair("ddlRegions1",order.ddlRegions1));
					list.add(new BasicNameValuePair("ddlRegions2",order.ddlRegions2));
					list.add(new BasicNameValuePair("ddlRegions3",order.ddlRegions3));
					list.add(new BasicNameValuePair("regionSelectorValue",order.regionSelectorValue));
					list.add(new BasicNameValuePair("regionSelectorNull",order.regionSelectorNull));
					list.add(new BasicNameValuePair("SubmmitOrder$txtAddress",order.SubmmitOrder$txtAddress));
					list.add(new BasicNameValuePair("SubmmitOrder$txtZipcode",order.SubmmitOrder$txtZipcode));
					list.add(new BasicNameValuePair("SubmmitOrder$txtTelPhone",order.SubmmitOrder$txtTelPhone));	
					list.add(new BasicNameValuePair("SubmmitOrder$txtCellPhone",order.SubmmitOrder$txtCellPhone));
					list.add(new BasicNameValuePair("invoiceRadio",order.invoiceRadio));
					list.add(new BasicNameValuePair("shippButton",order.shippButton));
					list.add(new BasicNameValuePair("paymentMode",order.paymentMode));
					list.add(new BasicNameValuePair("SubmmitOrder$tbActvityProductID",order.SubmmitOrder$tbActvityProductID));	
					list.add(new BasicNameValuePair("SubmmitOrder$tbActiviPrice",order.SubmmitOrder$tbActiviPrice));
					list.add(new BasicNameValuePair("SubmmitOrder$txtMessage",order.SubmmitOrder$txtMessage));
					list.add(new BasicNameValuePair("SubmmitOrder$txtInvoiceTitle",order.SubmmitOrder$txtInvoiceTitle));
					list.add(new BasicNameValuePair("SubmmitOrder$btnCreateOrder",order.SubmmitOrder$btnCreateOrder));
					list.add(new BasicNameValuePair("SubmmitOrder$htmlCouponCode",order.SubmmitOrder$htmlCouponCode));
					list.add(new BasicNameValuePair("SubmmitOrder$inputPaymentModeId",order.SubmmitOrder$inputPaymentModeId));
					list.add(new BasicNameValuePair("SubmmitOrder$inputShippingModeId",order.SubmmitOrder$inputShippingModeId));
					list.add(new BasicNameValuePair("SubmmitOrder$hdbuytype",order.SubmmitOrder$hdbuytype));
					list.add(new BasicNameValuePair("SubmmitOrder$inputInvoiceId",order.SubmmitOrder$inputInvoiceId));
					list.add(new BasicNameValuePair("productId",order.productId));
					list.add(new BasicNameValuePair("qty",order.qty));
					list.add(new BasicNameValuePair("logoFile",order.logoFile));
					list.add(new BasicNameValuePair("pingshengFile",order.pingshengFile));
					list.add(new BasicNameValuePair("userLogoFile",order.userLogoFile));
					list.add(new BasicNameValuePair("userPingshengFile",order.userPingshengFile));
					list.add(new BasicNameValuePair("dzjNum",order.dzjNum));
					list.add(new BasicNameValuePair("dzjMinNum",order.dzjMinNum));
			    	UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(list, Consts.UTF_8);

			  
				 
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
				  RequestConfig reqConfig =	RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).setConnectionRequestTimeout(10000).build();
				  httppost.setConfig(reqConfig);
			
				
		
	        }catch(Exception e) {
			       e.printStackTrace();
			       return null;
			} finally {
			 	try {
			 		if(response!=null)
			 			EntityUtils.consume(entity);
			 		if(httpclient!=null)
				httpclient.close();
			 }
			     catch (IOException e) {
					e.printStackTrace();
			}
		 }
	 	  		
	 
			order.timeStamp = new java.util.Date().getTime();
			order.setPriority(Thread.MAX_PRIORITY);
			order.initialProductName = Thread.currentThread().getName();
			order.httppost = httppost;
			order.flag = 0;

	      return order;  		
	}
	


	
	
	public void run() {
	
	   while(true){
		   
		  submitOrder = null;
		 
		  List<Cookie>  cookies = localContext.getCookieStore().getCookies();
          for (int i = 0; i < cookies.size(); i++) {
        	//  if("jiyou.biz.11185.cn".equals(cookies.get(i).getDomain())){
	        	//	  productList.remove(Thread.currentThread().getName());
	        		  submitOrder = initOrderStatus();
	        		  if(submitOrder != null && submitOrder.flag == 0){
	        			  productList.put(Thread.currentThread().getName(), submitOrder);
	        			  break;
	        		  }
//	        		  if(submitOrder != null && submitOrder.flag == 2){
//	        			  productList.put(Thread.currentThread().getName(), submitOrder);
//	        			  continue;
//	        		  }
        		 
        	//  }
          }	
          if((submitOrder != null) && ((submitOrder.flag ==0) /* || (submitOrder.flag ==2)*/))
        	  break;
     //     System.out.println(Thread.currentThread().getName());
       
          try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   }
	}
}
