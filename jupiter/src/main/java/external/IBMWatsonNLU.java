package external;

import com.ibm.watson.natural_language_understanding.v1.NaturalLanguageUnderstanding;

import com.ibm.watson.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.natural_language_understanding.v1.model.Features;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ibm.cloud.sdk.core.security.Authenticator;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;


public class IBMWatsonNLU {
	
	static final String IBM_API_KEY = "1y_QK-XyG3UGvhQu5TKMGufCm4IjKi1wIFf4LsqIN89f";
	
	static AnalysisResults analyzeKeywords(String text, String apiKey) {
		Authenticator authenticator = new IamAuthenticator(apiKey);
		NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding("2019-07-12", authenticator);

		EntitiesOptions entities = new EntitiesOptions.Builder()
		  .sentiment(false)
		  .limit(3L)
		  .build();
		Features features = new Features.Builder()
		  .entities(entities)
		  .build();
		AnalyzeOptions parameters = new AnalyzeOptions.Builder()
		  .text(text)
		  .features(features)
		  .build();

		AnalysisResults results = service.analyze(parameters).execute().getResult();
//		System.out.println(results);
		return results;
	}
	
	// .toString converts AnalysisResults to String type with json character
    public static List<String> extractTextFromJson(AnalysisResults analysisResults) {
    	JSONObject json = new JSONObject(analysisResults.toString());
    	
        List<String> extractedTexts = new ArrayList<>();
        extractTextRecursively(json, extractedTexts);
        return extractedTexts;
    }

    private static void extractTextRecursively(Object element, List<String> texts) {
        if (element instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) element;
            jsonObject.keySet().forEach(key -> {
                if ("text".equals(key)) {
                    texts.add(jsonObject.get(key).toString());
                } else {
                    extractTextRecursively(jsonObject.get(key), texts);
                }
            });
        } else if (element instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) element;
            for (int i = 0; i < jsonArray.length(); i++) {
                extractTextRecursively(jsonArray.get(i), texts);
            }
        }
    }
	
	
	
//	public static void main(String[] args) {
//		String test = "Job Description The Role: We have recently won a significant financial services assignment globally, as we undergo major transformation of our global media relationship to ensure our partnership is future ready. We are looking for Global Marketing Technology Lead who will be a key pioneering leader within the Western Union team and broader Data Center of Excellence, and will be responsible for cultivating and leading Technology And Activation Group (TAAG) related conversations, ensuring the end…";
//		AnalysisResults res = analyzeKeywords(test, IBM_API_KEY);
//		List<String> keywords = extractTextFromJson(res);
//		System.out.println(keywords);
//	}
}
