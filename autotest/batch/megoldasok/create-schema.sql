create schema if not exists locations character set utf8 collate utf8_hungarian_ci;
CREATE USER if not exists 'locations'@'localhost' IDENTIFIED BY 'locations';
use locations;
GRANT ALL PRIVILEGES ON locations.* TO 'locations'@'localhost';