import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import static java.lang.System.out;

public class Main {
    public static final int PORT = 8989;
    public static final String HOST = "127.0.0.1";

    public static void main(String[] args) throws Exception {
        SearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
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
                        writer.println("Результат поиска по слову: " + word);
                        var pageEntryList = engine.search(word);
                        writer.println(BooleanSearchEngine.answerJson(pageEntryList));
                    }
                }
            }

        } catch (IOException e) {
            out.println("Сеанс окончен.");
            e.printStackTrace();
        }
    }
}
