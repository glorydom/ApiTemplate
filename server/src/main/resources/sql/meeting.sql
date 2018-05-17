-- ----------------------------
-- 春华秋实会议系统实体
-- ----------------------------

CREATE TABLE `MEETING_meeting` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `meetingSubject` varchar(100) DEFAULT NULL,
  `introduction` varchar(500) DEFAULT NULL,
  `beginAt` date DEFAULT NULL,
  `endAt` date DEFAULT NULL,
  `organizer` varchar(255) DEFAULT NULL,
  `participantNumber` int(11) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `contactInfo` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代表一次会议';

