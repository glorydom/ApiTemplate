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
  `meetingId` int(11) DEFAULT NULL COMMENT '该ID就是会议的BussinessKey的后半部分',
  `attachmentManual` varchar(100) DEFAULT NULL COMMENT '演讲稿',
  `attachmentTranslated` varchar(100) DEFAULT NULL COMMENT '演讲稿翻译版',
  `creationTimestamp` bigint DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL COMMENT '状态包括：新建，催稿中，翻译中，检查中，排版中，印刷中，回发中，完毕',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议中，嘉宾使用的演讲稿';



#与会人员
DROP TABLE IF EXISTS `MEETING_Participant`;
CREATE TABLE `MEETING_Participant` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL COMMENT '人员名字',
  `gender` varchar(5)  COMMENT '性别',
  `telephone` varchar(20)  COMMENT 'telphone',
  `age` int(11)  COMMENT 'age',
  `address` varchar(40)  COMMENT 'address',
  `productOfInterest` varchar(100) NOT NULL COMMENT '感兴趣的产品， 使用逗号分隔',
  `hotel` varchar(10) COMMENT '酒店名',
  `hotelAddress` varchar(10) COMMENT '酒店地址',
  `hotelStatus` varchar(10)  COMMENT '没有 | 进行中 | 结束',
  `arrivalTime` date COMMENT '到达时间',
  `leaveTime` date COMMENT '离开时间',
  `trafficTool` varchar(10) COMMENT '交通工具',
  `recipiantStatus` varchar(10)  COMMENT '接人状态：没有 | 进行中 | 结束',
  `seatStatus` varchar(10)  COMMENT '会场座位是否已经安排：没有 | 进行中 | 结束',
  `seat` varchar(20)  COMMENT '会场中的位置  横纵坐标',
  `forumOfInterest` varchar(100) DEFAULT NULL COMMENT '参加那几场专题会, 专场会（forum)的id使用逗号分割，',
  `charged` varchar(10) DEFAULT NULL COMMENT '是否付费: 付费|非付费',
  `invoiced`  varchar(10) DEFAULT NULL COMMENT '是否开票: 已开票|未开票',
  `disguisedGuestCardContent` varchar(30) DEFAULT NULL COMMENT '嘉宾牌内容',
  `company` varchar(50) DEFAULT NULL COMMENT '所属的公司',
  `meetingId` int(11) DEFAULT NULL COMMENT '参加的是哪一届会议',
  `ticketId` varchar(100) DEFAULT NULL COMMENT '门票',
  `sponsor` varchar(50) DEFAULT NULL COMMENT '赞助商',
  `creationTimestamp` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='与会嘉宾';


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


#会前物品准备清单
DROP TABLE IF EXISTS `MEETING_Preparation_Item`;
CREATE TABLE `MEETING_Preparation_Item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item` varchar(100) DEFAULT NULL COMMENT '事项',
  `itemDescription` varchar(500) DEFAULT NULL COMMENT '事项说明',
  `amount` int(11) DEFAULT NULL COMMENT '物品数量',
  `price` float DEFAULT NULL COMMENT '物品单价',
  `unit` varchar(10) DEFAULT NULL COMMENT '单位',
  `attachment`  varchar(100) DEFAULT NULL COMMENT '附件',
  `creationTimestamp` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专题讨论会forum';


#专场论坛征询10个问题
DROP TABLE IF EXISTS `MEETING_Topic`;
CREATE TABLE `MEETING_Topic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `topic` varchar(100) DEFAULT NULL COMMENT '话题',
  `details` varchar(500) DEFAULT NULL COMMENT '',
  `proposer` int(11) DEFAULT NULL COMMENT '提出者',
  `status` varchar(20) DEFAULT NULL COMMENT '状态：提出 | 审核通过 | 审核不通过 | 上传至问答平台',
  `meetingId` int(11) NOT NULL COMMENT '与该界会议关联',
  `forumId` int(11) NOT NULL COMMENT '与该专题研讨会关联',
  `creationTimestamp` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专场论坛征询10个问题';


#任务执行者候选人
DROP TABLE IF EXISTS `MEETING_Task_Candidate`;
CREATE TABLE `MEETING_Task_Candidate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `meetingId` int(11) NULL COMMENT '与该界会议关联',
  `taskId` varchar(64) NOT NULL COMMENT '流程任务ID',
  `userId` int(11) NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务执行者候选人';


