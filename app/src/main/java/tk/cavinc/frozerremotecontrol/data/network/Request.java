package tk.cavinc.frozerremotecontrol.data.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import tk.cavinc.frozerremotecontrol.data.managers.DataManager;
import tk.cavinc.frozerremotecontrol.data.models.DeviceControlModel;
import tk.cavinc.frozerremotecontrol.data.models.RequestReturnModel;

/**
 * Created by cav on 19.07.19.
 */

public class Request {
    private String mUrl;

    private String getRequestMessage( HttpURLConnection conn) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));
        StringBuilder x = new StringBuilder();
        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
            x.append(output);
        }
        return x.toString();
    }

    public Request(String url) {
        mUrl = url;
        if (mUrl.indexOf("http://") == -1){
            mUrl = "http://"+mUrl;
        }
    }

    // запрос данных с страницы
    public RequestReturnModel getPageData(){
        RequestReturnModel requestRes = null;
        try {
            URL url = new URL(mUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(false);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");

            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String res = getRequestMessage(conn);
                System.out.println(res);
                //storeInStorage(res); // TODO отключить после отладки
                requestRes = new RequestReturnModel(true,res);
            } else {
                RequestReturnModel res = new RequestReturnModel(false,conn.getResponseMessage());
                return res;
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return new RequestReturnModel(false,e.getLocalizedMessage());
        }
        return requestRes;
    }

    // сохраняем данные
    private void storeInStorage(String store) throws IOException {
        DataManager manager = DataManager.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String df = format.format(new Date());
        String path = manager.getStorageAppPath();
        File out = new File(path+"/"+"l"+df+".txt");
        FileWriter writer = new FileWriter(out, true);
        BufferedWriter bufferWriter = new BufferedWriter(writer);
        bufferWriter.write(store);
        bufferWriter.close();
    }

    // запрос на устройство
    public void RequestSendData(String currentTemp,String hton,String htoff,String wifiSSID){
        try {
            URL url = new URL(mUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");

            StringBuilder tokenUri=new StringBuilder("heater_time_on=");
            tokenUri.append(URLEncoder.encode(hton,"UTF-8"));
            tokenUri.append("&heater_time_off=");
            tokenUri.append(URLEncoder.encode(htoff,"UTF-8"));
            tokenUri.append("&control_temperature=");
            tokenUri.append(URLEncoder.encode(currentTemp,"UTF-8"));
            if (wifiSSID != null) {
                tokenUri.append("&ssid=");
                tokenUri.append(URLEncoder.encode(wifiSSID,"UTF-8"));
            }

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
            outputStreamWriter.write(tokenUri.toString());
            outputStreamWriter.flush();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String res = getRequestMessage(conn);
                System.out.println(res);
            } else {
                System.out.println(conn.getResponseMessage());
            }
            conn.disconnect();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
