package com.datappsinfotech.gigadocs.utils.services;

import android.support.annotation.NonNull;
import android.util.Log;

import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigadocsConstants;
import com.datappsinfotech.gigadocs.utils.exception.GigadocsDataUnavailableException;
import com.datappsinfotech.gigadocs.utils.exception.GigadocsTimeOutException;
import com.datappsinfotech.gigadocs.utils.exception.GigadocsUnAutorisedException;

import org.apache.commons.io.IOUtils;

import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Guru on 15-Jun-16.
 */
public class ServiceClass {



    private interface ServiceClassConstant {
        String POST = "POST";
        String GET = "GET";
        String PUT = "PUT";
        String HTTP = "http";
        String CONTENT_TYPE = "Content-Type";
        String ACCEPT = "Accept";
        String ACCEPT_ENCODING = "Accept-Encoding";
        String GZIP = "gzip";
        String DEFLATE = "deflate";
        String APPLICATION_JSON = "application/json";
        String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
        String UTF_8 = "UTF-8";
        String UTF_64 = "UTF-64";
        String RESPONSE = "RESPONSE";

        String CONNECTION = "Connection";
        String KEEP_ALIVE = "Keep-Alive";
        String CACHE_CONTROL = "Cache-Control";
        String NO_CACHE = "no-cache";
        String CHARSET = "charset";
        String CONTENT_LENGTH = "Content-Length";
        String COOKIE = "Cookie";

        int TIME_OUT = 15000;
    }


