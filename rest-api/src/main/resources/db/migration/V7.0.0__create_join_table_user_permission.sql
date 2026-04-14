CREATE TABLE tb_user_permission (
  id_user INT NOT NULL,
  id_permission INT NOT NULL,
  PRIMARY KEY (id_user, id_permission),
  CONSTRAINT fk_user_permission FOREIGN KEY (id_user) REFERENCES tb_users (id),
  CONSTRAINT fk_user_permission_permission FOREIGN KEY (id_permission) REFERENCES tb_permission (id)
);