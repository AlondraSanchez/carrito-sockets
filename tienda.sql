-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema tienda
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema tienda
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `tienda` DEFAULT CHARACTER SET latin1 ;
USE `tienda` ;

-- -----------------------------------------------------
-- Table `tienda`.`producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tienda`.`producto` (
  `nombre` VARCHAR(100) NULL DEFAULT NULL,
  `cantidad` INT(11) NULL DEFAULT NULL,
  `urlImagen` VARCHAR(500) NULL DEFAULT NULL,
  `precio` DOUBLE NULL DEFAULT NULL,
  `descripcion` VARCHAR(100) NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
KEY_BLOCK_SIZE = 16;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `tienda`.`producto`
-- -----------------------------------------------------
START TRANSACTION;
USE `tienda`;
INSERT INTO `tienda`.`producto` (`nombre`, `cantidad`, `urlImagen`, `precio`, `descripcion`) VALUES ('Chetos flaming hot', 100, 'https://www.superama.com.mx/Content/images/products/img_large/0750101113093L.jpg', 10, 'cheetos picosos');
INSERT INTO `tienda`.`producto` (`nombre`, `cantidad`, `urlImagen`, `precio`, `descripcion`) VALUES ('Takis fuego', 200, 'https://www.chedraui.com.mx/medias/7501030424536-00-CH515Wx515H?context=bWFzdGVyfHJvb3R8NDMwNTR8aW1hZ2UvanBlZ3xoNWYvaDIxLzEwNjc2NjE2ODIyODE0LmpwZ3wzM2E2YjU0OTQ3NThkM2ZmZjhlMjZlY2Y5YjM5YTYwMDhlYzcwMmFjZWNmNWFhODZhZjRjODE1YzMzZGYxOTM3', 12, 'takis picosos');
INSERT INTO `tienda`.`producto` (`nombre`, `cantidad`, `urlImagen`, `precio`, `descripcion`) VALUES ('Coca-Cola', 500, 'https://www.chedraui.com.mx/medias/75007614-00-CH1200Wx1200H?context=bWFzdGVyfHJvb3R8MTA5MDEwfGltYWdlL2pwZWd8aDI3L2hjZS8xMDE1MDY3NDM5OTI2Mi5qcGd8NjY1OTJlNmZiNzU2OWVkYmFjNjg4ZTlmNTY0NzNiNTkzOWFjZGYzOWMxNTIxZTJmOGIyMzc3Mjc1MGU2NTFkMA', 15, 'Bebida refrescante sabor cola');
INSERT INTO `tienda`.`producto` (`nombre`, `cantidad`, `urlImagen`, `precio`, `descripcion`) VALUES ('sprite', 50, 'https://www.sixtogo.com.mx/media/catalog/product/1/2/12959_2.jpg', 14.50, 'Bebida refrescante sabor lima-limon');

COMMIT;

