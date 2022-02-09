package rysiek.edu;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Application {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("You must provide apikey as the first parameter of the program. You can also provide output file name as the second parameter. This parameter is optional. The default parameter is articles.txt ");
            System.exit(1);
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://newsapi.org/v2/top-headlines?country=pl&category=business&apiKey=" + args[0])
                .build();
        String gsonData;
        try (Response response = client.newCall(request).execute()) {
            if (response.code() != 200){
                System.out.println("Your request wasn't correctly processed");
                System.exit(1);
            }
            gsonData = Objects.requireNonNull(response.body()).string();
        } catch (Exception e) {
            System.err.println("Some errors");
            System.exit(1);
            return;
        }
        Gson gson = new Gson();
        NewsApiResponse newsApiResponse = gson.fromJson(gsonData, NewsApiResponse.class);
        if (newsApiResponse.status.equals("error")) {
            System.err.println("The server has return an error ");
            System.exit(1);
        }
        String fileName;
        if (args.length < 2) {
            fileName = "articles.txt";

        } else {
            fileName = args[1];
        }
        try (PrintWriter writer = new PrintWriter(fileName, StandardCharsets.UTF_8)) {
            for (Article article : newsApiResponse.articles)
                writer.println(article.author + ':' + article.title + ':' + article.description );
        } catch (Exception ee) {
            System.err.println("file writing failed");
            System.exit(1);
        }
    }
}
