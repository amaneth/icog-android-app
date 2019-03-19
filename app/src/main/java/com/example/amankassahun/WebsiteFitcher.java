package com.example.amankassahun;

import android.content.Context;
import android.net.Uri;

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

public class WebsiteFitcher  {
    private static final String TA = "FlickrFetchr";
    Context mContext;
    private static final String FETCH_RECENTS_METHOD = "getRecent";
    private static final String SEARCH_METHOD = "search";
    private static final String NewPage_METHOD = "search";



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
        return fetchItems(url);
    }
    public List<NotificationGallery> searchPhotos(String query,String link) {

        String url = buildUrl(SEARCH_METHOD, query,link);
        return fetchItems(url);
    }
    public List<NotificationGallery> fetchItems(String url) {

        Document doc;
List<NotificationGallery> items= new ArrayList<>();

        try {
            doc = Jsoup.connect(url).get();
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
        String content,id;
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
        int i=0;
        String realTitle=null;
        Elements allArticles;
        allArticles= doc.getElementsByTag("h4");

        for (Element article:allArticles)
        {
            NotificationGallery item = new NotificationGallery();
            id=article.nextElementSibling().getElementsByTag("img").attr("data-attachment-id");
            if(id!=""&&id!=null){
                 item.setId(id);
                content=article.text();
                if(content=="")
                    continue;
                item.setTitle(content);
                readMore=article.getElementsByTag("a").attr("href");
                item.setReadMore(readMore);
                url=article.nextElementSibling().getElementsByTag("img").attr("src");
                item.setUrl(url);

                realTitle=article.nextElementSibling().text();
                if(realTitle==null||realTitle=="")
                    realTitle=article.nextElementSibling().nextElementSibling().text();


             item.setContent(realTitle);
            items.add(item);
            }
i++;
    }
}
    private void parseItemsOfVacancy(List<NotificationGallery> items, Document doc)
    {    String content=null,subTitle;
        ArrayList<String> t2 = new ArrayList<String>();

        int i=0,j=100000000,k=1;
        Element eachArticles=null;
        Elements allArticles,titles=null;
        titles= doc.getElementsByAttributeValue("style","color: #808000;");
        allArticles= doc.getElementsByClass("accordions-head");
        for (Element titl:titles) {
            t2.add(titl.text());
        }

        for (Element article:allArticles) {
            boolean bull=false;
            NotificationGallery item = new NotificationGallery();
                item.setId(null);
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
bull=true;
                k++;
            }
            if(check(article)){
              j=i;
            }
            else if(i==0){
                item.setTitle(t2.get(0));
            }

            else if(!bull){
            item.setTitle("");
            }


            item.setUrl("");

subTitle=article.attr("main-text");
                item.setSubTitle(subTitle);




            item.setContent(content);
            items.add(item);


            i++;
        }
    }

    private boolean check(Element eachArTicles){
        boolean checkIt=eachArTicles.nextElementSibling().nextElementSibling()==null;
return checkIt;
    }
    private void parseItemsOfBlog(List<NotificationGallery> items, Document doc)
    {    String id,url,content;
        int i=0;
        String realTitle=null,readMoree;
        Elements allArticles;
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
            if(toBeCut.equals("post")){
                item.setId(id);
                content=article.getElementsByTag("h1").text();
                if(content=="")
                    continue;
                item.setTitle(content);
                url=article.getElementsByTag("img").attr("src");
                item.setUrl(url);

                realTitle=article.getElementsByTag("p").text();

                readMoree=article.getElementsByTag("a").attr("href");
                item.setReadMore(readMoree);

             item.setContent(realTitle);
                items.add(item);
            }
            i++;
        }
    }
}