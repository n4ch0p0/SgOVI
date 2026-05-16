-- ==========================================================
-- SCRIPT SQL PER AL XHAT TÈCNIC - USUARI
-- Executar quan tingues accés a la base de dades
-- ==========================================================

-- 1. Taula de converses entre tècnic i usuari
CREATE TABLE ConversaTecnic (
    id_conversa_tecnic SERIAL PRIMARY KEY,
    dni_usuario VARCHAR(20) NOT NULL,
    data_inici TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_conv_tecnic_usuario FOREIGN KEY (dni_usuario) 
        REFERENCES UsuarioOVI(dni) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT unq_conv_tecnic_usuario UNIQUE (dni_usuario)
);

-- 2. Taula de missatges per a converses tècnic-usuari
CREATE TABLE MissatgeTecnic (
    id_missatge SERIAL PRIMARY KEY,
    id_conversa_tecnic INT NOT NULL,
    emissor VARCHAR(10) NOT NULL CHECK (emissor IN ('Usuari', 'Tecnic')),
    text_missatge TEXT NOT NULL,
    data_enviament TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_msg_conv_tecnic FOREIGN KEY (id_conversa_tecnic) 
        REFERENCES ConversaTecnic(id_conversa_tecnic) ON UPDATE CASCADE ON DELETE CASCADE
);

-- ==========================================================
-- INSERTS DE PROVA (Opcional)
-- ==========================================================

-- Conversa entre el tècnic i Anna (11111111A)
INSERT INTO ConversaTecnic (dni_usuario) VALUES ('11111111A');

-- Missatges d'exemple
INSERT INTO MissatgeTecnic (id_conversa_tecnic, emissor, text_missatge) VALUES
(1, 'Usuari', 'Hola, tinc un dubte sobre la meua sol·licitud.'),
(1, 'Tecnic', 'Bon dia Anna, digues-me, en què puc ajudar-te?'),
(1, 'Usuari', 'Volia saber si ja han revisat la meua petició d''assistència.'),
(1, 'Tecnic', 'Sí, ja ha sigut aprovada. Pots buscar assistents des del teu panell.');
