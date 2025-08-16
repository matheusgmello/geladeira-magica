package dev.matheus.MagicFridgeAI.Service;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import dev.matheus.MagicFridgeAI.Model.FoodItem;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final WebClient webClient;
    private final String apiKey = System.getenv("GOOGLE_API_KEY");

    public ChatService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String generateRecipeGemini(List<FoodItem> foodItems) {
        Client client = new Client();

        String alimentos = foodItems.stream()
                .map(item -> String.format("%s (%s) - Quantidade: %d, validade: %s", item.getNome(), item.getCategoria(), item.getQuantidade(), item.getValidade()))
                .collect(Collectors.joining("\n"));

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.5-flash",
                        "Agora você é um chef de cozinha e ira me receitar receitas com base nos Seguintes alimento disponiveis: " + alimentos,
                        null);

        return response.text();
    }
}
