import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class Main {
    public static final int PORT = 8989;
    public static final String HOST = "127.0.0.1";


    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен.");
            while (true) {
                try (Socket client = server.accept();
                     PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
                    if (client.isConnected()) {
                        out.println("Новое подключение");
                        writer.println("Введите слово для поиска: ");
                        String word = reader.readLine();
                        word = word.toLowerCase();
                        List<PageEntry> pageEntryList = engine.search(word);
                        writer.println(answerJson(pageEntryList));
                    }
                }
            }

        } catch (IOException e) {
            out.println("Сеанс окончен.");
            e.printStackTrace();
        }
    }

    private static String answerJson(List<PageEntry> pageEntryList) {
        var builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        List<String> jsonList = new ArrayList<>();

        if (pageEntryList.isEmpty()) {
            return "Слово не найдено.";
        }

        for (PageEntry pageEntry : pageEntryList) {
            jsonList.add(gson.toJson(pageEntry));
        }
        return jsonList.toString();
    }
    }
