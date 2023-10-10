use freedb_fds_09;
drop table if exists  Gallery;

CREATE TABLE `freedb_fds_09`.`Gallery` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `title` VARCHAR(45) NOT NULL,
  `description` VARCHAR(45) NOT NULL,
  `updatedAt` DATETIME NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE);

INSERT INTO Gallery(name,title,description,updatedAt) VALUES
('photo1.jpg','photo1','second photo of gallery',now()),
('photo2.jpg','photo2','second photo of gallery',now()),
('photo3.jpg','photo3','third photo of gallery',now()),
('photo4.jpg','photo4','forth photo of gallery',now()),
('photo5.jpg','photo5','fifth photo of gallery',now()),
('photo6.jpg','photo6','sixth photo of gallery',now());

