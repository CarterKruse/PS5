import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViterbiAlgorithm
{
    private static HiddenMarkovModel markovModel;
    private static int unseenPenalty = -100;

    public static void trainModel()
    {
        List<String> words = new ArrayList<>();
        List<String> partsOfSpeech = new ArrayList<>();

        List<List<String>> wordsArray = InputLibrary.loadSentences("PS5/texts/brown-train-sentences.txt");
        List<List<String>> partsOfSpeechArray = InputLibrary.loadTags("PS5/texts/brown-train-tags.txt");

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
        if (sentence == null || sentence.isEmpty())
        {
            System.err.println("Error: Invalid Input");
            return null;
        }

        List<String> currentStates = new ArrayList<String>();
        currentStates.add("#");

        Map<String, Double> currentScores = new HashMap<String, Double>();
        currentScores.put("#", 0.0);

        List<Map<String, String>> backTraceList = new ArrayList<>();

        Map<String, Map<String, Double>> transitionMap = markovModel.getTransitionMap();
        Map<String, Map<String, Double>> observationMap = markovModel.getObservationMap();

        for (int i = 0; i < sentence.size(); i += 1)
        {
            String word = sentence.get(i);

            List<String> nextStates = new ArrayList<String>();
            Map<String, Double> nextScores = new HashMap<String, Double>();

            backTraceList.add(new HashMap<>());

            for (String currentState : currentStates)
            {
                // Include case in which current state is not in transmission scores.
                if (!transitionMap.containsKey(currentState))
                    continue;

                // Transition
                for (String nextState: transitionMap.get(currentState).keySet())
                {
                    double nextScore = currentScores.get(currentState) + transitionMap.get(currentState).get(nextState);

                    if (!observationMap.get(nextState).containsKey(word))
                        nextScore += unseenPenalty;
                    else
                        nextScore += observationMap.get(nextState).get(word);

                    if (!nextScores.containsKey(nextState) || nextScore > nextScores.get(nextState))
                    {
                        nextStates.add(nextState);
                        nextScores.put(nextState, nextScore);
                        backTraceList.get(i).put(nextState, currentState);
                    }
                }
            }

            currentStates = nextStates;
            currentScores = nextScores;
        }

        String lastState = findMaxEndState(currentScores);

        return findPath(backTraceList, lastState);
    }

    public static String findMaxEndState(Map<String, Double> currentScores)
    {
        double maxScore = Double.NEGATIVE_INFINITY;
        String bestTagMatch = "";

        for (String tag : currentScores.keySet())
        {
            double currentScore = currentScores.get(tag);

            if (currentScore > maxScore)
            {
                maxScore = currentScore;
                bestTagMatch = tag;
            }
        }

        return bestTagMatch;
    }

    public static List<String> findPath(List<Map<String, String>> backTrackList, String lastState)
    {
        List<String> path = new ArrayList<String>();

        for (int i = backTrackList.size() - 1; i >= 0; i -= 1)
        {
            path.add("");
        }

        for (int i = backTrackList.size() - 1; i >= 0; i -= 1)
        {
            path.set(i, lastState);
            lastState = backTrackList.get(i).get(lastState);
        }

        return path;
    }
}