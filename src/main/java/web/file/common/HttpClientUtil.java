package web.file.common;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

    public static String doGet(String url) {
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        HttpResponse httpResponse = null;
        try {
            httpClient = HttpClients.createDefault();
            httpGet = new HttpGet(url);
            httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = httpResponse.getEntity();
                String string = EntityUtils.toString(entity, "UTF-8");
                return string;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != httpGet) httpGet.releaseConnection();
            if (null != httpResponse) {
                try {
                    if (null != httpClient) httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String doPost(String url) {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = "";
        try {
            httpClient = HttpClients.createDefault();
            httpPost = new HttpPost(url);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            result = EntityUtils.toString(responseEntity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != httpPost) httpPost.releaseConnection();
            try {
                if (null != httpClient) httpClient.close();
            } catch (IOException ioE) {

            }
        }
        return result;
    }

    public static String doPost(String url, JSONObject param) {
        StringEntity entity = new StringEntity(param.toString(), "UTF-8");
        entity.setContentType("application/json");
        return post(url, entity);
    }

    @Deprecated
    public static String doPost(String url, Map<String, String> param) throws UnsupportedEncodingException {
        List<NameValuePair> params = new ArrayList<>();
        if (null != params) {
            for (Map.Entry<String, String> entry : param.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
        return post(url, entity);
    }

    public static String doGet(String url, Map<String, String> param) throws UnsupportedEncodingException {
        List<NameValuePair> params = new ArrayList<>();
        if (null != params) {
            for (Map.Entry<String, String> entry : param.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        return get(url, new UrlEncodedFormEntity(params, "UTF-8"));
    }

    private static String post(String url, HttpEntity requestEntity) {
        CloseableHttpClient client = null;
        HttpPost post = null;
        String result = "";
        try {
            client = HttpClients.createDefault();
            post = new HttpPost(url);
            post.setEntity(requestEntity);
            HttpResponse response = client.execute(post);
            HttpEntity responseEntity = response.getEntity();
            result = EntityUtils.toString(responseEntity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != post) post.releaseConnection();
            if (null != client) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private static String get(String url, HttpEntity requestEntity) {
        CloseableHttpClient client = null;
        HttpGet get = null;
        String result = "";
        try {
            client = HttpClients.createDefault();
            get = new HttpGet(url + "?" + EntityUtils.toString(requestEntity, "UTF-8"));
            HttpResponse response = client.execute(get);
            HttpEntity responseEntity = response.getEntity();
            result = EntityUtils.toString(responseEntity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != get) get.releaseConnection();
            try {
                if (null != client) client.close();
            } catch (IOException ioE) {
                ioE.printStackTrace();
            }
        }
        return result;
    }


    @Deprecated
    public static String filePost(String postUrl, String filepath, HashMap<String, String> map) throws Exception {
        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, "----------ThIs_Is_tHe_bouNdaRY_$", Charset.defaultCharset());
        for (String in : map.keySet()) {
            multipartEntity.addPart(in, new StringBody(map.get(in), Charset.forName("UTF-8")));
        }
        multipartEntity.addPart("avatarFile", new FileBody(new File(filepath), "image/png/jpg"));
        HttpPost request = new HttpPost(postUrl);
        request.setEntity(multipartEntity);
        request.addHeader("Content-Type", "multipart/form-data; boundary=----------ThIs_Is_tHe_bouNdaRY_$");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(request);
        InputStream is = response.getEntity().getContent();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }

    public static String doPost(String url, String filePath, Map<String, String> params) {
        String result = "";
        CloseableHttpClient httpClient = null;
        HttpPost post = null;
        try {
            httpClient = HttpClients.createDefault();
            post = new HttpPost(url);
            File file = new File(filePath);
            FileBody avatar = new FileBody(file);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addPart("avatar", avatar);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addPart(entry.getKey(), new StringBody(entry.getValue(), ContentType.TEXT_PLAIN.withCharset("UTF-8")));
            }
            HttpEntity requestEntity = builder.build();
            post.setEntity(requestEntity);
            HttpResponse response = httpClient.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
            else
                result = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != post) post.releaseConnection();
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("uuid","1");
        map.put("actionCode","actionCode");
        map.put("actionContent","actionContent");
        map.put("personName","jerry");
        map.put("avatar","wenjian");
        map.put("groupId","1");
        map.put("birthday","2018-02-02");
        map.put("idNumber","34122419913544615656");
        map.put("sex","男");

        System.out.println(doPost("http://106.12.23.45:8088/api/v1/sign","C:/Users/Administrator/Desktop/AppFile/001.jpg",map));

    }

}
