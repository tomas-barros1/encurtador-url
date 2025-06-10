package com.url.shorter.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ShortUrlRequest(
    @NotBlank(message = "A URL é obrigatória")
    @Size(max = 2048, message = "A URL deve ter no máximo 2048 caracteres")
    @Pattern(
        regexp = "^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?$",
        message = "A URL fornecida é inválida"
    )
    String url
) {}
