CREATE TABLE catalog_instruments (
  id INT NOT NULL AUTO_INCREMENT, 
  url VARCHAR(255) NOT null,
  title VARCHAR(255) NOT null,
  codeItem VARCHAR(50),
  price double DEFAULT 0.0,  
  PRIMARY KEY (id)
) ;

CREATE TABLE all_instruments (
  
) ;