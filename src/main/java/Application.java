import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Application {
    public static void main(String[] args) {

        OkHttpClient client = new OkHttpClient();
        String api_key = System.getenv("API_KEY");
        Request request = new Request.Builder()
                .url("https://newsapi.org/v2/top-headlines?country=pl&category=business&apiKey=" + api_key)
                .build();
        String gsonData;
        try (Response response = client.newCall(request).execute()) {
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
        if (args.length < 1) {
            fileName = "articles.txt";
        } else {
            fileName = args[0];
        }
        try (PrintWriter record = new PrintWriter(fileName, StandardCharsets.UTF_8)) {
            for (Article article : newsApiResponse.articles)
                record.println(article.title + ':' + article.description + ':' + article.author);
        } catch (Exception ee) {
            System.err.println("file writing failed");
            System.exit(1);
        }
    }
}
