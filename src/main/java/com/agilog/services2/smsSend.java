package com.agilog.services2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.stereotype.Service;
 
@Service
public class smsSend {
 
    private final static String apiUrl = "https://sslsms.cafe24.com/sms_sender.php";
    private final static String userAgent = "Mozilla/5.0";
    private final static String charset = "UTF-8";
    private final static boolean isTest = true;
 
    public void sendSMS(HashMap map){
        try{
  
            URL obj =new URL(apiUrl);
 
            HttpsURLConnection con= (HttpsURLConnection) obj.openConnection();
 
            con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
 
            con.setRequestProperty("Accept-Charset", charset);
 
            con.setRequestMethod("POST");
 
            con.setRequestProperty("User-Agent", userAgent);
            /*String content = map.get("resCoName")+"입니다. "
            				+map.get("resSuName")+"님, 자녀 "+map.get("resBbName")+"님의 "
            				+map.get("resDate")+"일자 예약상태가 "
            				+map.get("resActionName")+"로 변경되었습니다.";*/
            String content = map.get("resCoName")+"::"
    				+map.get("resSuName")+"::"+map.get("resBbName")+"::"
    				+map.get("resDate")+"::"
    				+map.get("resActionName")+"::"+map.get("resDoName");
            String rphone = (String) map.get("resSuPhone");
            
            String postParams = "user_id="+base64Encode("chlwltn94")
 
                                +"&secure="+base64Encode("138dbdd3d3c63b3571f7e64c3cb2bfd3")
 
                                +"&msg="+base64Encode(content)+"&sphone1="+base64Encode("010")+"&sphone2="+base64Encode("2081")+"&sphone3="+base64Encode("9036")
 
                                +"&rphone="+base64Encode(rphone) //수신자 번호
                                
                                +"&mode="+base64Encode("1")+"&smsType=S"+"&rdate"+base64Encode("")+"&rtime"+base64Encode(""); // SMS/LMS 여부
 
            System.out.println(postParams);
            //test 모드일 경우 실제로 SMS발송은 되지 않고 성공적인 호출 확인 여부만 확인 할 수 있도록 함
 
            if(isTest) {
 
                postParams+="&testflag"+base64Encode("Y");
 
            }
 
            //For POST only    - START
 
            con.setDoOutput(true);
 
            OutputStream os = con.getOutputStream();
 
            os.write(postParams.getBytes());
 
            os.flush();
 
            os.close();
 
            //For POST only - END
 
            int responseCode = con.getResponseCode();
 
            System.out.println("POST Response Code::"+responseCode);
 
            if(responseCode == HttpURLConnection.HTTP_OK){ // success
 
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
 
                String inputLine;
 
                StringBuffer buf  = new StringBuffer();
 
                while((inputLine=in.readLine())!=null){
 
                    buf.append(inputLine);
 
                }
 
                in.close();
 
                System.out.println("SMS Content : "+buf.toString());
                
            }else{
             	System.out.println("POST request not worked");
 
            }
 
        }catch(IOException ex){
 
        	System.out.println("SMS IOException:"+ex.getMessage());
        }
 
    }
 
    /**
    * BASE64 Encoder
    * @param str
    * @return
    */
    public static String base64Encode(String str)  throws java.io.IOException {
      Encoder encoder = Base64.getEncoder();
        byte[] strByte = str.getBytes("UTF-8");
        String result = encoder.encodeToString(strByte);
        return result ;
    }

    /**
      * BASE64 Decoder
      * @param str
      * @return
      */
    public static String base64Decode(String str)  throws java.io.IOException {
      Decoder decoder = Base64.getDecoder();
        byte[] strByte = decoder.decode(str);
        String result = new String(strByte);
        return result ;
    }
 
}