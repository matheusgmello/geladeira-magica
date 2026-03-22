package dev.matheus.MagicFridgeAI.Service;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import dev.matheus.MagicFridgeAI.Model.FoodItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Value("${api.key}")
    private String apiKey;

    public String generateRecipeGemini(List<FoodItem> foodItems) {
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("${api.key}") || apiKey.equals("${GEMINI_API_KEY}")) {
            System.err.println("API Key is missing or not resolved: " + apiKey);
            return "Erro: Chave de API do Gemini não configurada (GEMINI_API_KEY). Verifique suas variáveis de ambiente.";
        }

        System.out.println("Iniciando geração de receita. API Key carregada com sucesso (tamanho: " + apiKey.length() + ")");

        Client client = Client.builder()
                .apiKey(apiKey.trim())
                .build();

        String alimentos = foodItems.stream()
                .map(item -> String.format("%s (%s) - Quantidade: %d, validade: %s", item.getNome(), item.getCategoria(), item.getQuantidade(), item.getValidade()))
                .collect(Collectors.joining("\n"));

        try {
            String prompt = "Você é um Chef de Cozinha renomado. Com base nos ingredientes abaixo, crie sugestões de receitas criativas.\n\n" +
                    "REGRAS:\n" +
                    "1. Use Markdown estruturado.\n" +
                    "2. Use títulos (###) para o nome da receita.\n" +
                    "3. Use listas (1. 2. 3.) para passos e (* ) para ingredientes.\n" +
                    "4. Destaque os ingredientes da geladeira em **negrito**.\n" +
                    "5. Adicione uma seção curta de 'Dica do Chef' para cada prato.\n\n" +
                    "Ingredientes disponíveis:\n" + alimentos;

            GenerateContentResponse response =
                    client.models.generateContent(
                            "gemini-flash-latest",
                            prompt,
                            null);

            return response.text();
        } catch (Exception e) {
            System.err.println("Erro ao chamar a API do Gemini: " + e.getMessage());
            return "Desculpe, ocorreu um erro ao gerar sua receita. Verifique se sua chave de API é válida e se você tem saldo/cotas disponíveis. Detalhes: " + e.getMessage();
        }
    }
}
