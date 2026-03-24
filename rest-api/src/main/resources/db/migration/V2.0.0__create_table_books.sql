CREATE TABLE tb_books (
  id SERIAL PRIMARY KEY,
  author VARCHAR(120),
  launch_date DATE NOT NULL,
  price NUMERIC(6,2) NOT NULL,
  title VARCHAR(120)
);
