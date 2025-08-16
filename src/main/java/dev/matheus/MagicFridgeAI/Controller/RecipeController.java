package dev.matheus.MagicFridgeAI.Controller;

import dev.matheus.MagicFridgeAI.Service.ChatService;
import dev.matheus.MagicFridgeAI.Service.FoodItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecipeController {

    private final ChatService chatService;
    private final FoodItemService foodItemService;

    public RecipeController(FoodItemService foodItemService, ChatService chatService) {
        this.foodItemService = foodItemService;
        this.chatService = chatService;
    }

    @GetMapping("/generate")
    private ResponseEntity<String> generateRecipeGemini() {
        String response = chatService.generateRecipeGemini(foodItemService.listar());
        if (response != null) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A IA não conseguiu gerar uma resposta para a sua solicitação!");
    }

}
