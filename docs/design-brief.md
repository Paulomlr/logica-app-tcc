# Roteiro de Design — App de Ensino de Lógica e Tabela Verdade

Documento para validar telas e funcionalidades antes de implementar o frontend. Cada tela está mapeada ao endpoint do backend que ela consome (já implementado e testado), com lacunas explícitas onde o backend ainda precisaria de ajuste.

## Contexto e restrições de design (derivadas da pesquisa com estudantes, n=9)

- **Mobile-first**: smartphone é o dispositivo principal (6/9). Design para tela pequena primeiro.
- **Modo escuro** é o recurso de acessibilidade mais pedido; também citados: ajuste de tamanho de fonte, alto contraste.
- **Interface limpa, sem poluição visual** — citação literal de um respondente: um HUD de jogo de corrida cheio de elementos foi dado como exemplo negativo.
- **Feedback imediato** é quase unânime — toda ação de resposta precisa reagir na hora, não só no fim.
- **Gamificação bem recebida**: pontuação, conquistas, ranking.
- **PWA híbrido**: online com cache offline parcial, não site tradicional.
- **Tolerância de carregamento**: até 3s aceitável pela maioria.

## Paleta/tom sugerido
Modo escuro como padrão (não só opção), com alto contraste disponível. Poucos elementos por tela — cada tela deve ter **uma ação primária clara**.

---

## Telas

### 1. Splash / Onboarding
- Logo, breve explicação do app, prompt de instalação PWA.
- Botão "Entrar com Google".
- **Backend**: ⚠️ login ainda não implementado (adiado de propósito, ver histórico do projeto). Tela pode ser desenhada, mas não funcional ainda.

### 2. Home / Dashboard
- Saudação, card de resumo rápido (precisão geral, total de exercícios feitos).
- CTA principal: "Praticar".
- Acesso secundário: Progresso, Conquistas, Ranking.
- **Backend**: `GET /api/me/progress` — pronto e validado.

### 3. Seleção de Nível
- 3 cards: Fácil / Médio / Difícil.
- **Backend**: `GET /api/exercises?difficulty=X` — pronto.
- Opção "Gerar novo desafio" neste nível → `POST /api/exercises/generate?difficulty=X&count=1` — pronto.

### 4. Lista de Exercícios do Nível
- Lista de fórmulas disponíveis no nível escolhido (mostrar a fórmula como preview, ex: `(p ∧ q) → r`).
- **Backend**: mesmo endpoint da tela 3, filtrado.

### 5. Tela de Prática (núcleo do app)
- Tabela verdade: colunas de variáveis já preenchidas (somente leitura), colunas calculadas com input V/F por célula.
- Timer contando o tempo (vira `timeSpentSeconds` na submissão).
- Botão "Verificar" → `POST /api/exercises/{id}/attempts`.
- **Backend**: `GET /api/exercises/{id}/play` retorna `columnIsFillable` (array paralelo a `columnLabels`) — indica exatamente quais colunas o frontend deve renderizar como input vs. somente leitura. **Já resolve** o problema de "como saber quais colunas preencher" sem lógica duplicada no frontend.

### 6. Tela de Resultado
- Feedback célula a célula (verde/vermelho), revelando a resposta certa onde errou.
- Toast/modal de conquista desbloqueada, se houver.
- CTA: "Próximo exercício" ou "Voltar ao nível".
- **Backend**: `AttemptResultResponse` já traz `correctness`, `correctAnswers` e `newlyUnlockedAchievements` — pronto.

### 7. Progresso
- Anel/barra de precisão geral, tempo total, e um detalhamento por dificuldade (barras ou cards FACIL/MEDIO/DIFICIL).
- **Backend**: `GET /api/me/progress` — pronto, retorna exatamente essa estrutura (`byDifficulty` já vem quebrado por nível).

### 8. Histórico
- Lista cronológica (mais recente primeiro) de tentativas: fórmula, nível, certo/errado, tempo gasto.
- **Backend**: `GET /api/me/history` — pronto.

### 9. Conquistas
- Grade/lista de conquistas, idealmente mostrando também as **bloqueadas** (padrão comum em gamificação: ver o que falta desbloquear motiva).
- **Backend**: ⚠️ **lacuna real** — `GET /api/me/achievements` hoje só retorna as conquistas **já desbloqueadas**. Não existe endpoint com o catálogo completo (para mostrar bloqueadas). Duas opções pra decidir:
  - (a) Criar `GET /api/achievements` com o catálogo completo (5 conquistas fixas), e o frontend cruza com `/api/me/achievements` pra saber quais estão desbloqueadas.
  - (b) Enriquecer `/api/me/achievements` pra sempre retornar todas as 5, com um campo `unlocked: boolean` e `earnedAt` nulo se ainda não conquistada.
  - **Recomendo (b)**: uma chamada só, mais simples pro frontend.

### 10. Ranking
- Lista ordenada por acertos, destacando a posição do usuário atual.
- **Backend**: `GET /api/ranking` retorna a lista ordenada, mas **sem número de posição nem indicação de "esse é você"**. Com um único usuário de teste isso não aparece, mas quando o login existir isso vai importar. Não é bloqueante agora (dá pra calcular o índice no frontend), só vale registrar.

### 11. Configurações
- Modo escuro (padrão), tamanho de fonte, alto contraste, indicador de modo offline.
- **Backend**: nenhum — tudo client-side (localStorage/preferências do navegador).

---

## Resumo das lacunas de backend encontradas nesta revisão de design

1. **Catálogo de conquistas bloqueadas/desbloqueadas** — decidir entre opção (a) ou (b) acima antes de implementar a tela 9. (O protótipo visual já resolveu isso do lado do design, com dados mockados; falta ainda no backend real.)
2. **Posição no ranking** — não bloqueante agora, mas fica marcado para quando o login existir.
3. **Ranking multiusuário depende do login Google** — hoje só existe o usuário de teste fixo no banco. O ranking só terá dados reais de outros alunos quando o OAuth2 estiver implementado; até lá, qualquer nome "de exemplo" no design é ilustrativo, não dado real sincronizado.

## Decisões de escopo (revisão de protótipo, 07/08/2026)

- **Sistema de pontuação**: pontos por dificuldade (Fácil=1, Médio=2, Difícil=3), substituindo a contagem bruta de acertos usada hoje no ranking. Decidido, ainda não implementado no backend.
- **Bug de UX no preenchimento**: o toque na célula da tabela cicla em 3 estados (vazio→V→F→vazio), exigindo 2-3 toques pro valor certo. Fix proposto: toggle de 2 estados (toque alterna V↔F direto) + botão "Limpar respostas" separado no cabeçalho pra resetar tudo de uma vez.
- **Novos modos de exercício confirmados** (além do preenchimento atual, que o usuário considerou repetitivo demais sozinho):
  - **Classificar fórmula** (tautologia / contradição / contingência) — reaproveita os exemplos de De Morgan e equivalência do condicional já presentes no protótipo (que hoje são tratados como preenchimento comum, desperdiçando a propriedade especial deles).
  - **Modo tutor/dica** — botão de ajuda por célula explicando a regra do operador daquela linha. Resolve o pedido explícito da pesquisa por algo "como um tutor" e por "explicações teóricas", que nada no app cobre hoje.
  - **Ideia de fase futura (não escopo imediato)**: tutor com IA generativa, personalizando as explicações por aluno. Reconhecido como bem mais complexo (integração com API de LLM, custo por chamada, moderação de conteúdo) — registrar como possível diferencial do TCC, não arquitetar ainda.
