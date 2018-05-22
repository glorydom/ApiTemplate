-- ----------------------------
-- 春华秋实会议系统实体
-- ----------------------------

-- 会议
DROP TABLE IF EXISTS `MEETING_meeting`;
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
  `creationTimestamp` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代表一次会议';


# ----------------------------
# 演讲稿
# ----------------------------
DROP TABLE IF EXISTS `MEETING_ScriptManual`;
CREATE TABLE `MEETING_ScriptManual` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) DEFAULT NULL,
  `subTitle` varchar(100) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL COMMENT '该稿件的类别，比如是什么产品的',
  `ownerId` int(11) DEFAULT NULL COMMENT '该ID应该是participant的',
  `seminarId` int(11) DEFAULT NULL,
  `meetingId` int(11) DEFAULT NULL COMMENT '该ID应该是一届会议的',
  `attachmentManual` varchar(100) DEFAULT NULL COMMENT '演讲稿',
  `attachmentTranslated` varchar(100) DEFAULT NULL COMMENT '演讲稿翻译版',
  `creationTimestamp` bigint DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL COMMENT '状态包括：新建，催稿中，翻译中，检查中，排版中，印刷中，回发中，完毕',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议中，嘉宾使用的演讲稿';



#与会者
DROP TABLE IF EXISTS `MEETING_Participant`;
CREATE TABLE `MEETING_Participant` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(20) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `paidParticipant` varchar(10) DEFAULT NULL COMMENT '是否付费: 付费|非付费',
  `invoiced`  varchar(10) DEFAULT NULL COMMENT '是否开票: 已开票|未开票',
  `disguisedGuestCardContent` varchar(30) DEFAULT NULL COMMENT '嘉宾牌内容',
  `company` int(11) DEFAULT NULL COMMENT '所属的公司',
  `ticketId` varchar(100) DEFAULT NULL COMMENT '门票',
  `sponsor` varchar(50) DEFAULT NULL COMMENT '赞助商',
  `meetingId` int(11) DEFAULT NULL COMMENT '该ID应该是一届会议的',
  `creationTimestamp` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议中，嘉宾使用的演讲稿';

#与会者与专题讨论会的对应关系
DROP TABLE IF EXISTS `MEETING_Parti_Forum`;
CREATE TABLE `MEETING_Parti_Forum` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `participantId` int(11) DEFAULT NULL,
  `forumId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='与会者与专题讨论会的对应关系, 一个与会者参与多个专题讨论会，一个专题讨论会也需要多个与会者参与';


#专题讨论会forum
DROP TABLE IF EXISTS `MEETING_Forum`;
CREATE TABLE `MEETING_Forum` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `topic` varchar(100) DEFAULT NULL,
  `agenda` varchar(500) DEFAULT NULL COMMENT '征询嘉宾的到的问题汇总',
  `numOfQuestion` int(11) DEFAULT NULL COMMENT '问题的个数',
  `roundName` varchar(100) DEFAULT NULL COMMENT '专题讨论会的场次',
  `meetingId` int(11) DEFAULT NULL COMMENT '该ID应该是一届会议的',
  `creationTimestamp` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专题讨论会forum';




