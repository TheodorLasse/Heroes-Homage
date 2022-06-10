package src.tools;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that reads json files. New files can be added by creating a new value in VALUES enum and map
 */
public class JsonReader
{
    public enum VALUES
    {
        NINJA_1, NINJA_2, NINJA_3, NINJA_4, NINJA_5, NINJA_6
    }

    private static final Logger LOGGER = Logger.getLogger("");

    private static final Map<VALUES, String> VALUES_MAP = Map.ofEntries(
            Map.entry(VALUES.NINJA_1, "ninja_1"),
            Map.entry(VALUES.NINJA_2, "ninja_2"),
            Map.entry(VALUES.NINJA_3, "ninja_3"),
            Map.entry(VALUES.NINJA_4, "ninja_4"),
            Map.entry(VALUES.NINJA_5, "ninja_5"),
            Map.entry(VALUES.NINJA_6, "ninja_6")
            );

    public static Map<?, ?> readJson(VALUES value) throws IOException, FileNotFoundException {
        String fileName = VALUES_MAP.get(value);

        Gson gson = new Gson();

        // The slash triggers a warning in the automatic code inspection. That can be ignored because this is a resource URL.
        final String name = "json/" + fileName + ".json";
        InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(name);

        if (inputStream == null) {
            throw new FileNotFoundException("Could not find resource " + name);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        Map<?, ?> map = gson.fromJson(reader, Map.class);
        reader.close();

        return map;
    }

    /**
     * Tries to load json data. If it fails, the program will be terminated.
     * This should only be used where the json data is critical for the program, and it won't work without it.
     *
     * @param value Value to load
     *
     * @return Map with json data.
     */
    public static Map<?, ?> readJsonCritical(VALUES value) {
        try {
            return readJson(value);
        } catch (IOException e) {
            // This catch clause triggers two warnings in the automatic code inspection.
            // "CatchReturnNull" can be ignored because it does not actually return null, since the program is terminated before that.
            // "CatchWithExit" can be ignored because this exception must be caused by a bug or installation problem, and this function is
            // used where the program won't work without the json.

            LOGGER.log(Level.SEVERE, e.toString(), e);
            System.exit(1);

            // To avoid "missing return statement". The code will never reach here, because the program is terminated.
            return null;
        }
    }
}
