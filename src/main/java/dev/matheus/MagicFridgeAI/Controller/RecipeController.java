package dev.matheus.MagicFridgeAI.Controller;

import dev.matheus.MagicFridgeAI.Service.ChatService;
import dev.matheus.MagicFridgeAI.Service.FoodItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Recipes", description = "Endpoints para gerar receitas com base nos itens cadastrados na geladeira.")
public class RecipeController {

    private final ChatService chatService;
    private final FoodItemService foodItemService;

    public RecipeController(FoodItemService foodItemService, ChatService chatService) {
        this.foodItemService = foodItemService;
        this.chatService = chatService;
    }

    @GetMapping("/generate")
    @Operation(summary = "Gera uma receita com base nos itens cadastrados na geladeira",
            description = "Utiliza o gemini para sugerir uma receita criativa e saborosa com os ingredientes disponíveis na geladeira.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mostra a receita gerada pelo gemini."),
            @ApiResponse(responseCode = "400", description = "A IA não conseguiu gerar uma resposta para a sua solicitação!")
    })
    public ResponseEntity<String> generateRecipeGemini() {
        String response = chatService.generateRecipeGemini(foodItemService.listar());
        if (response != null) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A IA não conseguiu gerar uma resposta para a sua solicitação!");
    }

}