#通用任务模型
DROP TABLE IF EXISTS `MEETING_Common_Task`;
CREATE TABLE `MEETING_Common_Task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `taskType` varchar(40) NOT NULL COMMENT '任务类型，应该包含固定个数的类型，比如准备材料， 上传问题到平台',
  `activitiTaskId` varchar(20) DEFAULT NULL COMMENT 'Activiti流程使用的id',
  `taskStatus` varchar(10) DEFAULT NULL COMMENT '新建 | 进行中 | 结束',
  `taskViewers` varchar(500) DEFAULT NULL COMMENT 'userId 使用逗号分割',
  `taskOwner` varchar(100) DEFAULT NULL COMMENT '任务所有者 userId 使用逗号分割',
  `taskExecutors` varchar(100) DEFAULT NULL COMMENT 'userId 使用逗号分割',
  `taskDescription` varchar(1000) DEFAULT NULL COMMENT '任务详细描述',
  `taskTitle` varchar(40) DEFAULT NULL COMMENT '任务简单描述',
  `taskAttachment` varchar(1000) DEFAULT NULL COMMENT '任务附件，可以上传多个附件，他们被拼接为一个字符串',
  `startTime` date DEFAULT NULL COMMENT '开始时间',
  `endTime` date DEFAULT NULL COMMENT '结束时间',
  `meetingId` int(11) DEFAULT NULL COMMENT '会议的id  meetingId',
  `taskApprover` varchar(20) DEFAULT NULL COMMENT '任务需要审批者 userId 使用逗号分割',
  `needApproval` varchar(10) DEFAULT NULL COMMENT '任务需要审批否？  TRUE | FALSE',
  `creationTimestamp` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通用任务的模型';


#机场火车站的配置
DROP TABLE IF EXISTS `MEETING_Station`;
CREATE TABLE `MEETING_Station` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(40) DEFAULT NULL COMMENT '目前支持一下几种：  机场 | 火车站 ',
  `province` varchar(10) DEFAULT NULL COMMENT '',
  `city` varchar(20) DEFAULT NULL COMMENT 'city',
  `town` varchar(20) DEFAULT NULL COMMENT 'town',
  `address` varchar(50) DEFAULT NULL COMMENT 'address',
  `personInCharge` varchar(20) DEFAULT NULL COMMENT '负责人ID',
  `taskAttachment` varchar(500) DEFAULT NULL COMMENT '相关附件',
  `creationTimestamp` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机场，火车站的配置';


#会议室
DROP TABLE IF EXISTS `MEETING_Room`;
CREATE TABLE `MEETING_Room` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `province` varchar(10) DEFAULT NULL COMMENT '',
  `city` varchar(20) DEFAULT NULL COMMENT 'city',
  `town` varchar(20) DEFAULT NULL COMMENT 'town',
  `address` varchar(50) DEFAULT NULL COMMENT 'address',
  `validFlag` varchar(5) DEFAULT NULL COMMENT '是否有效 Y | N',
  `startValid` date DEFAULT NULL COMMENT '有效开始期',
  `endValid` date DEFAULT NULL COMMENT '有效截止期',
  `personInCharge` varchar(20) DEFAULT NULL COMMENT '负责人ID',
  `taskAttachment` varchar(500) DEFAULT NULL COMMENT '相关附件',
  `capacity` int(11)  DEFAULT NULL COMMENT '支持多少人',
  `price` float(11)  DEFAULT NULL COMMENT '支持多少人',
  `creationTimestamp` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机场火车站的配置';


#酒店
DROP TABLE IF EXISTS `MEETING_Room`;
CREATE TABLE `MEETING_Room` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `province` varchar(10) DEFAULT NULL COMMENT '',
  `city` varchar(20) DEFAULT NULL COMMENT 'city',
  `town` varchar(20) DEFAULT NULL COMMENT 'town',
  `address` varchar(50) DEFAULT NULL COMMENT 'address',
  `validFlag` varchar(5) DEFAULT NULL COMMENT '是否有效 Y | N',
  `startValid` date DEFAULT NULL COMMENT '有效开始期',
  `endValid` date DEFAULT NULL COMMENT '有效截止期',
  `personInCharge` varchar(20) DEFAULT NULL COMMENT '负责人ID',
  `taskAttachment` varchar(500) DEFAULT NULL COMMENT '相关附件',
  `creationTimestamp` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='酒店配置';


#人员安置任务
DROP TABLE IF EXISTS `MEETING_Participant_Recipiant_Task`;
CREATE TABLE `MEETING_Participant_Recipiant_Task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `participantId` int(11) DEFAULT NULL COMMENT '参会人员(MEETING_Participant)的id',
  `batchId` int(11)  DEFAULT NULL COMMENT '批次，来源于：MEETING_Participant_Recipiant_Batch_Task',
  `completeStatus` varchar(10) DEFAULT NULL COMMENT '完成状态： 开始 | 进行中 | 结束',
  `creationTimestamp` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人员安置任务';


#任务批处理  对一批人的安置
DROP TABLE IF EXISTS `MEETING_Participant_Recipiant_Batch_Task`;
CREATE TABLE `MEETING_Participant_Recipiant_Batch_Task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) DEFAULT NULL COMMENT '任务类型，仅仅支持以下类型： 接机 | 安排酒店 | 安排会议室',
  `stationId` int(11)  DEFAULT NULL COMMENT '火车站，机场id',
  `hotelId` int(11)  DEFAULT NULL COMMENT '酒店id',
  `meetingRoomId` int(11)  DEFAULT NULL COMMENT '会议室id',
  `startTime` date DEFAULT NULL COMMENT '有效截止期',
  `endTime` date DEFAULT NULL COMMENT '有效截止期',
  `personInCharge` varchar(20) DEFAULT NULL COMMENT '负责人ID',
  `attachment` varchar(500) DEFAULT NULL COMMENT '相关附件',
  `creationTimestamp` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对一批人的安置';