    public String getPostDataString(@NonNull HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        Log.i(GigadocsConstants.GIGADOCS,params.toString());
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), ServiceClassConstant.UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), ServiceClassConstant.UTF_8));
            Log.i(GigadocsConstants.GIGADOCS,result.toString());
        }
        Log.i(GigadocsConstants.GIGADOCS,result.toString());
        return result.toString();
    }

    public String postDataString(@NonNull String url, HashMap<String, String> postMap) throws GigadocsTimeOutException, GigadocsUnAutorisedException, GigadocsDataUnavailableException {
        Log.i(GigadocsConstants.GIGADOCS, ""  + Calendar.getInstance().getTime());
        Log.i(GigadocsConstants.GIGADOCS,postMap.toString());
        String output = null;
        URL targetUrl;
        HttpURLConnection httpConnection  = null;
        try {
            targetUrl = new URL(url);
            Log.i(GigadocsConstants.GIGADOCS,url);
            httpConnection  = (HttpURLConnection) targetUrl.openConnection();
            httpConnection.setRequestMethod(ServiceClassConstant.POST);
            httpConnection.setRequestProperty(ServiceClassConstant.CONTENT_TYPE, ServiceClassConstant.APPLICATION_X_WWW_FORM_URLENCODED);
            httpConnection.setRequestProperty(ServiceClassConstant.CACHE_CONTROL, ServiceClassConstant.NO_CACHE);
            httpConnection.setRequestProperty(ServiceClassConstant.CONNECTION, ServiceClassConstant.KEEP_ALIVE);
            httpConnection.setRequestProperty(ServiceClassConstant.CHARSET, ServiceClassConstant.UTF_8);
            Log.i(GigadocsConstants.GIGADOCS,"Done");
            String urlEncodeParamString = getPostDataString(postMap);
            Log.i(GigadocsConstants.GIGADOCS,"Done");
            httpConnection.setRequestProperty(ServiceClassConstant.CONTENT_LENGTH, String.format(Locale.ENGLISH,"%d", urlEncodeParamString.length()));
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            httpConnection.setUseCaches(false);
            httpConnection.setReadTimeout(ServiceClassConstant.TIME_OUT);
            httpConnection.setConnectTimeout(ServiceClassConstant.TIME_OUT);

            DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
            wr.writeBytes(urlEncodeParamString);
            wr.flush();
            wr.close();
            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                output = IOUtils.toString(httpConnection.getInputStream());

            }else if (httpConnection.getResponseCode()==HttpURLConnection.HTTP_UNAUTHORIZED){
                throw  new GigadocsUnAutorisedException(String.format("%s", IOUtils.toString(httpConnection.getErrorStream())));
            }else {
                throw new GigadocsDataUnavailableException(String.format("%s", IOUtils.toString(httpConnection.getErrorStream())));
            }
            Log.i(GigadocsConstants.GIGADOCS,""+Calendar.getInstance().getTime());

            if (output != null) {
                Log.i(GigadocsConstants.GIGADOCS,output);
            }
        } catch (MalformedURLException e) {
            throw new GigadocsTimeOutException(e.getMessage());

        } catch (Throwable t) {
            if (t instanceof GigadocsDataUnavailableException){
                throw  new GigadocsDataUnavailableException(t.getMessage());
            }else if (t instanceof GigadocsUnAutorisedException){
                throw new GigadocsUnAutorisedException(t.getMessage());
            }else{
                t.printStackTrace();
            }
        }finally {
            assert httpConnection != null;
                    httpConnection.disconnect();
        }
        return output;
    }
    public String postDataWithHeaderString(@NonNull String url, String json) throws GigadocsTimeOutException, GigadocsUnAutorisedException, GigadocsDataUnavailableException {
        Log.i(GigadocsConstants.GIGADOCS, ""  + Calendar.getInstance().getTime());
        Log.i(GigadocsConstants.GIGADOCS, ""  + json);

        String output = null;
        URL targetUrl;
        HttpURLConnection httpConnection  = null;
        try {
            targetUrl = new URL(url);
            Log.i(GigadocsConstants.GIGADOCS,url);
            httpConnection  = (HttpURLConnection) targetUrl.openConnection();
            httpConnection.setRequestMethod(ServiceClassConstant.POST);
            httpConnection.setRequestProperty(ServiceClassConstant.CACHE_CONTROL, ServiceClassConstant.NO_CACHE);
            httpConnection.setRequestProperty(ServiceClassConstant.CONNECTION, ServiceClassConstant.KEEP_ALIVE);
            httpConnection.setRequestProperty(ServiceClassConstant.CHARSET, ServiceClassConstant.UTF_8);

            httpConnection.setRequestProperty(ServiceClassConstant.CONTENT_TYPE, ServiceClassConstant.APPLICATION_JSON);

            httpConnection.setRequestProperty(ServiceClassConstant.CONTENT_LENGTH, String.format(Locale.ENGLISH,"%d", json.length()));
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            httpConnection.setUseCaches(false);
            httpConnection.setReadTimeout(ServiceClassConstant.TIME_OUT);
            httpConnection.setConnectTimeout(ServiceClassConstant.TIME_OUT);

            DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
            wr.writeBytes(json);
            wr.flush();
            wr.close();
            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                output = IOUtils.toString(httpConnection.getInputStream());

            }else if (httpConnection.getResponseCode()==HttpURLConnection.HTTP_UNAUTHORIZED){
                throw  new GigadocsUnAutorisedException(String.format("%s", IOUtils.toString(httpConnection.getErrorStream())));
            }else {
                throw new GigadocsDataUnavailableException(String.format("%s", IOUtils.toString(httpConnection.getErrorStream())));
            }
            Log.i(GigadocsConstants.GIGADOCS,""+Calendar.getInstance().getTime());
            if (output != null) {
                Log.i(GigadocsConstants.GIGADOCS,output);
            }
        } catch (MalformedURLException e) {
            throw new GigadocsTimeOutException(e.getMessage());
        } catch (Throwable t) {
            if (t instanceof GigadocsDataUnavailableException){
                throw  new GigadocsDataUnavailableException(t.getMessage());
            }else if (t instanceof GigadocsUnAutorisedException){
                throw new GigadocsUnAutorisedException(t.getMessage());
            }else{
                t.printStackTrace();
            }
        }finally {
            assert httpConnection != null;
            httpConnection.disconnect();
        }

        return output;
    }

}





