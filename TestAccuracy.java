import java.util.List;

public class TestAccuracy
{
    public static void main(String[] args)
    {
        List<List<String>> testInput = InputLibrary.loadSentences("PS5/texts/brown-test-sentences.txt");
        List<List<String>> testOutput = InputLibrary.loadTags("PS5/texts/brown-test-tags.txt");

        ViterbiAlgorithm.trainModel();

        System.out.println(testAccuracy(testInput, testOutput));
    }

    public static double testAccuracy(List<List<String>> observations, List<List<String>> testTags)
    {
        double total = 0.0;
        double matches = 0.0;

        for (int i = 0; i < observations.size(); i += 1)
        {
            List<String> sentence = observations.get(i);
            List<String> tags = ViterbiAlgorithm.tagSentence(sentence);

            List<String> givenTags = testTags.get(i);

            for (int j = 0; j < observations.get(i).size(); j += 1)
            {
                if (tags.get(j).equals(givenTags.get(j)))
                {
                    matches += 1;
                }

                total += 1;
            }
        }

        return total - matches;
    }
}
