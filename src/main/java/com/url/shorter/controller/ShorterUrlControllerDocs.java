package com.url.shorter.controller;

import com.url.shorter.requests.ShortUrlRequest;
import com.url.shorter.requests.ShortUrlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "URL Shortener", description = "API para encurtar e redirecionar URLs")
public interface ShorterUrlControllerDocs {  @Operation(
        summary = "Encurtar uma URL",
        description = "Cria uma versão encurtada de uma URL longa. A URL encurtada expira em 2 dias por padrão."
  )
  @ApiResponses(value = {
        @ApiResponse(
              responseCode = "200",
              description = "URL encurtada com sucesso",
              content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ShortUrlResponse.class),
                    examples = @ExampleObject(
                          name = "Exemplo de resposta",
                          value = """
                                    {
                                      "shortUrl": "2A3B4C",
                                      "expirationDateTime": "2025-06-12T10:30:00"
                                    }
                                    """
                    )
              )
        ),
        @ApiResponse(
              responseCode = "400",
              description = "Dados inválidos na requisição",
              content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                          name = "Erro de validação",
                          value = """
                                    {
                                      "timestamp": "2025-06-10T10:30:00",
                                      "status": 400,
                                      "errors": {
                                        "url": "A URL fornecida é inválida"
                                      }
                                    }
                                    """
                    )
              )
        ),
        @ApiResponse(
              responseCode = "500",
              description = "Erro interno do servidor",
              content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                          name = "Erro interno",
                          value = """
                                    {
                                      "status": 500,
                                      "message": "Erro interno do servidor",
                                      "timestamp": "2025-06-10T10:30:00"
                                    }
                                    """
                    )
              )
        )
  })
  @PostMapping
  ResponseEntity<ShortUrlResponse> shortUrl(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Dados da URL a ser encurtada",
              required = true,
              content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ShortUrlRequest.class),
                    examples = @ExampleObject(
                          name = "Exemplo de requisição",
                          value = """
                                {
                                  "url": "https://www.google.com"
                                }
                                """
                    )
              )
        )
        @RequestBody ShortUrlRequest shortUrlRequest);

  @Operation(
        summary = "Redirecionar para URL original",
        description = "Redireciona automaticamente para a URL original usando o código encurtado. " +
              "Este endpoint é usado quando alguém acessa a URL encurtada no browser."
  )
  @ApiResponses(value = {
        @ApiResponse(
              responseCode = "302",
              description = "Redirecionamento bem-sucedido para a URL original",
              content = @Content(
                    examples = @ExampleObject(
                          name = "Redirecionamento",
                          description = "Browser é redirecionado automaticamente para a URL original"
                    )
              )
        ),
        @ApiResponse(
              responseCode = "404",
              description = "Código da URL encurtada não encontrado",
              content = @Content(
                    mediaType = "application/json",                    examples = @ExampleObject(
                          name = "URL não encontrada",
                          value = """
                                    {
                                      "status": 404,
                                      "message": "URL encurtada não encontrada",
                                      "timestamp": "2025-06-10T10:30:00"
                                    }
                                    """
                    )
              )
        ),
        @ApiResponse(
              responseCode = "410",
              description = "URL encurtada expirada",
              content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                          name = "URL expirada",
                          value = """
                                    {
                                      "status": 410,
                                      "message": "URL expirada",
                                      "timestamp": "2025-06-10T10:30:00"
                                    }
                                    """
                    )
              )
        )
  })  @GetMapping("/{shortCode}")
  ResponseEntity<Void> redirectToOriginalUrl(
        @Parameter(
              description = "Código da URL encurtada (6 caracteres alfanuméricos)",
              example = "2A3B4C",
              required = true
        )
        @PathVariable String shortCode,
        HttpServletRequest request);
}
