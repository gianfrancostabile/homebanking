use testpalermo;
CREATE TABLE Clients(
	id BIGINT AUTO_INCREMENT PRIMARY KEY, 
    name varchar(60), 
    lastname varchar(60)
);
CREATE TABLE Products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_id BIGINT,
    alias VARCHAR(150) UNIQUE KEY,
    cbu VARCHAR(22) UNIQUE KEY, 
    type VARCHAR(150),
    balance DOUBLE,
    creation_date DATE,
    FOREIGN KEY(client_id) REFERENCES Clients(id) ON DELETE CASCADE
);
CREATE TABLE Cards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT,
    brand VARCHAR(150),
    type VARCHAR(150),
    card_number VARCHAR(32) UNIQUE KEY,
    security_code VARCHAR(3),
    expiration_date VARCHAR(60),
    owner_name VARCHAR(150),
    available_debt_balance DOUBLE,
    debt_balance DOUBLE,
    FOREIGN KEY(product_id) REFERENCES Products(id) ON DELETE CASCADE
);
CREATE TABLE Transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    source_product_id BIGINT,
    destination_product_id BIGINT,
    card_id BIGINT,
    type VARCHAR(150),
    payment_method VARCHAR(150),
    currency VARCHAR(25),
    amount DOUBLE,
    creation_date DATETIME,
    FOREIGN KEY(source_product_id) REFERENCES Products(id) ON DELETE CASCADE,
    FOREIGN KEY(destination_product_id) REFERENCES Products(id) ON DELETE CASCADE,
    FOREIGN KEY(card_id) REFERENCES Cards(id) ON DELETE CASCADE
);

DELIMITER //
CREATE TRIGGER before_insert_product
BEFORE INSERT ON Products
FOR EACH ROW
BEGIN
    DECLARE next_cbu INT;
    DECLARE client_name VARCHAR(150);
    DECLARE type_suffix VARCHAR(5);
    DECLARE base_alias VARCHAR(150);
    DECLARE max_suffix INT;

    SELECT IFNULL(MAX(CAST(cbu AS UNSIGNED)), 0) + 1 INTO next_cbu FROM Products;
    SET NEW.cbu = LPAD(next_cbu, 22, '0');

    SELECT CONCAT(LOWER(name), '.', LOWER(lastname)) INTO client_name 
    FROM Clients 
    WHERE id = NEW.client_id;

    SET type_suffix = CASE NEW.type
        WHEN 'CUENTA_CORRIENTE' THEN 'cc'
        WHEN 'CAJA_DE_AHORRO' THEN 'ca'
        WHEN 'CAJA_DE_AHORRO_DOLARES' THEN 'cad'
        ELSE 'prod'
    END;

    SET base_alias = CONCAT(client_name, '.', type_suffix);

    IF EXISTS (SELECT 1 FROM Products WHERE alias = base_alias) THEN
        SELECT MAX(CAST(SUBSTRING_INDEX(alias, '.', -1) AS UNSIGNED)) INTO max_suffix
        FROM Products
        WHERE alias REGEXP CONCAT('^', base_alias, '\\.[0-9]+$');
        
        IF max_suffix IS NULL THEN
            SET NEW.alias = CONCAT(base_alias, '.1');
        ELSE
            SET NEW.alias = CONCAT(base_alias, '.', max_suffix + 1);
        END IF;
        
    ELSE
        SET NEW.alias = base_alias;
    END IF;
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER before_insert_card
BEFORE INSERT ON Cards
FOR EACH ROW
BEGIN
    DECLARE card_prefix VARCHAR(8);
    DECLARE next_sequence INT;

    SET card_prefix = NEW.card_number;

    SELECT IFNULL(MAX(CAST(SUBSTRING(card_number, 9, 8) AS UNSIGNED)), 0) + 1 
    INTO next_sequence 
    FROM Cards
    WHERE card_number LIKE CONCAT(card_prefix, '%');

    SET NEW.card_number = CONCAT(card_prefix, LPAD(next_sequence, 8, '0'));
END//
DELIMITER ;