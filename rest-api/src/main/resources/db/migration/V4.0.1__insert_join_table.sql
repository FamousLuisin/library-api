INSERT INTO tb_person_books (person_id, book_id)
SELECT 
    p.id AS person_id, 
    b.id AS book_id
FROM 
    (SELECT id FROM tb_person WHERE id <= 12) p
CROSS JOIN
    (SELECT id FROM tb_books ORDER BY RANDOM() LIMIT 20) b;

INSERT INTO tb_person_books (person_id, book_id)
SELECT 
    p.id AS person_id, 
    b.id AS book_id
FROM 
    (SELECT id FROM tb_person WHERE id > 12) p
CROSS JOIN
    (SELECT id FROM tb_books ORDER BY RANDOM() LIMIT 5) b;