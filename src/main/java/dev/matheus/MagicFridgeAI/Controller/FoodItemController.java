package dev.matheus.MagicFridgeAI.Controller;

import dev.matheus.MagicFridgeAI.DTOs.FoodItemDTO;
import dev.matheus.MagicFridgeAI.Mapper.FoodItemMapper;
import dev.matheus.MagicFridgeAI.Service.FoodItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/food")
@Tag(name = "Food", description = "Endpoints para gerenciamento de itens na geladeira.")
public class FoodItemController {

    private final FoodItemService foodItemService;
    private final FoodItemMapper foodItemMapper;

    public FoodItemController(FoodItemService foodItemService, FoodItemMapper foodItemMapper) {
        this.foodItemService = foodItemService;
        this.foodItemMapper = foodItemMapper;
    }

    // POST REQUEST - Cria um item
    @PostMapping("/criar")
    @Operation(summary = "Cadastro de um item para geladeira",
            description = "Cria um novo item para a geladeira com os detalhes fornecidos. categoria deve ser uma das predefinidas," +
                    "A categoria do item de comida. Deve ser um dos seguintes valores: " +
                    "LATICINIOS, CARNES, VEGETAIS, FRUTAS, GRAOS, FRUTOS_DO_MAR, BEBIDAS, " +
                    "LANCHES, CONDIMENTOS, CONGELADOS, PADARIA, ENLATADOS, ESPECIARIAS, OLEOS, DOCES ou OUTROS.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item criado com sucesso.",
                    content = @Content(schema = @Schema(implementation = FoodItemDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida (item já existe ou dados incorretos).")
    })
    public ResponseEntity<FoodItemDTO> criar(@RequestBody FoodItemDTO foodItemDTO) {
        FoodItemDTO salvo = foodItemService.salvar(foodItemDTO);
        return ResponseEntity.ok(salvo);
    }

    // GET REQUEST - Listar todos os itens
    @GetMapping("/listar")
    @Operation(summary = "Listagem de todos os itens da geladeira",
            description = "Retorna uma lista de todos os itens cadastrados na geladeira.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de itens recuperada com sucesso.",
                    content = @Content(schema = @Schema(implementation = FoodItemDTO.class))),
            @ApiResponse(responseCode = "404", description = "Nenhum item encontrado na geladeira.")
    })
    public ResponseEntity<List<FoodItemDTO>> listar() {
        List<FoodItemDTO> foodItems = foodItemService.listar().stream()
                .map(foodItemMapper::map)
                .collect(Collectors.toList());
        return ResponseEntity.ok(foodItems);
    }

    // GET REQUEST BY ID - Lista um item por ID
    @GetMapping("/listar/{id}")
    @Operation(summary = "Listagem de um item específico por ID",
            description = "Retorna os detalhes de um item específico da geladeira pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item encontrado.",
                    content = @Content(schema = @Schema(implementation = FoodItemDTO.class))),
            @ApiResponse(responseCode = "404", description = "Item não encontrado com o ID fornecido.")
    })
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
    @Operation(summary = "Deletar um item",
            description = "Remove um item da geladeira pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Item não encontrado com o ID fornecido.")
    })
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

    // PATCH REQUEST - Atualizar parcialmente um item
    @PatchMapping("/atualizar/{id}")
    @Operation(summary = "Atualizar parcialmente um item",
            description = "Atualiza parcialmente os detalhes de um item da geladeira pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso.",
                    content = @Content(schema = @Schema(implementation = FoodItemDTO.class))),
            @ApiResponse(responseCode = "404", description = "Item não encontrado com o ID fornecido.")
    })
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