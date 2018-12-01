package com.example.amankassahun;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

public class WebsiteFitcher  {
    private static final String TAG = "WebsiteFitcher";
    private static final String TA = "FlickrFetchr";
Activity activity;
    Context mContext;
    private static final int MESSAGE_DOWNLOAD = 2;
    private static final String FETCH_RECENTS_METHOD = "getRecent";
    private Handler mRequestHandler;
    private static final String SEARCH_METHOD = "search";
    private static final String NewPage_METHOD = "search";
    private static final Uri ENDPOIN = Uri.parse("http://www.icog-labs.com/news/");



    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public List<NotificationGallery> fetchRecentPhotos(String link,Context context ) {
//        mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD)
  //              .sendToTarget();
        mContext=context;
        String url = buildUrl(FETCH_RECENTS_METHOD, null,link);
        Log.d(TA,url+" recent");
        return fetchItems(url);
    }
    public List<NotificationGallery> searchPhotos(String query,String link) {

        String url = buildUrl(SEARCH_METHOD, query,link);
        Log.d(TA,url+"  search");
        return fetchItems(url);
    }
    public List<NotificationGallery> newPage(String query, String link) {

        String url = buildUrl(NewPage_METHOD, query,link);
        Log.d(TA,url+"  newpage");
        return fetchItems(url);
    }
    public List<NotificationGallery> fetchItems(String url) {

        Document doc;
List<NotificationGallery> items= new ArrayList<>();
        Log.d("ethiopia",url+"1");

        try {
            doc = Jsoup.connect(url).get();
            //Log.d("smart","doc="+doc);
         String justToString=url;
            if(justToString.equals("http://www.icog-labs.com/articles/")){
parseItemsOfBlog(items,doc);}
            else if(justToString.equals("http://www.icog-labs.com/news/")){
                parseItems(items,doc);}
            else if(justToString.equals("http://www.icog-labs.com/about-us/apprenticeship-program/")){
                parseItemsOfInternship(items,doc);
            }
            else {
                parseItemsOfVacancy(items,doc);
            }
        } catch (IOException e) {



            Log.d("smart", "MFAILWD ");
            e.printStackTrace();
        }
        return items;
    }




