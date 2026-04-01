CREATE TABLE tb_person_books (
  person_id INT NOT NULL,
  book_id INT NOT NULL,
  PRIMARY KEY (person_id, book_id),
  FOREIGN KEY (person_id) REFERENCES tb_person(id) ON DELETE CASCADE,
  FOREIGN KEY (book_id) REFERENCES tb_books(id) ON DELETE CASCADE
);