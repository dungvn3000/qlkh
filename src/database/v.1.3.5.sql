SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

ALTER TABLE `material_group` ADD `position` INT NOT NULL;

TRUNCATE TABLE `material_group`;

INSERT INTO `material_group` (`id`, `codeDisplay`, `createdDate`, `updatedDate`, `createBy`, `updateBy`, `name`, `code`, `regex`, `position`) VALUES
(14, 'SF1', '2013-04-29', '2013-07-29', 1, 1, 'ĐƯỜNG TRUYỀN TẢI', 'SF1', 'SF1.*', 3),
(15, 'I', '2013-04-29', '2013-07-26', 1, 1, 'ĐỊNH KỲ', 'SF1.I', '1.1*,1.2*,1.3*,1.4*', 4),
(16, 'II', '2013-04-29', '2013-07-26', 1, 1, 'KHÔNG ĐỊNH KỲ', 'SF1.II', '1.5*,1.6*', 5),
(17, 'SF2', '2013-04-29', '2013-07-29', 1, 1, 'TRẠM TỔNG ĐÀI', 'SF2', 'SF2.*', 6),
(18, 'I', '2013-04-29', '2013-07-13', 1, 1, 'ĐỊNH KỲ', 'SF2.I', '2.1*,2.2*,2.3*,2.4*', 7),
(19, 'II', '2013-04-29', '2013-04-29', 1, 1, 'KHÔNG ĐỊNH KỲ', 'SF2.II', '2.5*,2.6*', 8),
(20, 'SF3', '2013-04-29', '2013-07-29', 1, 1, 'TÍN HIỆU RA VÀO GA', 'SF3', 'SF3.*', 9),
(21, 'I', '2013-04-29', '2013-04-29', 1, 1, 'ĐỊNH KỲ', 'SF3.I', '3.3*,3.4*', 10),
(22, 'II', '2013-04-29', '2013-04-29', 1, 1, 'KHÔNG ĐỊNH KỲ', 'SF3.II', '3.5*,3.6*', 11),
(23, 'SF4', '2013-04-29', '2013-04-29', 1, 1, 'THIẾT BỊ KHỐNG CHẾ', 'SF4', 'SF4.*', 12),
(24, 'I', '2013-04-29', '2013-04-29', 1, 1, 'ĐỊNH KỲ', 'SF4.I', '4.3*,4.4*', 13),
(25, 'II', '2013-04-29', '2013-04-29', 1, 1, 'KHÔNG ĐỊNH KỲ', 'SF4.II', '4.5*,4.6*', 14),
(26, 'SF5', '2013-04-29', '2013-04-29', 1, 1, 'THIẾT BỊ ĐIỀU KHIỂN', 'SF5', 'SF5.*', 15),
(27, 'I', '2013-04-29', '2013-04-29', 1, 1, 'ĐỊNH KỲ', 'SF5.I', '5.3*,5.4*', 16),
(28, 'II', '2013-04-29', '2013-04-29', 1, 1, 'KHÔNG ĐỊNH KỲ', 'SF5.II', '5.5*,5.6*', 17),
(29, 'SF6', '2013-04-29', '2013-07-29', 1, 1, 'CÁP TÍN HIỆU', 'SF6', 'SF6.*', 18),
(30, 'I', '2013-04-29', '2013-04-29', 1, 1, 'ĐỊNH KỲ', 'SF6.I', '6.3*,6.4*', 19),
(31, 'II', '2013-04-29', '2013-04-29', 1, 1, 'KHÔNG ĐỊNH KỲ', 'SF6.II', '6.5*,6.6*', 20),
(32, 'SF7', '2013-04-29', '2013-07-29', 1, 1, 'THIẾT BỊ NGUỒN ĐIỆN', 'SF7', 'SF7.*', 21),
(33, 'I', '2013-04-29', '2013-04-29', 1, 1, 'ĐỊNH KỲ', 'SF7.I', '7.3*,7.4*', 22),
(34, 'II', '2013-04-29', '2013-04-29', 1, 1, 'KHÔNG ĐỊNH KỲ', 'SF7.II', '7.5*,7.6*', 23),
(35, 'SF10', '2013-05-04', '2013-05-04', 1, 1, 'BỔ TRỢ SẢN XUẤT', 'SF10', 'SF10.*', 25),
(36, 'II', '2013-05-04', '2013-05-04', 1, 1, 'KHÔNG ĐỊNH KỲ', 'SF10.II', '10.4*,10.5*,10.6*', 26),
(37, 'SF8', '2013-05-04', '2013-05-05', 1, 1, 'CHI PHI', 'SF8', NULL, 24),
(38, 'SF11', '2013-06-17', '2013-06-17', 1, 1, 'NHIÊN LIỆU CHẠY MÁY NỔ', 'SF11', NULL, 27),
(40, 'SF12', '2013-06-17', '2013-06-17', 1, 1, 'TRỰC TIẾP PHÍ KHÁC', 'SF12', NULL, 28),
(41, 'SF13', '2013-06-18', '2013-07-15', 1, 1, 'CHI PHÍ CHUNG', 'SF13', NULL, 29),
(42, 'SF13.1', '2013-06-18', '2013-06-18', 1, 1, 'VĂN PHÒNG PHẨM', 'SF13.1', NULL, 30),
(43, 'SF13.2', '2013-06-18', '2013-06-18', 1, 1, 'BẢO HỘ LAO ĐỘNG', 'SF13.2', NULL, 31),
(44, 'SF13.3', '2013-06-18', '2013-06-18', 1, 1, 'PHÒNG CHÁY CHỮA CHÁY', 'SF13.3', NULL, 32),
(45, 'A', '2013-08-02', '2013-08-02', 1, 1, 'TỔNG KẾ HOẠCH GIAO', 'A', 'A.*', 0),
(46, 'I', '2013-08-02', '2013-08-02', 1, 1, 'ĐỊNH KỲ', 'A.I', 'SF*.I', 1),
(47, 'II', '2013-08-02', '2013-08-02', 1, 1, 'KHÔNG ĐỊNH KỲ', 'A.II', 'SF*.II', 2);

SET FOREIGN_KEY_CHECKS=1;