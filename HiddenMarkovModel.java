import java.util.*;

public class HiddenMarkovModel
{
    private Map<String, Map<String, Double>> transitionMap;
    private Map<String, Map<String, Double>> observationMap;

    public HiddenMarkovModel()
    {
        transitionMap = new HashMap<>();
        observationMap = new HashMap<>();
    }

    public Map<String, Map<String, Double>> getTransitionMap()
    {
        return transitionMap;
    }

    public Map<String, Map<String, Double>> getObservationMap()
    {
        return observationMap;
    }

    public void trainObservations(List<String> partsOfSpeech, List<String> words)
    {
        if (partsOfSpeech.size() < 1 || words.size() < 1 || partsOfSpeech.size() != words.size())
        {
            System.err.println("Invalid Input - Incorrect Training Data");
            return;
        }

        for (int i = 0; i < partsOfSpeech.size(); i += 1)
        {
            String state = partsOfSpeech.get(i);
            if (!observationMap.containsKey(state))
                observationMap.put(state, new HashMap<>());

            Map<String, Double> map = observationMap.get(state);

            if (!map.containsKey(words.get(i)))
                map.put(words.get(i), 0.0);

            map.put(words.get(i), map.get(words.get(i)) + 1.0);
        }

        for (String state: observationMap.keySet())
        {
            double totalInstances = 0;

            for (String word: observationMap.get(state).keySet())
            {
                totalInstances += observationMap.get(state).get(word);
            }

            for (String word: observationMap.get(state).keySet())
            {
                double logScore = Math.log(observationMap.get(state).get(word) / totalInstances);
                observationMap.get(state).put(word, logScore);
            }
        }
    }

    public void trainTransitions(List<List<String>> partsOfSpeechTags)
    {
        if (partsOfSpeechTags.size() < 1)
        {
            System.err.println("Invalid Input - Incorrect Training Data");
            return;
        }

        for (List<String> partsOfSpeech: partsOfSpeechTags)
        {
            String previous = "#";
            for (int i = 0; i < partsOfSpeech.size(); i += 1)
            {
                String state = partsOfSpeech.get(i);

                if (!transitionMap.containsKey(previous))
                    transitionMap.put(previous, new HashMap<>());

                Map<String, Double> map = transitionMap.get(previous);

                if (!map.containsKey(state))
                    map.put(state, 0.0);

                map.put(state, map.get(state) + 1.0);
                previous = state;
            }
        }

        for (String state : transitionMap.keySet())
        {
            double totalInstances = 0;

            for (String transition : transitionMap.get(state).keySet())
            {
                totalInstances += transitionMap.get(state).get(transition);
            }

            for (String transition : transitionMap.get(state).keySet())
            {
                double logScore = Math.log(transitionMap.get(state).get(transition) / totalInstances);
                transitionMap.get(state).put(transition, logScore);
            }
        }
    }
}