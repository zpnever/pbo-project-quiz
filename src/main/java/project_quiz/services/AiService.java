package project_quiz.services;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class AiService {
  public void generatedSoal(String prompt) {

    final var API_KEY = "AIzaSyCewyVTnecw029pGmp1H9yXbP1Bah9JkCA";
    // The client gets the API key from the environment variable `GEMINI_API_KEY`.
    Client client = Client.builder().apiKey(API_KEY).build();

    GenerateContentResponse response = client.models.generateContent(
        "gemini-2.5-flash",
        prompt,
        null);

    System.out.println(response.text());

  }
}