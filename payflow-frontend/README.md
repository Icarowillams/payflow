# 💸 PayFlow Frontend

Frontend React moderno e intuitivo para o sistema de transferências PayFlow. Interface completa com autenticação JWT, dashboard interativo, e funcionalidades de transferência e depósito.

## 🎨 Funcionalidades

- ✅ **Autenticação JWT** - Login seguro com tokens
- ✅ **Cadastro de Usuário** - Criação de novas contas
- ✅ **Dashboard Interativo** - Visualização de saldo e histórico de transações
- ✅ **Transferências** - Envio de dinheiro entre usuários
- ✅ **Depósitos** - Adição de saldo à conta
- ✅ **Design Responsivo** - Interface bonita e adaptável
- ✅ **Validação de Formulários** - Feedback visual e validação em tempo real
- ✅ **Tratamento de Erros** - Mensagens claras e informativas

## 🚀 Tecnologias

- **React 18** - Biblioteca JavaScript para interfaces
- **Vite** - Build tool rápida e moderna
- **React Router DOM** - Navegação entre páginas
- **Axios** - Cliente HTTP para requisições à API
- **Tailwind CSS** - Framework CSS utilitário
- **Lucide React** - Biblioteca de ícones moderna

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Node.js** (versão 16 ou superior)
- **npm** ou **yarn**
- **Backend PayFlow** rodando em `http://localhost:8081`

## ⚙️ Instalação

### 1. Clone ou navegue até o diretório do projeto

```bash
cd payflow-frontend
```

### 2. Instale as dependências

```bash
npm install
```

### 3. Configure a URL da API (opcional)

Se o backend estiver rodando em uma porta diferente de `8081`, edite o arquivo `src/services/api.js`:

```javascript
const API_URL = 'http://localhost:PORTA_DO_SEU_BACKEND';
```

## 🎯 Como Executar

### Modo Desenvolvimento

```bash
npm run dev
```

O frontend estará disponível em: **http://localhost:5173**

### Build para Produção

```bash
npm run build
```

### Preview da Build de Produção

```bash
npm run preview
```

## 📱 Como Usar

### 1. **Criar uma Conta**
- Acesse a página inicial
- Clique em "Criar conta"
- Preencha nome, email e senha
- Clique em "Criar conta"

### 2. **Fazer Login**
- Na tela de login, insira seu email e senha
- Clique em "Entrar"
- Você será redirecionado para o Dashboard

### 3. **Dashboard**
- Visualize seu saldo atual
- Veja o histórico de todas as transações
- Acesse as funcionalidades de Transferir e Depositar

### 4. **Fazer um Depósito**
- No Dashboard, clique em "Depositar"
- Digite o valor ou use os valores rápidos
- Clique em "Depositar"
- Seu saldo será atualizado automaticamente

### 5. **Fazer uma Transferência**
- No Dashboard, clique em "Transferir"
- Digite o ID do usuário destinatário
- Digite o valor a transferir
- Clique em "Transferir"
- A transação aparecerá no histórico

### 6. **Sair da Conta**
- Clique no botão "Sair" no canto superior direito

## 🗂️ Estrutura do Projeto

```
payflow-frontend/
├── src/
│   ├── components/        # Componentes reutilizáveis
│   │   ├── Layout.jsx     # Layout principal com navbar
│   │   └── PrivateRoute.jsx # Proteção de rotas autenticadas
│   ├── contexts/          # Contextos React
│   │   └── AuthContext.jsx # Gerenciamento de autenticação
│   ├── pages/             # Páginas da aplicação
│   │   ├── Login.jsx      # Tela de login
│   │   ├── Register.jsx   # Tela de cadastro
│   │   ├── Dashboard.jsx  # Dashboard principal
│   │   ├── Transfer.jsx   # Tela de transferência
│   │   └── Deposit.jsx    # Tela de depósito
│   ├── services/          # Serviços de API
│   │   ├── api.js         # Configuração do Axios
│   │   ├── authService.js # Serviços de autenticação
│   │   ├── userService.js # Serviços de usuário
│   │   └── transactionService.js # Serviços de transações
│   ├── App.jsx            # Configuração de rotas
│   ├── main.jsx           # Ponto de entrada
│   └── index.css          # Estilos globais (Tailwind)
├── public/                # Arquivos públicos
├── index.html             # HTML principal
├── package.json           # Dependências
├── vite.config.js         # Configuração Vite
├── tailwind.config.js     # Configuração Tailwind
└── README.md              # Este arquivo
```

## 🔌 Endpoints da API

O frontend consome os seguintes endpoints do backend:

| Método | Endpoint                  | Descrição                    | Autenticação |
|--------|---------------------------|------------------------------|--------------|
| POST   | `/auth/login`             | Login de usuário             | Não          |
| POST   | `/users`                  | Cadastro de novo usuário     | Não          |
| GET    | `/users/me`               | Dados do usuário logado      | Sim          |
| POST   | `/users/me/deposit`       | Realizar depósito            | Sim          |
| POST   | `/transactions`           | Realizar transferência       | Sim          |
| GET    | `/transactions/history`   | Histórico de transações      | Sim          |

## 🔐 Autenticação

O sistema utiliza **JWT (JSON Web Token)** para autenticação:

1. Ao fazer login, o token é armazenado no `localStorage`
2. Todas as requisições autenticadas incluem o header: `Authorization: Bearer <token>`
3. Se o token expirar, o usuário é redirecionado para a tela de login
4. Ao fazer logout, o token é removido do `localStorage`

## 🎨 Design

O frontend foi desenvolvido com foco em:

- **UI/UX moderna** - Design clean e intuitivo
- **Responsividade** - Funciona em desktop, tablet e mobile
- **Acessibilidade** - Formulários e navegação acessíveis
- **Feedback Visual** - Loading states, mensagens de erro e sucesso
- **Paleta de cores** - Azul primary com gradientes suaves

## 🐛 Solução de Problemas

### Erro de CORS
Se encontrar erros de CORS, certifique-se de que o backend está configurado para aceitar requisições de `http://localhost:5173`.

### Backend não está respondendo
Verifique se o backend está rodando em `http://localhost:8081`:

```bash
curl http://localhost:8081/auth/login
```

### Dependências não instaladas
Remova a pasta `node_modules` e o arquivo `package-lock.json`, então reinstale:

```bash
rm -rf node_modules package-lock.json
npm install
```

## 📝 Notas Importantes

- O **ID do destinatário** para transferências pode ser encontrado na URL ou nos dados do usuário
- Os **depósitos são simulados** - em produção, seria necessária integração com gateway de pagamento
- As **senhas devem ter no mínimo 6 caracteres** conforme validação do backend
- O **saldo é atualizado automaticamente** após cada transação

## 👤 Desenvolvido para

Este frontend foi desenvolvido para complementar o backend **PayFlow** de **Ícaro Williams**.

---

## 🚀 Próximos Passos Sugeridos

- [ ] Adicionar tema dark mode
- [ ] Implementar busca de usuários por email
- [ ] Adicionar paginação no histórico de transações
- [ ] Implementar filtros por data e tipo de transação
- [ ] Adicionar gráficos de despesas e receitas
- [ ] Implementar notificações em tempo real
- [ ] Adicionar exportação de extrato em PDF

---

**Divirta-se usando o PayFlow! 💸**
