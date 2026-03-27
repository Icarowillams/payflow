# 📖 Guia de Uso do PayFlow Frontend

## Como encontrar o ID de um usuário

Para realizar uma transferência, você precisa do **ID do usuário destinatário**. Aqui estão algumas formas de obter esse ID:

### Método 1: Console do Navegador (Desenvolvimento)

1. Faça login na conta
2. Abra o Console do Navegador (F12)
3. Digite: `JSON.parse(localStorage.getItem('user'))?.id`
4. Ou acesse a aba "Application" → "Local Storage" → veja os dados armazenados

### Método 2: Adicionar componente de exibição do ID

Você pode adicionar uma seção no Dashboard que exiba o ID do usuário logado para compartilhamento.

**Exemplo de código para adicionar ao Dashboard:**

```jsx
<div className="bg-white rounded-xl shadow-md p-6">
  <h3 className="text-lg font-semibold text-gray-900 mb-2">Meu ID</h3>
  <div className="flex items-center space-x-2">
    <code className="flex-1 px-4 py-2 bg-gray-100 rounded-lg text-sm">
      {user?.id}
    </code>
    <button
      onClick={() => navigator.clipboard.writeText(user?.id)}
      className="px-4 py-2 bg-primary-600 text-white rounded-lg hover:bg-primary-700"
    >
      Copiar
    </button>
  </div>
  <p className="mt-2 text-xs text-gray-500">
    Compartilhe este ID com quem deseja te enviar dinheiro
  </p>
</div>
```

### Método 3: Via API (Backend)

O ID do usuário também é retornado pela API no endpoint `GET /users/me`:

```bash
curl -X GET http://localhost:8081/users/me \
  -H "Authorization: Bearer SEU_TOKEN_JWT"
```

Resposta:
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "João Silva",
  "email": "joao@example.com",
  "balance": 1000.00
}
```

## Fluxo completo de teste

### 1. Criar dois usuários

**Usuário 1:**
- Nome: João Silva
- Email: joao@email.com
- Senha: senha123

**Usuário 2:**
- Nome: Maria Santos
- Email: maria@email.com
- Senha: senha123

### 2. Fazer login com Usuário 1 (João)

1. Login com joao@email.com
2. No console: copie o ID do João
3. Fazer um depósito de R$ 500,00
4. Fazer logout

### 3. Fazer login com Usuário 2 (Maria)

1. Login com maria@email.com
2. No console: copie o ID da Maria
3. Fazer logout

### 4. Teste de transferência

1. Login com joao@email.com
2. Ir em "Transferir"
3. Colar o ID da Maria
4. Digite R$ 100,00
5. Confirmar transferência
6. Verificar que o saldo foi descontado
7. Verificar o histórico de transações

### 5. Verificar recebimento

1. Fazer logout
2. Login com maria@email.com
3. Verificar que o saldo aumentou para R$ 100,00
4. Verificar o histórico de transações mostrando o recebimento

## Dicas

### Validações implementadas

✅ Email deve ser válido  
✅ Senha deve ter no mínimo 6 caracteres  
✅ Não é possível transferir para si mesmo  
✅ Não é possível transferir com saldo insuficiente  
✅ Valores devem ser positivos  

### Mensagens de erro comuns

| Erro | Causa | Solução |
|------|-------|---------|
| "Email ou senha incorretos" | Credenciais inválidas | Verifique email e senha |
| "Saldo insuficiente" | Tentativa de transferir mais do que possui | Deposite mais dinheiro |
| "Destinatário não encontrado" | ID inválido ou inexistente | Verifique o ID do destinatário |
| "As senhas não coincidem" | Senhas diferentes no cadastro | Digite senhas iguais |

### Formato dos valores

- Use ponto (.) para separar decimais no input
- Exemplo: 100.50 para R$ 100,50
- O sistema formata automaticamente para o padrão brasileiro na exibição

### Histórico de transações

- **Verde (+)**: Dinheiro recebido
- **Vermelho (-)**: Dinheiro enviado
- Ordenado da mais recente para a mais antiga
- Mostra nome do remetente/destinatário e data/hora

## Troubleshooting

### Frontend não conecta no backend

1. Certifique-se de que o backend está rodando:
```bash
curl http://localhost:8081/auth/login
```

2. Verifique a URL da API em `src/services/api.js`

### Token expirado

O sistema redireciona automaticamente para login quando o token expira. Basta fazer login novamente.

### Erro 401 Unauthorized

- Verifique se você está logado
- Tente fazer logout e login novamente
- Limpe o localStorage: `localStorage.clear()`

### Transação não aparece no histórico

- Clique no botão "Atualizar" no Dashboard
- Verifique se a transação foi realmente concluída (mensagem de sucesso)
- Faça logout e login novamente

## Melhorias futuras sugeridas

1. **Buscar usuários por email**: Facilitar a busca de destinatários
2. **QR Code do ID**: Compartilhamento fácil do ID via QR Code
3. **Favoritos**: Salvar destinatários frequentes
4. **Notificações**: Alertas de novas transações
5. **Exportar extrato**: Download do histórico em PDF
6. **Filtros avançados**: Filtrar por data, valor, tipo

---

**Divirta-se explorando o PayFlow! 💰**
