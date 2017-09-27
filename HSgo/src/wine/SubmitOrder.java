package wine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class SubmitOrder extends Thread {

	public static CloseableHttpClient httpclient = null;

	static {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		// Increase max total connection to 200
		cm.setMaxTotal(1000);
		// Increase default max connection per route to 20
		cm.setDefaultMaxPerRoute(1000);
		// Increase max connections for localhost:80 to 50
		HttpHost localhost = new HttpHost("http://www.emaotai.cn", 80);
		cm.setMaxPerRoute(new HttpRoute(localhost), 1000);
		cm.setValidateAfterInactivity(5000);
		httpclient = HttpClients.custom().setConnectionManager(cm).build();
	}

	public String initialProductName = "";
	public long timeStamp = 0;// more than 700 ms than current time could submit
	public int flag = 1; // 0 order ready no run ; if = 1 , it's running, need
							// wait for change to 0,could order ;; if=2 stand
							// for initial met issue due to param or be locked
	public HttpClientContext localContext = null;

	String __VIEWSTATE = "";  //
	String radaddresstype = "taobao";
	String SubmmitOrder$txtShipTo = "饶女士";//
	String ddlRegions1 = "1944";//
	String ddlRegions2 = "2048";//
	String ddlRegions3 = "2049";//
	String regionSelectorValue = "2049";//
	String regionSelectorNull = "-请选择-";
	String SubmmitOrder$txtAddress = "二横街美苑路【和友路路口】昌盛贸易行";//
	String SubmmitOrder$txtZipcode = "514000";  //
	String SubmmitOrder$txtTelPhone = "";
	String SubmmitOrder$txtCellPhone = "18211462007"; //
	String invoiceRadio = "on";
	String shippButton = "13";
	String paymentMode = "15";
	String SubmmitOrder$tbActvityProductID = ",471";  //  String SubmmitOrder$tbActvityProductID=",481";	
//	String SubmmitOrder$tbActvityProductID=",447";	
// String SubmmitOrder$tbActiviPrice=",408.00";
	String SubmmitOrder$tbActiviPrice = ",1299.00";   // String SubmmitOrder$tbActiviPrice=",358.00";
	String SubmmitOrder$txtMessage = "";
	String SubmmitOrder$txtInvoiceTitle = "";
	String SubmmitOrder$btnCreateOrder = "确认提交";
	String SubmmitOrder$htmlCouponCode = "";
	String SubmmitOrder$inputPaymentModeId = "5";
	String SubmmitOrder$inputShippingModeId = "13";
	String SubmmitOrder$hdbuytype = "";
	String SubmmitOrder$inputInvoiceId = "14";
	String productId = "";
	String qty = "";
	String logoFile = "";
	String pingshengFile = "";
	String userLogoFile = "";
	String userPingshengFile = "";
	String dzjNum = "";
	String dzjMinNum = "";

	//public HttpGet httpgetPhone = null;
	public HttpPost httppost = null;
//	public String url = "http://www.emaotai.cn/SubmmitOrder.aspx";

	public boolean submitFlag = false;


	int submitOrderValidate(){
		
		BufferedReader theHTML = null;
		String thisLine = null;
		
		

		// CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		int count = 0;
		while (true) {
			try {
		
				response = httpclient.execute(httppost, localContext);
				System.out.println(initialProductName + "  " + System.currentTimeMillis());
				entity = response.getEntity();
				theHTML = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
			while ((thisLine = theHTML.readLine()) != null) {
					//System.out.println(thisLine);
					if(thisLine.indexOf("<script>alert")!= -1){
							System.out.println(thisLine);
							break;
					}
					if(thisLine.indexOf("moved to")!= -1){
						System.out.println(thisLine);
						break;
					}				
				}

				break;
			} catch (Exception e) {
				count++;
				e.printStackTrace();
				System.out.println(count + " " + e + " submitting order time out");
				if (count > 3)
					break;
			} finally {
				try {
					if (response != null)
						EntityUtils.consume(entity);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		return 0;

	}

	@Override
	public void run() {

		while (true) {
			long time = System.currentTimeMillis();
			if (true ) {
				System.out.println("submiting .............." + initialProductName + "  time:" + time);
			//	ProductDetail.productList.remove(initialProductName);
				submitOrderValidate();
//				if (submitFlag) {
//					submitOrderValidate();
//				} else {
//					submitOrder();
//				}
				// flag = 1;
				break;
			} /*
				 * else if(flag == 2){
				 * System.out.println("submit paramter value =2 invalid!");
				 * break; }
				 */
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
	}

}
