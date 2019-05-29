package jp.sample;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.*;

public class ScrapeService {
    private String red    = "\u001b[00;31m";
    private String green  = "\u001b[00;32m";
    private String yellow = "\u001b[00;33m";
    private String purple = "\u001b[00;34m";
    private String pink   = "\u001b[00;35m";
    private String cyan   = "\u001b[00;36m";
    private String end    = "\u001b[00m";

    public void getTitle() throws IOException {
        String list[] = new String[10];
        try{
            File file = new File("input.txt");
            FileReader filereader = new FileReader(file);
            BufferedReader bufferreader = new BufferedReader(filereader);
            String line;
            int cnt=0;
            while ((line = bufferreader.readLine()) != null) {
                list[cnt] = line;
                cnt+=1;
            }
            filereader.close();
        }catch(FileNotFoundException e){
            System.out.println(e);
        }catch(IOException e) {
            System.out.println(e);
        }


        FileWriter fw = new FileWriter("out.csv");
        fw.write("カテゴリ,ランキング,商品名,価格,メーカー,商品ページへのリンク\n");
        for(String url : list) {
            Document document = Jsoup.connect(url).get();
            //System.out.println(document.html());
            Elements prices = document.getElementsByClass("p13n-sc-price");
            Elements elements = document.getElementsByClass("p13n-sc-truncate p13n-sc-line-clamp-2");
            Elements makers = document.getElementsByClass("a-size-small a-color-base");
            Elements ranks = document.getElementsByClass("zg_rankNumber");
            Elements URLs = document.getElementsByClass("a-fixed-left-grid-col a-col-right");
            Elements Category = document.getElementsByClass("category");


            int cnt = 0;
            String title_list[] = new String[100];
            for (Element element : elements) {
                title_list[cnt] = element.text();
                cnt += 1;
            }
            cnt = 0;
            String price_list[] = new String[100];
            for (Element price : prices) {
                price_list[cnt] = price.text();
                cnt += 1;
            }
            cnt = 0;
            String maker_list[] = new String[100];
            for (Element maker : makers) {
                maker_list[cnt] = maker.text();
                cnt += 1;
            }
            cnt = 0;
            String rank_list[] = new String[100];
            for (Element rank : ranks) {
                rank_list[cnt] = rank.text();
                cnt += 1;
            }
            cnt = 0;
            String URL_list[] = new String[100];
            for (Element URL : URLs) {
                URL_list[cnt] = "https://www.amazon.co.jp" + URL.select("a").attr("href");
                cnt += 1;
            }

            for (int i = 0; i < 20; i++) {
                System.out.println(rank_list[i]);
                System.out.println(green + title_list[i] + end);
                System.out.println(pink + price_list[i] + end);
                System.out.println(maker_list[i]);
                System.out.println(URL_list[i]);
                System.out.println(cyan + Category.text() + end);
                System.out.println();
                fw.write(Category.text()
                        +","+rank_list[i]
                        +","+title_list[i]
                        +","+price_list[i].replace(",","")
                        +","+maker_list[i]
                        +","+URL_list[i]
                        +"\n");
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                }
            }
        }
        fw.close();
    }
}