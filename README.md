# 🔗 URL Shorter - Encurtador de URLs

Um serviço de encurtamento de URLs moderno e eficiente construído com Spring Boot e MongoDB.

## ✨ Características

- 🚀 Encurtamento rápido de URLs
- 🔄 Redirecionamento automático
- ⏱️ Links com expiração automática (2 dias)
- 📊 Interface Swagger para documentação
- 🔍 Códigos curtos legíveis (6 caracteres)

## 🛠️ Tecnologias

- Java 17
- Spring Boot 3.5
- MongoDB
- Docker & Docker Compose
- Swagger/OpenAPI

## 🚀 Como Executar

1. Clone o repositório:

```bash
git clone https://seu-repositorio/url-shorter.git
cd url-shorter
```

2. Inicie o MongoDB usando Docker:

```bash
docker-compose up -d
```

3. Execute a aplicação:

```bash
./mvnw spring-boot:run
```

4. Acesse a documentação da API:

```
http://localhost:8080/swagger-ui.html
```

## 📝 Uso da API

### Encurtar uma URL

```http
POST /shorter
Content-Type: application/json

{
    "url": "https://www.exemplo.com.br/pagina-com-url-muito-longa"
}
```

Resposta:

```json
{
  "shortUrl": "2A3B4C",
  "expirationDateTime": "2025-06-12T10:30:00"
}
```

### Acessar URL Original

```http
GET /shorter/{shortCode}
```

O navegador será redirecionado automaticamente para a URL original.

## ⚙️ Configuração

As configurações podem ser ajustadas no arquivo `application.yml`:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://root:strongPassword123!@localhost:27017/url_shorter?authSource=admin

server:
  port: 8080
```

## 📈 Monitoramento

Acesse o MongoDB Express para visualizar os dados:

```
http://localhost:8081
```