#### 初始化sql
```sql
CREATE TABLE IF NOT EXISTS `statistics_records`(
   `id` INT  AUTO_INCREMENT,
   `type` int(1) NOT NULL,
   `name` VARCHAR(16) NOT NULL,
   PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
```