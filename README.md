##API de gastos

A API desenvolvida permite cadastrar gastos associados a uma categoria
e consultar um resumo dos seus gastos.

Ex:

POST /api/gastos
```json
{
  "categoria": "alimentação",
  "valor": 100
}
```

GET /api/gastos/resumo
```json
{
  "alimentação": {
    "average": 100,
    "max": 100,
    "min": 100,
    "sum": 100,
    "count": 1
  }
}
```

Por simplicidade, a aplicação usa um banco de dados
em memória. Portanto os dados não persistiram entre execuções
consecutivas.

### Rodando a aplicação
```./mvnw spring-boot:run```

### Rodando os testes
```./mvnw test```

### Documentação
Para ver a documentação completa da API, acesse este endereço após iniciar a aplicação:
[http://localhost:8080/swagger-ui.html]()