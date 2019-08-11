drop table if exists `flow`;
create table `flow`(
  `id` int(11) not null AUTO_INCREMENT,
  `sid` int(11),
  `index` int(100),
  `method` varchar(255),
  `params` json,
  primary key (`id`),
  foreign key (`sid`) references schema(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;