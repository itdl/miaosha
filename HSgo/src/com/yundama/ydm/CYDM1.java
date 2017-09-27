package com.yundama.ydm;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class CYDM1
{
	// �����ƴ���DLL http://yundama.com/apidoc/YDM_SDK.html#DLL
	// yundamaAPI 32λ, yundamaAPI-x64 64λ
	public static String	DLLPATH		= "yundamaAPI-x64";
	
	// ע����������ͨ��Ա�˺ţ����ǿ������˺ţ�ע���ַ http://www.yundama.com/index/reg/user
			// �����߿�����ϵ�ͷ���ȡ��ѵ������
	public static String username = "";
	public static String password	= "";

	// ����ʱ��ֱ��ʹ��Ĭ�ϵ����ID��Կ����Ҫ���ܿ����߷ֳɱ���ʹ���Լ������ID����Կ
	// 1. http://www.yundama.com/index/reg/developer ע�Ὺ�����˺�
	// 2. http://www.yundama.com/developer/myapp ��������
	// 3. ʹ����ӵ����ID����Կ���п��������ܷ��ֳ�
	public static int appid	= 1;									
	public static String appkey	= "";
	

	//  ����1004��ʾ4λ��ĸ���֣���ͬ�����շѲ�ͬ����׼ȷ��д������Ӱ��ʶ���ʡ��ڴ˲�ѯ�������� http://www.yundama.com/price.html
	public static int codetype = 1004;
	
	public static int timeOutPic = 30;
	
	
	public interface YDM extends Library
	{
		YDM	INSTANCE	= (YDM) Native.loadLibrary(DLLPATH, YDM.class);		

		public void YDM_SetBaseAPI(String lpBaseAPI);
		public void YDM_SetAppInfo(int nAppId, String lpAppKey);
		public int YDM_Login(String lpUserName, String lpPassWord);
		public int YDM_DecodeByPath(String lpFilePath, int nCodeType, byte[] pCodeResult);
		public int YDM_UploadByPath(String lpFilePath, int nCodeType);
		public int YDM_EasyDecodeByPath(String lpUserName, String lpPassWord, int nAppId, String lpAppKey, String lpFilePath, int nCodeType, int nTimeOut, byte[] pCodeResult);
		public int YDM_DecodeByBytes(byte[] lpBuffer, int nNumberOfBytesToRead, int nCodeType, byte[] pCodeResult);
		public int YDM_UploadByBytes(byte[] lpBuffer, int nNumberOfBytesToRead, int nCodeType);
		public int YDM_EasyDecodeByBytes(String lpUserName, String lpPassWord, int nAppId, String lpAppKey, byte[] lpBuffer, int nNumberOfBytesToRead, int nCodeType, int nTimeOut, byte[] pCodeResult);
		public int YDM_GetResult(int nCaptchaId, byte[] pCodeResult);
		public int YDM_Report(int nCaptchaId, boolean bCorrect);
		public int YDM_EasyReport(String lpUserName, String lpPassWord, int nAppId, String lpAppKey, int nCaptchaId, boolean bCorrect);
		public int YDM_GetBalance(String lpUserName, String lpPassWord);
		public int YDM_EasyGetBalance(String lpUserName, String lpPassWord, int nAppId, String lpAppKey);
		public int YDM_SetTimeOut(int nTimeOut);
		public int YDM_Reg(String lpUserName, String lpPassWord, String lpEmail, String lpMobile, String lpQQUin);
		public int YDM_EasyReg(int nAppId, String lpAppKey, String lpUserName, String lpPassWord, String lpEmail, String lpMobile, String lpQQUin);
		public int YDM_Pay(String lpUserName, String lpPassWord, String lpCard);
		public int YDM_EasyPay(String lpUserName, String lpPassWord, long nAppId, String lpAppKey, String lpCard);
	}
	
	
	public static void main(String[] args) throws Exception
	{
	
		
		// ͼƬ·��
		String	imagepath	= "1907721076.png";

		
		// ֻ��Ҫ�ڳ�ʼ��ʱ���½һ��
	
			System.out.println("�����ύʶ��...");	
			byte[] byteResult = new byte[30];
			int cid = YDM.INSTANCE.YDM_EasyDecodeByPath(CYDM1.username, CYDM1.password, CYDM1.appid, CYDM1.appkey, imagepath, CYDM1.codetype, CYDM1.timeOutPic, byteResult);
			Thread.sleep(1000);
			System.out.println("ʶ�𷵻ش���:" + cid);
			if(cid < 0){
				 int reportWrongcodeid = YDM.INSTANCE.YDM_Report(cid, false);
			}else{
				String strResult = new String(byteResult, "UTF-8").trim();
				System.out.println("ʶ�𷵻ؽ��:" + strResult); 

			}
			
			// ������������������ѯ http://www.yundama.com/apidoc/YDM_ErrorCode.html
			
			

		
	}
}
