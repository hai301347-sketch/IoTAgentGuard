CREATE DATABASE IF NOT EXISTS iot_guard DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE iot_guard;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    real_name VARCHAR(50),
    role VARCHAR(20) DEFAULT 'USER',
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT NOW(),
    update_time DATETIME DEFAULT NOW()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 设备信息表
CREATE TABLE IF NOT EXISTS iot_device (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    device_name VARCHAR(100) NOT NULL,
    device_code VARCHAR(50) NOT NULL UNIQUE,
    device_type VARCHAR(50),
    location VARCHAR(200),
    status VARCHAR(20) DEFAULT 'OFFLINE',
    description VARCHAR(500),
    create_time DATETIME DEFAULT NOW(),
    update_time DATETIME DEFAULT NOW()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 环境传感数据表
CREATE TABLE IF NOT EXISTS env_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    device_id BIGINT,
    device_code VARCHAR(50),
    temperature DECIMAL(5,2),
    humidity DECIMAL(5,2),
    light INT,
    smoke INT,
    collect_time DATETIME DEFAULT NOW(),
    INDEX idx_device_id(device_id),
    INDEX idx_collect_time(collect_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 设备告警记录表
CREATE TABLE IF NOT EXISTS alert_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    device_id BIGINT,
    device_code VARCHAR(50),
    alert_type VARCHAR(50),
    alert_level VARCHAR(20),
    alert_content VARCHAR(500),
    status TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT NOW(),
    handle_time DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 初始管理员账号由应用启动时自动创建 (admin / admin123)
-- 如需手动插入，请使用应用生成的BCrypt哈希值

-- 示例设备
INSERT INTO iot_device (device_name, device_code, device_type, location, status) VALUES
('温湿度传感器-A1', 'D001', '传感器', '教学楼A栋101室', 'ONLINE'),
('光照烟雾传感器-B2', 'D002', '传感器', '实验楼B栋202室', 'ONLINE'),
('环境监测网关-C1', 'D003', '网关', '实训中心C栋大厅', 'ONLINE');

-- 示例环境数据
INSERT INTO env_data (device_id, device_code, temperature, humidity, light, smoke, collect_time) VALUES
(1, 'D001', 25.50, 60.00, 500, 10, NOW()),
(1, 'D001', 26.30, 58.50, 520, 8, DATE_SUB(NOW(), INTERVAL 10 MINUTE)),
(2, 'D002', 24.80, 62.00, 480, 12, NOW()),
(2, 'D002', 38.50, 75.00, 300, 55, DATE_SUB(NOW(), INTERVAL 20 MINUTE)),
(3, 'D003', 23.00, 55.00, 600, 5, NOW());

-- 示例告警
INSERT INTO alert_record (device_id, device_code, alert_type, alert_level, alert_content, status) VALUES
(2, 'D002', '温度异常', 'HIGH', '设备D002温度达到38.5°C，超过预警阈值', 0),
(2, 'D002', '烟雾超标', 'CRITICAL', '设备D002烟雾浓度达到55，超过危险阈值', 0);
