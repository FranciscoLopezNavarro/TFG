-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: 127.0.0.1    Database: TFG
-- ------------------------------------------------------
-- Server version	5.5.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Alumno`
--

DROP TABLE IF EXISTS `Alumno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Alumno` (
  `idAlumno` int(11) NOT NULL,
  PRIMARY KEY (`idAlumno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Alumno`
--

LOCK TABLES `Alumno` WRITE;
/*!40000 ALTER TABLE `Alumno` DISABLE KEYS */;
/*!40000 ALTER TABLE `Alumno` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Asig_Cursadas`
--

DROP TABLE IF EXISTS `Asig_Cursadas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Asig_Cursadas` (
  `Alumno` int(11) NOT NULL,
  `Asignatura` int(11) NOT NULL,
  `Año` int(11) NOT NULL,
  PRIMARY KEY (`Alumno`,`Asignatura`,`Año`),
  KEY `FK_Asignatura_idx` (`Asignatura`),
  CONSTRAINT `FK_Alumno` FOREIGN KEY (`Alumno`) REFERENCES `Alumno` (`idAlumno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Asig_Cursadas`
--

LOCK TABLES `Asig_Cursadas` WRITE;
/*!40000 ALTER TABLE `Asig_Cursadas` DISABLE KEYS */;
/*!40000 ALTER TABLE `Asig_Cursadas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Asig_Impartidas`
--

DROP TABLE IF EXISTS `Asig_Impartidas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Asig_Impartidas` (
  `Profesor` int(11) NOT NULL,
  `Asignatura` int(11) NOT NULL,
  `Año` int(11) NOT NULL,
  PRIMARY KEY (`Profesor`,`Asignatura`,`Año`),
  KEY `FK_Asignatura_idx` (`Asignatura`),
  CONSTRAINT `FK_Profesor` FOREIGN KEY (`Profesor`) REFERENCES `Profesor` (`idProfesor`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Asig_Impartidas`
--

LOCK TABLES `Asig_Impartidas` WRITE;
/*!40000 ALTER TABLE `Asig_Impartidas` DISABLE KEYS */;
/*!40000 ALTER TABLE `Asig_Impartidas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Asignatura`
--

DROP TABLE IF EXISTS `Asignatura`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Asignatura` (
  `idAsignatura` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  `Curso` int(11) NOT NULL,
  PRIMARY KEY (`idAsignatura`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Asignatura`
--

LOCK TABLES `Asignatura` WRITE;
/*!40000 ALTER TABLE `Asignatura` DISABLE KEYS */;
/*!40000 ALTER TABLE `Asignatura` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Calificacion`
--

DROP TABLE IF EXISTS `Calificacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Calificacion` (
  `Alumno` int(11) NOT NULL,
  `Prueba` int(11) NOT NULL,
  `Nota` double NOT NULL,
  `Year` varchar(30) NOT NULL,
  PRIMARY KEY (`Alumno`,`Prueba`,`Nota`,`Year`),
  KEY `FK_Prueba_idx` (`Prueba`),
  CONSTRAINT `FK_Alumno_Calificacion` FOREIGN KEY (`Alumno`) REFERENCES `Alumno` (`idAlumno`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_Prueba_Calificacion` FOREIGN KEY (`Prueba`) REFERENCES `Prueba` (`idPrueba`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Calificacion`
--

LOCK TABLES `Calificacion` WRITE;
/*!40000 ALTER TABLE `Calificacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `Calificacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Profesor`
--

DROP TABLE IF EXISTS `Profesor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Profesor` (
  `idProfesor` int(11) NOT NULL,
  `Password` varchar(45) NOT NULL,
  `Nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`idProfesor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Profesor`
--

LOCK TABLES `Profesor` WRITE;
/*!40000 ALTER TABLE `Profesor` DISABLE KEYS */;
INSERT INTO `Profesor` VALUES (5713940,'1111','Paco');
/*!40000 ALTER TABLE `Profesor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Prueba`
--

DROP TABLE IF EXISTS `Prueba`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Prueba` (
  `idPrueba` int(11) NOT NULL AUTO_INCREMENT,
  `Titulo` varchar(45) NOT NULL,
  `Orden` int(11) NOT NULL,
  `N_min` double NOT NULL,
  `N_corte` double NOT NULL,
  `N_max` double NOT NULL,
  `Asignatura` int(11) NOT NULL,
  PRIMARY KEY (`idPrueba`),
  KEY `FK_Asignatura_idx` (`Asignatura`),
  CONSTRAINT `FK_Asignatura` FOREIGN KEY (`Asignatura`) REFERENCES `Asignatura` (`idAsignatura`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Prueba`
--

LOCK TABLES `Prueba` WRITE;
/*!40000 ALTER TABLE `Prueba` DISABLE KEYS */;
/*!40000 ALTER TABLE `Prueba` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RelacionesPruebas`
--

DROP TABLE IF EXISTS `RelacionesPruebas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RelacionesPruebas` (
  `Prueba1` int(11) NOT NULL,
  `Prueba2` int(11) NOT NULL,
  PRIMARY KEY (`Prueba1`,`Prueba2`),
  KEY `FK_Prueba2_idx` (`Prueba2`),
  CONSTRAINT `FK_Prueba1` FOREIGN KEY (`Prueba1`) REFERENCES `Prueba` (`idPrueba`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_Prueba2` FOREIGN KEY (`Prueba2`) REFERENCES `Prueba` (`idPrueba`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RelacionesPruebas`
--

LOCK TABLES `RelacionesPruebas` WRITE;
/*!40000 ALTER TABLE `RelacionesPruebas` DISABLE KEYS */;
/*!40000 ALTER TABLE `RelacionesPruebas` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-05-21 21:48:53
