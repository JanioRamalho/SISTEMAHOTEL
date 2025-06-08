CREATE DATABASE  IF NOT EXISTS `sistema_hotel` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `sistema_hotel`;
-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: sistema_hotel
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cardapio`
--

DROP TABLE IF EXISTS `cardapio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cardapio` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  `valor` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cardapio`
--

LOCK TABLES `cardapio` WRITE;
/*!40000 ALTER TABLE `cardapio` DISABLE KEYS */;
INSERT INTO `cardapio` VALUES (1,'Hamburguer','Hamburguer com queijo e bacon',25.00),(2,'Salada Caesar','Salada com alface, croutons e molho especial',20.00),(3,'Suco Natural','Suco de laranja natural',10.00),(4,'Pizza Portuguesa','Pizza com presunto, ovos e azeitonas',40.00),(5,'Cafe Expresso','Cafe expresso tradicional',8.00);
/*!40000 ALTER TABLE `cardapio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categorias_quartos`
--

DROP TABLE IF EXISTS `categorias_quartos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias_quartos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `tipo_quarto` enum('standard','luxo','suite','vip') NOT NULL DEFAULT 'standard',
  `descricao` text,
  `preco_base` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias_quartos`
--

LOCK TABLES `categorias_quartos` WRITE;
/*!40000 ALTER TABLE `categorias_quartos` DISABLE KEYS */;
INSERT INTO `categorias_quartos` VALUES (1,'Standard','standard','Quarto confortável para até 2 pessoas',150.00),(2,'Luxo','luxo','Quarto com vista para o mar e varanda',300.00),(3,'Suíte','suite','Suíte espaçosa com sala de estar',450.00),(4,'VIP','vip','Quarto com serviço exclusivo e jacuzzi',700.00),(5,'Economico','standard','Quarto simples e econômico',100.00);
/*!40000 ALTER TABLE `categorias_quartos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `senha` varchar(255) NOT NULL,
  `cpf` varchar(14) NOT NULL,
  `telefone` varchar(20) NOT NULL,
  `email` varchar(70) NOT NULL,
  `data_nascimento` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cpf` (`cpf`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (1,'Joao','Joao123','123.456.789-00','(11)98765-4321','joao@gmail.com','1985-04-12'),(2,'Maria','Maria123','987.654.321-00','(21)91234-5678','maria@gmail.com','1990-07-23'),(3,'Lucas','Lucas123','111.222.333-44','(31)99888-7766','lucas@gmail.com','1988-05-16'),(4,'Ana','Ana123','555.666.777-88','(41)99977-6655','ana@gmail.com','1992-08-05'),(5,'Pedro','Pedro123','999.888.777-66','(51)97766-5544','pedro@gmail.com','1995-11-30'),(6,'Janio Ramalho','Janio123','111.222.333-93','(84) 912213324','janio@gmail.com',NULL);
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conta_restaurante`
--

DROP TABLE IF EXISTS `conta_restaurante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conta_restaurante` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_reserva` int NOT NULL,
  `data_pedido` date NOT NULL,
  `valor_total` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_reserva` (`id_reserva`),
  CONSTRAINT `conta_restaurante_ibfk_1` FOREIGN KEY (`id_reserva`) REFERENCES `reservas` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conta_restaurante`
--

LOCK TABLES `conta_restaurante` WRITE;
/*!40000 ALTER TABLE `conta_restaurante` DISABLE KEYS */;
INSERT INTO `conta_restaurante` VALUES (1,1,'2025-06-11',55.00),(2,2,'2025-07-02',35.00),(3,3,'2025-06-21',60.00),(4,4,'2025-08-12',25.00),(5,5,'2025-09-03',45.00);
/*!40000 ALTER TABLE `conta_restaurante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `funcionario`
--

DROP TABLE IF EXISTS `funcionario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `funcionario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `cargo` varchar(30) NOT NULL,
  `login` varchar(255) NOT NULL,
  `senha` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `funcionario`
--

LOCK TABLES `funcionario` WRITE;
/*!40000 ALTER TABLE `funcionario` DISABLE KEYS */;
INSERT INTO `funcionario` VALUES (1,'Carlos','Recepcionista','carlos','Carlos1234'),(2,'Ana','Gerente','ana','Ana1234'),(3,'Paulo','Limpeza','paulo','Paulo1234'),(4,'Sofia','Cozinha','sofia','Sofia1234'),(5,'Rafael','Seguranca','rafael','Rafael1234');
/*!40000 ALTER TABLE `funcionario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pagamentos`
--

DROP TABLE IF EXISTS `pagamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pagamentos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_reserva` int NOT NULL,
  `valor` decimal(10,2) NOT NULL,
  `data_pagamento` date NOT NULL,
  `metodo` varchar(70) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_reserva` (`id_reserva`),
  CONSTRAINT `pagamentos_ibfk_1` FOREIGN KEY (`id_reserva`) REFERENCES `reservas` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pagamentos`
--

LOCK TABLES `pagamentos` WRITE;
/*!40000 ALTER TABLE `pagamentos` DISABLE KEYS */;
INSERT INTO `pagamentos` VALUES (1,1,750.00,'2025-06-01','PIX'),(2,2,1800.00,'2025-06-20','CartaoCredito'),(3,3,280.00,'2025-06-19','Dinheiro'),(4,4,1500.00,'2025-08-01','Boleto'),(5,5,3500.00,'2025-08-25','CartaoDebito'),(6,8,2340.00,'2025-06-05','Cartão de Crédito'),(7,9,1190.00,'2025-06-05','Cartão de Crédito'),(10,12,1635.00,'2025-06-05','Cartão de Crédito'),(11,13,660.00,'2025-06-05','Cartão de Crédito'),(12,14,1620.00,'2025-06-05','Cartão de Crédito'),(13,20,670.00,'2025-06-06','Cartão'),(14,21,1680.00,'2025-06-06','Cartão');
/*!40000 ALTER TABLE `pagamentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quartos`
--

DROP TABLE IF EXISTS `quartos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quartos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `numero` int NOT NULL,
  `tipo` varchar(50) NOT NULL,
  `situacao` varchar(20) NOT NULL,
  `valor_diaria` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `numero` (`numero`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quartos`
--

LOCK TABLES `quartos` WRITE;
/*!40000 ALTER TABLE `quartos` DISABLE KEYS */;
INSERT INTO `quartos` VALUES (1,101,'standard','reservado',150.00),(2,102,'luxo','ocupado',300.00),(3,201,'suite','reservado',450.00),(4,301,'vip','manutencao',700.00),(5,103,'standard','reservado',140.00),(6,111,'standard','reservado',150.00),(7,112,'standard','ocupado',155.00),(8,113,'standard','Disponível',145.00),(9,114,'standard','Ocupado',160.00),(10,115,'standard','Ocupado',150.00),(11,202,'suite','Ocupado',480.00),(12,203,'suite','Ocupado',475.00),(13,204,'suite','Ocupado',490.00),(14,208,'suite','Ocupado',500.00),(15,209,'suite','Ocupado',485.00),(16,210,'suite','Ocupado',495.00),(17,310,'vip','Ocupado',720.00),(18,311,'vip','Ocupado',730.00),(19,312,'vip','disponivel',750.00),(20,313,'vip','disponivel',740.00),(21,314,'vip','disponivel',710.00),(22,315,'vip','disponivel',760.00),(23,116,'standard','Ocupado',150.00),(24,117,'standard','disponivel',155.00),(25,118,'standard','disponivel',145.00);
/*!40000 ALTER TABLE `quartos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservas`
--

DROP TABLE IF EXISTS `reservas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_quarto` int NOT NULL,
  `id_cliente` int NOT NULL,
  `data_checkin` date NOT NULL,
  `data_checkout` date NOT NULL,
  `valor_total` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_quarto` (`id_quarto`),
  KEY `id_cliente` (`id_cliente`),
  CONSTRAINT `reservas_ibfk_1` FOREIGN KEY (`id_quarto`) REFERENCES `quartos` (`id`),
  CONSTRAINT `reservas_ibfk_2` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservas`
--

LOCK TABLES `reservas` WRITE;
/*!40000 ALTER TABLE `reservas` DISABLE KEYS */;
INSERT INTO `reservas` VALUES (1,1,1,'2025-06-10','2025-06-15',750.00),(2,3,2,'2025-07-01','2025-07-05',1800.00),(3,5,3,'2025-06-20','2025-06-22',280.00),(4,2,4,'2025-08-10','2025-08-15',1500.00),(5,4,5,'2025-09-01','2025-09-05',3500.00),(6,3,1,'2025-06-10','2025-06-12',900.00),(7,17,1,'2027-02-10','2027-02-13',2340.00),(8,17,1,'2027-02-10','2027-02-13',2340.00),(9,12,1,'2029-03-20','2029-03-22',1190.00),(12,15,6,'2027-09-09','2027-09-12',1635.00),(13,9,6,'2027-09-09','2027-09-12',660.00),(14,11,6,'2027-09-09','2027-09-12',1620.00),(15,14,6,'2029-04-12','2029-04-15',1720.00),(16,13,6,'2029-10-01','2029-10-04',1690.00),(17,13,6,'2029-10-01','2029-10-04',1690.00),(18,18,6,'2030-10-10','2030-10-15',3840.00),(19,16,6,'2026-03-20','2026-03-24',2010.00),(20,23,6,'2025-07-10','2025-07-13',670.00),(21,10,6,'1000-10-10','1000-10-20',1680.00);
/*!40000 ALTER TABLE `reservas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servicos_adicionais`
--

DROP TABLE IF EXISTS `servicos_adicionais`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `servicos_adicionais` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  `valor` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servicos_adicionais`
--

LOCK TABLES `servicos_adicionais` WRITE;
/*!40000 ALTER TABLE `servicos_adicionais` DISABLE KEYS */;
INSERT INTO `servicos_adicionais` VALUES (1,'Lavanderia','Serviço de lavagem e passagem de roupas',50.00),(2,'Translado Aeroporto','Transporte de ida e volta ao aeroporto',100.00),(3,'Cafe da Manha','Café da manhã servido no quarto',30.00),(4,'Estacionamento','Vaga coberta para o carro',40.00),(5,'Wi-Fi Premium','Internet de alta velocidade no quarto',20.00);
/*!40000 ALTER TABLE `servicos_adicionais` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-08 18:04:46
