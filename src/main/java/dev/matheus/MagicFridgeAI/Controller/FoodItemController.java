package dev.matheus.MagicFridgeAI.Controller;

import dev.matheus.MagicFridgeAI.DTOs.FoodItemDTO;
import dev.matheus.MagicFridgeAI.Mapper.FoodItemMapper;
import dev.matheus.MagicFridgeAI.Service.FoodItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/food")
public class FoodItemController {

    private final FoodItemService foodItemService;
    private final FoodItemMapper foodItemMapper;

    public FoodItemController(FoodItemService foodItemService, FoodItemMapper foodItemMapper) {
        this.foodItemService = foodItemService;
        this.foodItemMapper = foodItemMapper;
    }

    // POST REQUEST - Cria um item
    @PostMapping("/criar")
    public ResponseEntity<FoodItemDTO> criar(@RequestBody FoodItemDTO foodItemDTO) {
        FoodItemDTO salvo = foodItemService.salvar(foodItemDTO);
        return ResponseEntity.ok(salvo);
    }

    // GET REQUEST- Listar todos os itens
    @GetMapping("/listar")
    public ResponseEntity<List<FoodItemDTO>> listar() {
        List<FoodItemDTO> foodItems = foodItemService.listar().stream()
                .map(foodItemMapper::map)
                .collect(Collectors.toList());
        return ResponseEntity.ok(foodItems);
    }

    // GET REQUEST BY ID - Lista um item por ID
    @GetMapping("/listar/{id}")
    public ResponseEntity<?> listarPorId(@PathVariable Long id) {
        FoodItemDTO foodItemDTO = foodItemService.listarPorId(id);
        if (foodItemDTO != null) {
            return ResponseEntity.ok(foodItemDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("A comida de id " + id + " não existe");
        }
    }

    // DELETE REQUEST - Remover item
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        FoodItemDTO foodItem = foodItemService.listarPorId(id);
        if (foodItem != null) {
            foodItemService.deletar(id);
            return ResponseEntity.ok("Item: " + foodItem.getNome() + " deletado!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("A comida de id " + id + " não existe.");
        }
    }

    // PATCH REQUEST- Atualizar parcialmente um item
    @PatchMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarParcialmente(@PathVariable Long id,
                                                   @RequestBody FoodItemDTO foodItemDTO) {
        FoodItemDTO atualizado = foodItemService.atualizarParcialmente(id, foodItemDTO);
        if (atualizado != null) {
            return ResponseEntity.ok(atualizado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("A comida de id " + id + " não existe");
        }
    }
}