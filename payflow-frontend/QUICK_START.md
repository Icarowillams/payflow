# ⚡ PayFlow Frontend - Quick Start

## 🚀 Comandos Rápidos

### Instalação e Execução

```bash
# 1. Navegue até o diretório
cd /home/ubuntu/payflow-frontend

# 2. Instale as dependências
npm install

# 3. Execute em modo desenvolvimento
npm run dev

# Acesse: http://localhost:5173
```

### Build de Produção

```bash
# Build
npm run build

# Preview da build
npm run preview
```

### Linting

```bash
npm run lint
```

---

## 🔧 Configuração da API Backend

Por padrão, o frontend está configurado para conectar em:
```
http://localhost:8081
```

Para alterar, edite `src/services/api.js`:

```javascript
const API_URL = 'http://localhost:PORTA_DESEJADA';
```

---

## 📝 Credenciais para Teste

### Criar usuários de teste:

**Usuário 1:**
- Nome: João Silva
- Email: joao@test.com
- Senha: senha123

**Usuário 2:**
- Nome: Maria Santos
- Email: maria@test.com
- Senha: senha123

---

## 🎯 Fluxo de Teste Rápido

```bash
1. Abra http://localhost:5173
2. Clique em "Criar conta"
3. Cadastre o Usuário 1
4. Faça login
5. Copie seu ID do Dashboard
6. Deposite R$ 500
7. Faça logout
8. Cadastre o Usuário 2
9. Faça login
10. Copie o ID do Usuário 2
11. Faça logout
12. Login como Usuário 1
13. Transfira R$ 100 para o Usuário 2 usando o ID copiado
14. Veja o histórico
15. Faça logout e login como Usuário 2 para verificar o recebimento
```

---

## 🐛 Troubleshooting Rápido

### Erro de CORS
```bash
# Certifique-se de que o backend está rodando
curl http://localhost:8081/auth/login
```

### Token expirado
```javascript
// Limpe o localStorage
localStorage.clear()
// Faça login novamente
```

### Dependências com problema
```bash
# Reinstale tudo
rm -rf node_modules package-lock.json
npm install
```

### Build não funciona
```bash
# Verifique se todas as dependências estão instaladas
npm install

# Tente o build novamente
npm run build
```

---

## 📊 Estrutura de Rotas

| Rota          | Acesso  | Descrição                    |
|---------------|---------|------------------------------|
| /login        | Público | Tela de login                |
| /register     | Público | Tela de cadastro             |
| /dashboard    | Privado | Dashboard principal          |
| /transfer     | Privado | Transferência entre usuários |
| /deposit      | Privado | Depósito na conta            |
| /             | Público | Redireciona para /dashboard  |

---

## 🔐 Autenticação

O sistema usa JWT armazenado no `localStorage`:

```javascript
// Ver token atual
console.log(localStorage.getItem('token'))

// Limpar token
localStorage.removeItem('token')

// Limpar tudo
localStorage.clear()
```

---

## 🎨 Temas e Personalização

### Cores (Tailwind)

Edite `tailwind.config.js` para mudar as cores:

```javascript
theme: {
  extend: {
    colors: {
      primary: {
        500: '#SEU_HEX_AQUI',
        600: '#SEU_HEX_AQUI',
        // ...
      },
    },
  },
}
```

### Estilos Globais

Edite `src/index.css` para estilos globais.

---

## 📦 Dependências Principais

| Pacote              | Versão | Uso                    |
|---------------------|--------|------------------------|
| react               | 19.x   | Framework UI           |
| react-router-dom    | 7.x    | Roteamento             |
| axios               | 1.x    | Requisições HTTP       |
| tailwindcss         | 4.x    | Estilos                |
| lucide-react        | 1.x    | Ícones                 |

---

## ⚙️ Variáveis de Ambiente (Futuro)

Para usar variáveis de ambiente, crie um arquivo `.env`:

```env
VITE_API_URL=http://localhost:8081
```

E use no código:

```javascript
const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8081';
```

---

## 🔄 Atualizações

### Atualizar dependências

```bash
# Ver pacotes desatualizados
npm outdated

# Atualizar todos (cuidado!)
npm update

# Atualizar um específico
npm install react@latest
```

---

## 📱 Responsividade

O projeto é 100% responsivo:

- **Mobile**: < 640px
- **Tablet**: 640px - 1024px
- **Desktop**: > 1024px

Tailwind breakpoints: `sm:`, `md:`, `lg:`, `xl:`, `2xl:`

---

## 🎯 Scripts NPM

```bash
npm run dev      # Desenvolvimento
npm run build    # Build produção
npm run preview  # Preview da build
npm run lint     # Verificar código
```

---

## ✅ Checklist Pré-Deploy

- [ ] Backend rodando
- [ ] Variáveis de ambiente configuradas
- [ ] Build de produção testado
- [ ] CORS configurado no backend
- [ ] Rotas protegidas funcionando
- [ ] Validações testadas
- [ ] Responsividade verificada
- [ ] Navegadores testados

---

## 🌐 Deploy (Futuro)

### Vercel
```bash
npm install -g vercel
vercel
```

### Netlify
```bash
npm run build
# Upload da pasta dist/
```

### GitHub Pages
```bash
# Adicione ao package.json:
"homepage": "https://seu-usuario.github.io/payflow-frontend"

# Build e deploy
npm run build
# Upload da pasta dist/
```

---

**Pronto para começar! 🚀**
