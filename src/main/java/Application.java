import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class Application {
    public static void main(String[] args)  throws IOException {

        OkHttpClient client = new OkHttpClient();
        String api_key = System.getenv("API_KEY");
        Request request = new Request.Builder()
                .url("https://newsapi.org/v2/top-headlines?country=pl&category=business&apiKey="+api_key)
                .build();
        String gsonData;
        try (Response response = client.newCall(request).execute()) {
            gsonData = response.body().string();
        }
        catch ( Exception e ) {
            //possibles errors underneath
            // UnknownHostException >>> no internet connections serwer doesnt work( we do not know if we or serwer do not have internet)
            //SocketTimeoutException >>> connection started but serwer did not response
            // Exception any error or exception when appear
            System.err.println("Some errors");
            System.exit(1); // if not zero then error if zero all ok
            return;
        }
        Gson gson = new Gson();
        NewsApiResponse newsApiResponse = gson.fromJson(gsonData, NewsApiResponse.class);
        try (PrintWriter zapis = new PrintWriter("plik.txt", StandardCharsets.UTF_8);){
            for (Article article:newsApiResponse.articles)
                zapis.println(article.title+':'+ article.description+':'+article.author);
        }
        catch (Exception ee){
            System.err.println("file writing failed");
            System.exit(1); // if not zero then error if zero all ok
            return;
        // zapis.close();
    }
    }

}
