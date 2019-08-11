drop table if exists `schema`;
create table `schema`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(25),
  `create_by` int(11),
  PRIMARY KEY (`id`),
  unique (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;