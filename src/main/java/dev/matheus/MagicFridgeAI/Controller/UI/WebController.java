package dev.matheus.MagicFridgeAI.Controller.UI;

import dev.matheus.MagicFridgeAI.DTOs.FoodItemDTO;
import dev.matheus.MagicFridgeAI.Enums.FoodCategory;
import dev.matheus.MagicFridgeAI.Mapper.FoodItemMapper;
import dev.matheus.MagicFridgeAI.Service.ChatService;
import dev.matheus.MagicFridgeAI.Service.FoodItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/food/ui")
public class WebController {

    private final FoodItemService foodItemService;
    private final ChatService chatService;
    private final FoodItemMapper foodItemMapper;

    public WebController(FoodItemService foodItemService, ChatService chatService, FoodItemMapper foodItemMapper) {
        this.foodItemService = foodItemService;
        this.chatService = chatService;
        this.foodItemMapper = foodItemMapper;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/food/ui/dashboard";
    }
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<FoodItemDTO> foodItems = foodItemService.listar().stream()
                .map(foodItemMapper::map)
                .collect(Collectors.toList());
        model.addAttribute("foodItems", foodItems);
        model.addAttribute("foodItemDTO", new FoodItemDTO());
        model.addAttribute("categories", Arrays.asList(FoodCategory.values()));
        return "dashboard";
    }

    @PostMapping("/salvar")
    public String salvarItem(@ModelAttribute("foodItemDTO") FoodItemDTO foodItemDTO) {
        foodItemService.salvar(foodItemDTO);
        return "redirect:/food/ui/dashboard";
    }

    @GetMapping("/deletar/{id}")
    public String deletarItem(@PathVariable Long id) {
        foodItemService.deletar(id);
        return "redirect:/food/ui/dashboard";
    }

    @GetMapping("/gerarReceita")
    public String gerarReceita(Model model) {
        String recipe = chatService.generateRecipeGemini(foodItemService.listar());
        model.addAttribute("recipe", recipe);

        // Adiciona a lista de itens para evitar o NullPointerException
        List<FoodItemDTO> foodItems = foodItemService.listar().stream()
                .map(foodItemMapper::map)
                .collect(Collectors.toList());
        model.addAttribute("foodItems", foodItems);
        model.addAttribute("foodItemDTO", new FoodItemDTO());
        model.addAttribute("categories", Arrays.asList(FoodCategory.values()));

        return "dashboard";
    }
}