    private String buildUrl(String method, String query,String endPoint) {
        Uri ENDPOINT = Uri.parse(endPoint);
       String url=ENDPOINT.toString();
        if (method.equals(SEARCH_METHOD)) {
            url = ENDPOINT.buildUpon().appendQueryParameter("s", query).toString();
        }
        else if(method.equals(NewPage_METHOD)){
            url=ENDPOINT.toString()+query;
        }
        return url;
    }
    private void parseItemsOfInternship(List<NotificationGallery> items, Document doc){
        String url,content,id,subTitle;
        content=doc.getElementsByTag("em").first().text();
        NotificationGallery item = new NotificationGallery();
        item.setContent(content);
        item.setTitle(null);
        item.setSubTitle(null);
        item.setUrl("");
        item.setId(null);
        items.add(item);

    }
    private void parseItems(List<NotificationGallery> items, Document doc)
    {    String id,url,content,readMore;
        int i=0,j;
        String realTitle=null;
        Element eachArticles=null;
        Elements allArticles,titles=null;
        allArticles= doc.getElementsByTag("h4");

        for (Element article:allArticles)
        {
            NotificationGallery item = new NotificationGallery();
            id=article.nextElementSibling().getElementsByTag("img").attr("data-attachment-id");
            Log.d(TA,"id="+id);
            if(id!=""&&id!=null){
                 item.setId(id);
                content=article.text();
                if(content=="")
                    continue;
                item.setTitle(content);
                readMore=article.getElementsByTag("a").attr("href");
                Log.d(TA,"readMore= "+readMore);
                item.setReadMore(readMore);
                Log.d(TA,"content= "+content);
                url=article.nextElementSibling().getElementsByTag("img").attr("src");
                item.setUrl(url);j=0;
                Log.d(TA,"url= "+url);

                realTitle=article.nextElementSibling().text();
                if(realTitle==null||realTitle=="")
                    realTitle=article.nextElementSibling().nextElementSibling().text();


                Log.d(TA,"i="+i);
              /*  for(Element title:titles){
                realTitle=title.text();
if(realTitle=="")
    continue;
                    Log.d(TA,"j="+j);
                    if(i==j){
                        Log.d(TA,"gonna breaked");
                        break;
                    }
                j++;}*/item.setContent(realTitle);  Log.d(TA,"title="+realTitle);
            items.add(item);
            }
i++;
    }
}
    private void parseItemsOfVacancy(List<NotificationGallery> items, Document doc)
    {    String id,url,content=null,subTitle;
        ArrayList<String> t2 = new ArrayList<String>();

        int i=0,j=100000000,k=1;
        String realTitle=null;
        Element eachArticles=null;
        Elements allArticles,titles=null;
        titles= doc.getElementsByAttributeValue("style","color: #808000;");
        allArticles= doc.getElementsByClass("accordions-head");
        for (Element titl:titles) {
            Log.d(TA,"bura"+titl.text());
            t2.add(titl.text());
        }

        for (Element article:allArticles) {
            boolean bull=false;
            NotificationGallery item = new NotificationGallery();
                item.setId(null);
            Log.d(TA,"i na j"+i+j+k);
            eachArticles=article;
            eachArticles=eachArticles.nextElementSibling().getElementsByTag("p").first();
            content =eachArticles.text();
            do {
                eachArticles = eachArticles.nextElementSibling();
                content=content+"\n"+eachArticles.text();
            }while (eachArticles.nextElementSibling()!=null);
            if (content == "")
                continue;
            if(i==j+1) {
                item.setTitle(t2.get(k));
                Log.d(TA,"exccuted"+t2.get(k));
bull=true;
                k++;
            }
            if(check(article)){
                Log.d(TA,"check exccuted");
              j=i;
            }
            else if(i==0){
                item.setTitle(t2.get(0));
            }

            else if(!bull){
            item.setTitle("");
            }

            Log.d(TA, "content= " + content);

            item.setUrl("");

subTitle=article.attr("main-text");
                item.setSubTitle(subTitle);



            Log.d(TA, "i=" + i);
              /*  for(Element title:titles){
                realTitle=title.text();
if(realTitle=="")
    continue;
                    Log.d(TA,"j="+j);
                    if(i==j){
                        Log.d(TA,"gonna breaked");
                        break;
                    }
                j++;}*/
            item.setContent(content);
            Log.d(TA, "Subtitle= "+subTitle);
            items.add(item);


            i++;
        }
    }
    private void parseItemsOfVacancyy(List<NotificationGallery> items, Document doc)
    {    String id="a",url,content,subTitle,title,toBeCut;
        int i=0,j;
        String realTitle=null;
        Element eachArticles=null;
        Elements allArticles,titles=null;
        allArticles= doc.getElementsByTag("hr");

        for (Element article:allArticles)
        {
            NotificationGallery item = new NotificationGallery();
            boolean myTired=article.nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().getElementsByTag("p").attr("style").equals("height: 2px; width: 965px;");
            if(myTired)
                return;
            else
            eachArticles=article.nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling();

            title=article.nextElementSibling().text();
            Log.d(TA,"title="+title);

            id=eachArticles.attr("id");
            if(id.length()>=10){
                toBeCut=id.substring(0,10);
            }
            else {
                continue;
            }
            Log.d(TA,"id="+id+"x"+toBeCut);
            if(toBeCut.equals("accordions")){
                item.setId(id);
                subTitle=article.getElementsByTag("span").text();
                if(subTitle=="")
                    continue;
                item.setSubTitle(subTitle);
                Log.d(TA,"subt= "+subTitle);
                url="";
                item.setUrl(url);j=0;
                Log.d(TA,"url= "+url);

                Log.d(TA,"titled="+title);
              item.setTitle(title);
               content= article.getElementsByTag("p").text();

                Log.d(TA,"i="+i);
              /*  for(Element title:titles){
                realTitle=title.text();
if(realTitle=="")
    continue;
                    Log.d(TA,"j="+j);
                    if(i==j){
                        Log.d(TA,"gonna breaked");
                        break;
                    }
                j++;}*/item.setContent(content);  Log.d(TA,"content="+content);
                items.add(item);
            }
            i++;
        }
    }
    private boolean check(Element eachArTicles){
        boolean checkIt=eachArTicles.nextElementSibling().nextElementSibling()==null;
        Log.d(TA,"gg"+checkIt);
return checkIt;
    }
    private void parseItemsOfBlog(List<NotificationGallery> items, Document doc)
    {    String id,url,content;
        int i=0,j;
        String realTitle=null,readMoree;
        Element eachArticles=null;
        Elements allArticles,titles=null;
        allArticles= doc.getElementsByTag("div");



        for (Element article:allArticles)
        {
            String toBeCut=null;
            NotificationGallery item = new NotificationGallery();
            id=article.attr("id");
            if(id.length()>=5){
                toBeCut=id.substring(0,4);
            }
            else {
                continue;
            }
            Log.d(TA,"id="+id+"x"+toBeCut);
            if(toBeCut.equals("post")){
                item.setId(id);
                content=article.getElementsByTag("h1").text();
                if(content=="")
                    continue;
                item.setTitle(content);
                Log.d(TA,"content= "+content);
                url=article.getElementsByTag("img").attr("src");
                item.setUrl(url);j=0;
                Log.d(TA,"url= "+url);

                realTitle=article.getElementsByTag("p").text();

                readMoree=article.getElementsByTag("a").attr("href");
                item.setReadMore(readMoree);
                Log.d("lasted",readMoree);

                Log.d(TA,"i="+i);
              /*  for(Element title:titles){
                realTitle=title.text();
if(realTitle=="")
    continue;
                    Log.d(TA,"j="+j);
                    if(i==j){
                        Log.d(TA,"gonna breaked");
                        break;
                    }
                j++;}*/item.setContent(realTitle);  Log.d(TA,"title="+realTitle);
                items.add(item);
            }
            i++;
        }
    }
}