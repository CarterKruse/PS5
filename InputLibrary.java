import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputLibrary
{
    static List<List<String>> sentences;
    static List<List<String>> tags;

    static BufferedReader sentencesInput;
    static BufferedReader tagsInput;

    public static List<List<String>> loadSentences(String filename)
    {
        sentences = new ArrayList<>();

        try
        {
            sentencesInput = new BufferedReader(new FileReader(filename));

            String line;
            while ((line = sentencesInput.readLine()) != null && line.length() > 0)
            {
                List<String> sentence = List.of(line.split(" "));

                for (String item: sentence)
                    item.toLowerCase();

                sentences.add(sentence);
            }
        }

        catch (IOException e)
        {
            System.err.println("Error: " + e.getMessage());
        }

        return sentences;
    }

    public static List<List<String>> loadTags(String filename)
    {
        tags = new ArrayList<>();

        try
        {
            tagsInput = new BufferedReader(new FileReader(filename));

            String line;
            while ((line = tagsInput.readLine()) != null && line.length() > 0)
            {
                List<String> tagList = List.of(line.split(" "));

                for (String item: tagList)
                    item.toLowerCase();

                tags.add(tagList);
            }
        }

        catch (IOException e)
        {
            System.err.println("Error: " + e.getMessage());
        }

        return tags;
    }
}

