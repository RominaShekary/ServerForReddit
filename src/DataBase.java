import java.io.*;
import java.util.*;

public class DataBase {
    static int accounts = 0;
    static int channels = 0;
    static private DataBase instance;
    private final HashMap<String, Controller> dataBase = new HashMap<>();

    public static DataBase getInstance() {
        if (instance == null)
            instance = new DataBase();
        return instance;
    }

    void addDataBase(String str, Controller controller) {
        dataBase.put(str, controller);
    }

    Controller getController(String str) {
        return dataBase.get(str);
    }
}

class Controller {
    private final File file;
    private Scanner scanner;
    private PrintWriter writer;

    public Controller(String path) {
        file = new File(path);

        try {
            if (!file.exists())
                file.createNewFile();
            scanner = new Scanner(file);
            writer = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String read() throws FileNotFoundException {
        StringBuilder str = new StringBuilder();
        scanner = new Scanner(file);
        while (scanner.hasNextLine())
            str.append(scanner.nextLine()).append("\n");
        return str.toString();
    }

    void write(String str) {
        writer.write(str);
        writer.flush();
    }

    String getRow(String username) throws FileNotFoundException {
        String[] strs = read().split("\n");
        for (String str : strs) {
            if (str.startsWith(username))
                return str;
        }
        return "invalid";
    }

    String[] getRows(String str) throws FileNotFoundException {
        String[] strs = read().split("\n");
        List<String> rows = new ArrayList<>();
        int size=0;
        for (String s : strs) {
            if (s.contains(str)) {
                rows.add(s);
                size++;
            }
        }
        return rows.toArray(new String[size]);
    }
}
