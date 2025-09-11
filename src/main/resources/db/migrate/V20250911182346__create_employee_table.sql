create database oioio;
use oioio;
-- 创建员工表
CREATE TABLE employee (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(50) NOT NULL,
                          age INT,
                          salary DOUBLE NOT NULL DEFAULT 0,
                          gender VARCHAR(10),
                          company_id BIGINT,
                          active_status INT NOT NULL DEFAULT 1
);
