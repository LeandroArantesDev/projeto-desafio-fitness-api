CREATE DATABASE IF NOT EXISTS projeto_desafios_fitness
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE projeto_desafios_fitness;

-- =========================================
-- TABELA DE USUARIOS
-- =========================================

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    tipo ENUM('admin', 'user') DEFAULT 'user',
    senha_hash VARCHAR(255) NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =========================================
-- TABELA DE DESAFIOS
-- =========================================

CREATE TABLE desafios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    criador_id INT NOT NULL,
    nome VARCHAR(150) NOT NULL,
    descricao TEXT,
    categoria ENUM('corrida', 'musculacao', 'ciclismo', 'flexoes', 'habito', 'outro') DEFAULT 'outro',
    tipo_meta ENUM('repeticoes', 'distancia_km', 'tempo_minutos', 'dias_seguidos') NOT NULL,
    meta_total DECIMAL(10, 2) NOT NULL,
    status ENUM('ativo', 'finalizado', 'cancelado') DEFAULT 'ativo',
    unidade_medida VARCHAR(20) GENERATED ALWAYS AS (
        CASE tipo_meta
            WHEN 'repeticoes' THEN 'reps'
            WHEN 'distancia_km' THEN 'km'
            WHEN 'tempo_minutos' THEN 'min'
            WHEN 'dias_seguidos' THEN 'dias'
        END
    ) STORED,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_desafios_criador
        FOREIGN KEY (criador_id) REFERENCES usuarios(id)
        ON DELETE RESTRICT,
    CONSTRAINT chk_desafios_periodo
        CHECK (data_fim > data_inicio)
);

-- =========================================
-- TABELA DE PARTICIPAÇÕES
-- =========================================

CREATE TABLE participacoes_desafio (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    desafio_id INT NOT NULL,
    status ENUM('em_andamento', 'concluido', 'desistiu') DEFAULT 'em_andamento',
    concluido_em DATETIME DEFAULT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Evita que o mesmo usuário se inscreva duas vezes no mesmo desafio
    UNIQUE KEY uq_usuario_desafio (usuario_id, desafio_id),
    
    CONSTRAINT fk_participacoes_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_participacoes_desafio
        FOREIGN KEY (desafio_id) REFERENCES desafios(id)
        ON DELETE CASCADE
);

-- =========================================
-- TABELA HISTÓRICO DE PROGRESSO
-- =========================================

CREATE TABLE progresso (
    id INT AUTO_INCREMENT PRIMARY KEY,
    participacao_id INT NOT NULL,
    valor_registrado DECIMAL(10, 2) NOT NULL, -- Ex: correu 5.5 (km), fez 50 (flexões)
    observacao VARCHAR(255) DEFAULT NULL,
    data_registro DATE NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_progresso_participacao
        FOREIGN KEY (participacao_id) REFERENCES participacoes_desafio(id)
        ON DELETE CASCADE,
    CONSTRAINT chk_progresso_valor_positivo
        CHECK (valor_registrado > 0)
);

-- ===================================================
-- 1. POVOAMENTO DA TABELA: USUÁRIOS
-- ===================================================
INSERT INTO usuarios (id, nome, email, senha_hash, tipo) VALUES
(1, 'Admin', 'admin@gmail.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'admin'),
(2, 'Teste', 'teste@gmail.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'user'),
(3, 'Leandro', 'leandro@gmail.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'user'),
(4, 'Ruan', 'ruan@gmail.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'user');

-- ===================================================
-- 2. POVOAMENTO DA TABELA: DESAFIOS
-- ===================================================
INSERT INTO desafios (id, criador_id, nome, descricao, categoria, tipo_meta, meta_total, data_inicio, data_fim) VALUES
(1, 1, 'Desafio 100km em Setembro', 'Correr ou caminhar um total acumulado de 100km durante o mês de setembro.', 'corrida', 'distancia_km', 100.00, '2026-09-01', '2026-09-30'),
(2, 2, 'Clube das 1.000 Flexões', 'Complete mil flexões ao longo de 30 dias para fortalecer peitoral e tríceps.', 'flexoes', 'repeticoes', 1000.00, '2026-09-01', '2026-09-30'),
(3, 1, 'Pedal de Primavera', 'Pedalar pelo menos 250km até o fim do mês.', 'ciclismo', 'distancia_km', 250.00, '2026-09-05', '2026-09-30');

-- ===================================================
-- 3. POVOAMENTO DA TABELA: PARTICIPACOES_DESAFIO
-- ===================================================
INSERT INTO participacoes_desafio (id, usuario_id, desafio_id, status, concluido_em) VALUES
(1, 1, 1, 'em_andamento', NULL),
(2, 2, 1, 'em_andamento', NULL),
(3, 3, 1, 'em_andamento', NULL),
(4, 4, 2, 'em_andamento', NULL);

-- ===================================================
-- 4. POVOAMENTO DA TABELA: PROGRESSO (CHECK-INS)
-- ===================================================
INSERT INTO progresso (participacao_id, valor_registrado, observacao, data_registro) VALUES
(1, 6.50, 'Treino matinal no parque', '2026-09-02'),
(1, 5.00, 'Corrida leve na esteira', '2026-09-04'),
(1, 6.30, 'Tiro curto e ritmo forte', '2026-09-07'),
(2, 8.20, 'Longão de sábado na ciclovia', '2026-09-05'),
(2, 10.00, 'Treino com a assessoria', '2026-09-07'),
(3, 4.50, 'Primeiro treino pós-recuperação', '2026-09-03'),
(4, 50.00, '5 séries de 10 reps', '2026-09-02'),
(4, 50.00, 'Foco em amplitude', '2026-09-04'),
(4, 50.00, 'Última série até a falha', '2026-09-06');