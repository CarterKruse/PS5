import java.util.ArrayList;
import java.util.List;

public class ViterbiAlgorithm
{
    private static HiddenMarkovModel markovModel;
    private static int unseenPenalty = -100;

    public static void trainModel()
    {
        List<String> words = new ArrayList<>();
        List<String> partsOfSpeech = new ArrayList<>();

        List<List<String>> wordsArray = InputLibrary.loadSentences("PS5/texts/simple-train-sentences.txt");
        List<List<String>> partsOfSpeechArray = InputLibrary.loadTags("PS5/texts/simple-train-tags.txt");

        for (List<String> item : wordsArray)
        {
            words.addAll(item);
        }

        for (List<String> item : partsOfSpeechArray)
        {
            partsOfSpeech.addAll(item);
        }

        markovModel = new HiddenMarkovModel();
        markovModel.trainObservations(partsOfSpeech, words);

        markovModel.trainTransitions(partsOfSpeechArray);
    }

    public static List<String> tagSentence(List<String> sentence)
    {
        List<String> tagList = new ArrayList<>();

        if (sentence == null || sentence.isEmpty())
        {
            System.err.println("Error: Invalid Input");
            return null;
        }

        // TODO

        return tagList;
    }

    public static void main(String[] args)
    {
        List<String> sentence = new ArrayList<>(List.of("the", "dog", "saw", "trains", "in", "the", "night", "."));
        tagSentence(sentence);
    }
}