-- Création de séquence pour TICKETS (si vous utilisez une stratégie de séquence)
CREATE SEQUENCE TICKET_SEQ START WITH 1 INCREMENT BY 1 NOCACHE;

-- Table USERS
CREATE TABLE USERS (
                       ID NUMBER PRIMARY KEY,
                       USERNAME VARCHAR2(255) NOT NULL UNIQUE,
                       ROLE VARCHAR2(255)
);

-- Table CATEGORIES
CREATE TABLE CATEGORIES (
                            ID NUMBER PRIMARY KEY,
                            CATEGORY_NAME VARCHAR2(255) NOT NULL UNIQUE
);

-- Table TICKETS
CREATE TABLE TICKETS (
                         ID NUMBER PRIMARY KEY,
                         TITLE VARCHAR2(255) NOT NULL,
                         DESCRIPTION CLOB NOT NULL,
                         PRIORITY VARCHAR2(255),
                         CATEGORY VARCHAR2(255),
                         STATUS VARCHAR2(255),
                         CREATED_AT TIMESTAMP NOT NULL,
                         USER_ID NUMBER,
                         CATEGORY_ID NUMBER,
                         CONSTRAINT FK_TICKET_USER FOREIGN KEY (USER_ID) REFERENCES USERS(ID),
                         CONSTRAINT FK_TICKET_CATEGORY FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORIES(ID)
);

-- Table AUDIT_LOG
CREATE TABLE AUDIT_LOG (
                           ID NUMBER PRIMARY KEY,
                           ACTION VARCHAR2(255) NOT NULL,
                           ENTITY VARCHAR2(255) NOT NULL,
                           ENTITY_ID NUMBER NOT NULL,
                           DESCRIPTION VARCHAR2(255) NOT NULL,
                           TIMESTAMP TIMESTAMP NOT NULL,
                           DETAILS CLOB,
                           TICKET_ID NUMBER NOT NULL,
                           USER_ID NUMBER NOT NULL,
                           CONSTRAINT FK_AUDIT_TICKET FOREIGN KEY (TICKET_ID) REFERENCES TICKETS(ID),
                           CONSTRAINT FK_AUDIT_USER FOREIGN KEY (USER_ID) REFERENCES USERS(ID)
);

CREATE TABLE TICKET_COMMENTS (
                                 ID NUMBER PRIMARY KEY,
                                 TICKET_ID NUMBER,
                                 COMMENT_TEXT CLOB NOT NULL,
                                 CREATED_AT TIMESTAMP,
                                 CONSTRAINT FK_COMMENT_TICKET FOREIGN KEY (TICKET_ID) REFERENCES TICKETS(ID)
);
