CREATE TABLE tb_users (
  id SERIAL NOT NULL,
  user_name varchar(255) DEFAULT NULL,
  full_name varchar(255) DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  account_non_expired BOOLEAN DEFAULT NULL,
  account_non_locked BOOLEAN DEFAULT NULL,
  credentials_non_expired BOOLEAN DEFAULT NULL,
  enabled BOOLEAN DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uk_user_name UNIQUE (user_name)
);