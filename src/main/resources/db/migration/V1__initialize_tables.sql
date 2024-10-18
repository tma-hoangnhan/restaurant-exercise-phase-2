CREATE TABLE IF NOT EXISTS `item` (
  `id` INT NOT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `img` VARCHAR(255) NULL DEFAULT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `price` DOUBLE NOT NULL,
  `state` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE IF NOT EXISTS `drink` (
  `volume` INT NOT NULL,
  `id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_drink_to_item`
    FOREIGN KEY (`id`)
    REFERENCES `restaurant`.`item` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE IF NOT EXISTS `alcohol` (
  `id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_alcohol_to_drink`
    FOREIGN KEY (`id`)
    REFERENCES `restaurant`.`drink` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE IF NOT EXISTS `soft_drink` (
  `id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_soft_drink_to_drink`
    FOREIGN KEY (`id`)
    REFERENCES `restaurant`.`drink` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE IF NOT EXISTS `food` (
  `id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_food_to_item`
    FOREIGN KEY (`id`)
    REFERENCES `restaurant`.`item` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE IF NOT EXISTS `breakfast` (
  `id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_breakfast_to_food`
    FOREIGN KEY (`id`)
    REFERENCES `restaurant`.`food` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE IF NOT EXISTS `lunch` (
  `id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_lunch_to_food`
    FOREIGN KEY (`id`)
    REFERENCES `restaurant`.`food` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE IF NOT EXISTS `dinner` (
  `id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_dinner_to_food`
    FOREIGN KEY (`id`)
    REFERENCES `restaurant`.`food` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE IF NOT EXISTS `bill` (
  `id` INT NOT NULL,
  `ordered_time` DATETIME(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE IF NOT EXISTS `order_item` (
  `id` INT NOT NULL,
  `quantity` INT NOT NULL,
  `bill_id` INT NULL DEFAULT NULL,
  `item_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_order_item_to_bill`
    FOREIGN KEY (`bill_id`)
    REFERENCES `restaurant`.`bill` (`id`),
  CONSTRAINT `FK_order_item_to_item`
    FOREIGN KEY (`item_id`)
    REFERENCES `restaurant`.`item` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;
