/* =========================================================
   🗄️  社区投票系统  ——  MySQL 8.x  /  utf8mb4
   ---------------------------------------------------------
   ① users               业主 / 居民
   ② admin               后台管理员
   ③ vote_activities     投票活动（时间窗 & 范围）
   ④ vote_questions      议题（隶属于活动）
   ⑤ vote_option_set_items  选项模板项（A/B/C…）
   ⑥ user_votes          用户实际投票记录（加权面积）
   ---------------------------------------------------------
   主外键关系（→ 表.字段）
     user_votes.user_id            → users.id
     user_votes.vote_activity_id   → vote_activities.id
     user_votes.question_id        → vote_questions.id
     user_votes.option_id          → vote_option_set_items.id
     vote_questions.vote_activity_id → vote_activities.id
   ========================================================= */

/* ---------- 1. users：业主 / 居民 ---------- */
CREATE TABLE users (
  id              INT          PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
  phone           VARCHAR(20)  NOT NULL UNIQUE COMMENT '手机号',
  password        VARCHAR(100) NOT NULL COMMENT '登录密码（加密）',
  name            VARCHAR(50)  NOT NULL COMMENT '姓名',
  gender          ENUM('男','女','未知') DEFAULT '未知' COMMENT '性别',
  id_card         VARCHAR(30)  COMMENT '身份证号',
  community_name  VARCHAR(100) NOT NULL COMMENT '所属小区',
  building_number VARCHAR(20)  COMMENT '栋',
  unit_number     VARCHAR(20)  COMMENT '单元',
  room_number     VARCHAR(20)  COMMENT '房号',
  area_size       DECIMAL(10,2) COMMENT '房屋面积 m²',
  is_verified     TINYINT(1)   DEFAULT 0 COMMENT '实名认证通过 1/0',
  created_at      DATETIME     NOT NULL COMMENT '注册时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业主表';

/* ---------- 2. admin：后台账号 ---------- */
CREATE TABLE admin (
  id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  phone        VARCHAR(20)  NOT NULL UNIQUE COMMENT '手机号',
  password     VARCHAR(100) NOT NULL COMMENT '密码（加密）',
  idCard       VARCHAR(18)  COMMENT '身份证号',
  name         VARCHAR(50)  COMMENT '姓名',
  avatar       VARCHAR(255) COMMENT '头像URL',
  gender       VARCHAR(50)  DEFAULT '0' COMMENT '性别',
  birthday     DATE         COMMENT '生日',
  education    VARCHAR(20)  COMMENT '学历',
  politicalStatus VARCHAR(20) COMMENT '政治面貌',
  address      VARCHAR(255) COMMENT '地址',
  validFrom    DATE         COMMENT '证件有效期起',
  validTo      DATE         COMMENT '证件有效期止',
  organization VARCHAR(100) COMMENT '组织',
  position     VARCHAR(50)  COMMENT '岗位',
  create_time  DATETIME     COMMENT '创建时间',
  update_time  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='后台管理员表';

/* ---------- 3. vote_activities：投票活动 ---------- */
CREATE TABLE vote_activities (
  id             INT PRIMARY KEY AUTO_INCREMENT COMMENT '活动ID',
  title          VARCHAR(200) NOT NULL COMMENT '活动标题',
  start_time     DATETIME     NOT NULL COMMENT '开始时间',
  end_time       DATETIME     NOT NULL COMMENT '结束时间',
  is_official    TINYINT(1)   DEFAULT 0 COMMENT '官方投票 1/0',
  vote_scope VARCHAR(20) COMMENT '活动范围，可以是ALL或者PARTIAL',
  community_name VARCHAR(100) NOT NULL COMMENT '适用小区',
  attachment_url VARCHAR(255) COMMENT '附件URL',
  created_at     DATETIME     NOT NULL COMMENT '创建时间',
  CHECK (start_time < end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投票活动表';

/* ---------- 4. vote_questions：议题 ---------- */
CREATE TABLE vote_questions (
  id              INT PRIMARY KEY AUTO_INCREMENT COMMENT '议W题ID',
  vote_activity_id INT NOT NULL COMMENT '所属活动ID',
  title           VARCHAR(200) NOT NULL COMMENT '议题内容',
  sort_order      INT DEFAULT 0 COMMENT '排序',
  option_set_id   INT NOT NULL COMMENT '选项模板ID',
  created_at      DATETIME NOT NULL COMMENT '创建时间',
  INDEX idx_act (vote_activity_id),
  CONSTRAINT fk_questions_activity
    FOREIGN KEY (vote_activity_id) REFERENCES vote_activities(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='议题表';

/* ---------- 5. vote_option_set_items：选项模板项 ---------- */
CREATE TABLE vote_option_set_items (
  id          INT PRIMARY KEY AUTO_INCREMENT COMMENT '模板项ID',
  set_id      INT NOT NULL COMMENT '模板编号',
  option_text VARCHAR(100) NOT NULL COMMENT '选项文本',
  option_code VARCHAR(5)   COMMENT '选项代号 A/B/C',
  sort_order  INT DEFAULT 0 COMMENT '排序'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='选项模板项表';

/* ---------- 6. user_votes：投票记录 ---------- */
CREATE TABLE user_votes (
  id              INT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
  user_id         INT NOT NULL COMMENT '用户ID',
  vote_activity_id INT NOT NULL COMMENT '活动ID',
  question_id     INT NOT NULL COMMENT '议题ID',
  option_id       INT NOT NULL COMMENT '选项ID',
  vote_method     VARCHAR(20) NOT NULL COMMENT '方式 online/offline/sms',
  vote_time       DATETIME    NOT NULL COMMENT '投票时间',
  area_size       DECIMAL(10,2) COMMENT '投票时房屋面积',
  INDEX idx_user_act (user_id, vote_activity_id),
  INDEX idx_q_opt  (question_id, option_id),
  CONSTRAINT fk_votes_user      FOREIGN KEY (user_id)         REFERENCES users(id),
  CONSTRAINT fk_votes_activity  FOREIGN KEY (vote_activity_id) REFERENCES vote_activities(id),
  CONSTRAINT fk_votes_question  FOREIGN KEY (question_id)     REFERENCES vote_questions(id),
  CONSTRAINT fk_votes_option    FOREIGN KEY (option_id)       REFERENCES vote_option_set_items(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户投票记录表';
/* ---------- 7. vote_activity_scopes：活动投票范围表 ---------- */
CREATE TABLE `vote_activity_scopes`  (
                                         `id` int(11) NOT NULL AUTO_INCREMENT,
                                         `activity_id` int(11) NOT NULL,
                                         `building_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                         `unit_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact COMMENT='活动投票范围表';
-- ---------- users ----------
INSERT INTO users
(id, phone, password, name, gender, id_card, community_name, building_number,
 unit_number, room_number, area_size, is_verified, created_at)
VALUES
(1, '13800001111', '$2b$10$hash1', '王小明', '男', '110101199001015432',
 '阳光花园', '3', '1', '504', 88.50, 1, '2025-08-25 10:00:00'),
(2, '13900002222', '$2b$10$hash2', '李晓红', '女', '110101199305064321',
 '阳光花园', '3', '2', '602', 72.00, 1, '2025-08-25 10:05:00');

-- ---------- admin ----------
INSERT INTO admin
(id, phone, password, name, organization, position, create_time)
VALUES
(1, '13100003333', '$2b$10$adminhash', '物业张经理', '阳光物业', '社区管理员',
 '2025-08-26 09:00:00');

-- ---------- vote_activities ----------
INSERT INTO vote_activities
(id, title, start_time, end_time, is_official,vote_scope, community_name,
attachment_url, created_at)
VALUES
(1, '2025 年物业费调整投票', '2025-09-01 08:00:00', '2025-09-05 20:00:00',
 1, 'ALL','阳光花园', NULL, '2025-08-26 12:00:00');

-- ---------- vote_option_set_items ----------
-- set_id = 1 代表「赞同/反对/弃权」模板
INSERT INTO vote_option_set_items
(id, set_id, option_text, option_code, sort_order)
VALUES
(1, 1, '赞同', 'A', 1),
(2, 1, '反对', 'B', 2),
(3, 1, '弃权', 'C', 3);

-- ---------- vote_questions ----------
INSERT INTO vote_questions
(id, vote_activity_id, title, sort_order, option_set_id, created_at)
VALUES
(1, 1, '本小区物业费由 1.8 元/㎡ 调整至 2.2 元/㎡，是否同意？', 1, 1,
 '2025-08-26 12:05:00'),
(2, 1, '2025 年度绿化专项基金 200 万元预算，是否同意？', 2, 1,
 '2025-08-26 12:06:00');

-- ---------- user_votes ----------
INSERT INTO user_votes
(id, user_id, vote_activity_id, question_id, option_id,
 vote_method, vote_time, area_size)
VALUES
-- 王小明 对两道议题均投「赞同」
(1, 1, 1, 1, 1, 'online', '2025-09-01 09:00:00', 88.50),
(2, 1, 1, 2, 1, 'online', '2025-09-01 09:01:00', 88.50),
-- 李晓红 第一题反对，第二题弃权
(3, 2, 1, 1, 2, 'online', '2025-09-01 09:05:00', 72.00),
(4, 2, 1, 2, 3, 'online', '2025-09-01 09:06:00', 72.00);


---------- vote_activity_scopes----------
INSERT INTO vote_activity_scopes (activity_id, building_number, unit_number) VALUES
(101, '1', '1'),   -- 1栋1单元
(101, '1', '2'),   -- 1栋2单元
(101, '2', NULL);  -- 2栋整栋（unit_number = NULL 表示整栋）
