/*
 Navicat Premium Dump SQL

 Source Server         : 本地docker-mysql8
 Source Server Type    : MySQL
 Source Server Version : 80403 (8.4.3)
 Source Host           : localhost:3306
 Source Schema         : forum

 Target Server Type    : MySQL
 Target Server Version : 80403 (8.4.3)
 File Encoding         : 65001

 Date: 11/03/2025 16:21:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for db_account
-- ----------------------------
DROP TABLE IF EXISTS `db_account`;
CREATE TABLE `db_account`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像url',
  `register_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_email`(`email` ASC) USING BTREE,
  UNIQUE INDEX `unique_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_account
-- ----------------------------
INSERT INTO `db_account` VALUES (1, 'kevin', 'lzk729765221@gmail.com', '$2a$10$YjGz5vusDFbujLiEQ6d6HO.LoX4k/J2cu8dzH/RHn5Qnz8CK538JC', 'user', '/avatar/ae95610a5ae14bffba5f05aaeb74a0cd', '2025-01-07 07:11:38');
INSERT INTO `db_account` VALUES (5, 'test', '729765231@qq.com', '$2a$10$mGUkKWzUZMZJrLea/keAyuCdxU4XvNIoJ/ppxkbPWJuNZwGaTxBje', 'user', '/avatar/cc981ef73e1144b8b763f31861112634', '2025-03-03 08:35:25');
INSERT INTO `db_account` VALUES (6, 'tomcat', '729765221@qq.com', '$2a$10$xqzbNMK4rodocRDMBS4mPOeYWuqnbd9ZOY9OuQSi.3mSqvyCw59Ye', 'user', NULL, '2025-03-10 06:04:15');

-- ----------------------------
-- Table structure for db_account_details
-- ----------------------------
DROP TABLE IF EXISTS `db_account_details`;
CREATE TABLE `db_account_details`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '账户id',
  `gender` tinyint NULL DEFAULT NULL COMMENT '性别',
  `phone` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机',
  `qq` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'qq',
  `wx` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '微信',
  `desc` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '详细描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户详细信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_account_details
-- ----------------------------
INSERT INTO `db_account_details` VALUES (1, 0, '13819870687', '729765221', 'aabb12345', 'born to kill！');
INSERT INTO `db_account_details` VALUES (5, 1, '15556024855', '1275629835', 'wx1275629835', 'hello');
INSERT INTO `db_account_details` VALUES (6, 0, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for db_account_privacy
-- ----------------------------
DROP TABLE IF EXISTS `db_account_privacy`;
CREATE TABLE `db_account_privacy`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `phone` tinyint NULL DEFAULT NULL COMMENT '手机号码',
  `email` tinyint NULL DEFAULT NULL COMMENT '电子邮箱',
  `wx` tinyint NULL DEFAULT NULL COMMENT '微信',
  `qq` tinyint NULL DEFAULT NULL COMMENT 'qq',
  `gender` tinyint NULL DEFAULT NULL COMMENT '性别',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户隐私表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_account_privacy
-- ----------------------------
INSERT INTO `db_account_privacy` VALUES (1, 1, 1, 1, 0, 0);
INSERT INTO `db_account_privacy` VALUES (5, 0, 0, 1, 1, 1);
INSERT INTO `db_account_privacy` VALUES (6, 1, 1, 1, 1, 1);

-- ----------------------------
-- Table structure for db_image_store
-- ----------------------------
DROP TABLE IF EXISTS `db_image_store`;
CREATE TABLE `db_image_store`  (
  `uid` int NULL DEFAULT NULL COMMENT '用户id',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '图片名称',
  `time` datetime NULL DEFAULT NULL COMMENT '存储时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户图片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_image_store
-- ----------------------------
INSERT INTO `db_image_store` VALUES (1, '/cache/20250213/df9da2a917124a59bc1c0da00e393bfd', '2025-02-13 06:55:45');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250213/6b83af7a40f0411bb830883a553ffc78', '2025-02-13 07:00:40');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250213/01c4db41db3449f98e26810a1caec2a3', '2025-02-13 07:10:24');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250213/3de8c244aab1492487255f0d29492323', '2025-02-13 07:14:12');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250213/976924b274544938addf1297dae8734c', '2025-02-13 07:17:03');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250213/45c5a22a1f0449ed8d1cf2dbfbbbb158', '2025-02-13 07:24:17');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250213/48de8a774f9d472a8e426cfdbcc0218e', '2025-02-13 07:25:49');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250213/335c8d7acae74617a976f5d9ec0c5644', '2025-02-13 07:25:52');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250213/0c45b5e88bc54924bfeb2bb9f890f151', '2025-02-13 07:25:56');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250213/caeb18e543fe4650bf268f203e59b73d', '2025-02-13 07:26:00');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250213/bbebc1464f1c403692a292a4ae1ba818', '2025-02-13 07:26:04');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250213/2c4d9b8aa10a465fba0e39ba9ce85166', '2025-02-13 07:26:09');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250213/a3a1f880fe084077b3dc5740c912841e', '2025-02-13 07:26:56');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250213/468bb10e20f04c46afb7483e8b96f4cf', '2025-02-13 12:46:42');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250215/d343a6dfb64d43bfb88a8a75706cfac8', '2025-02-15 02:29:45');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250215/6d808095df5942faa6bdf9be5082471a', '2025-02-15 02:30:08');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250215/0014c683fa864741affd2f3718a36cec', '2025-02-15 06:24:51');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250217/4b76d592a4ff4ba688c9a82c62b793f8', '2025-02-17 03:18:42');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250217/7143a54e6b824caaaeabb73f062fdbd9', '2025-02-17 05:45:07');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250219/408cdd1f997e4ed685019796ff3c500b', '2025-02-19 06:26:45');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250219/7a39c7e72c9041dab0ee6c7b06d4cc94', '2025-02-19 06:27:21');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250219/7b968f8a9408470cb0f60e7c93662ece', '2025-02-19 06:33:03');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250219/0fada7d0c2314828b4448319853d73db', '2025-02-19 06:33:18');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250224/8498ca8b314d4b5e9974407caecb4a58', '2025-02-24 06:29:38');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250224/e7aea59d450f45d6b0034734449b97f0', '2025-02-24 06:30:24');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250225/2609e88b010647198b4ae46dfb15fe19', '2025-02-25 01:39:21');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250225/e825d490e2e34c0fb3ca954ab88337f1', '2025-02-25 02:50:54');
INSERT INTO `db_image_store` VALUES (5, '/cache/20250304/6d93a6523c8a4edfb32d9c5f974dc075', '2025-03-04 05:15:03');
INSERT INTO `db_image_store` VALUES (1, '/cache/20250304/0e80b3b4c66344b486df83168504ea83', '2025-03-04 07:48:09');

-- ----------------------------
-- Table structure for db_notification
-- ----------------------------
DROP TABLE IF EXISTS `db_notification`;
CREATE TABLE `db_notification`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '提醒id',
  `uid` int NULL DEFAULT NULL COMMENT '用户id\r\n',
  `title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '内容',
  `type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '类型',
  `url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '地址',
  `time` datetime NULL DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '信息通知表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_notification
-- ----------------------------

-- ----------------------------
-- Table structure for db_topic
-- ----------------------------
DROP TABLE IF EXISTS `db_topic`;
CREATE TABLE `db_topic`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '帖子id',
  `title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '帖子标题',
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '帖子内容',
  `uid` int NULL DEFAULT NULL COMMENT '发帖用户id',
  `type` int NULL DEFAULT NULL COMMENT '帖子类型',
  `time` datetime NULL DEFAULT NULL COMMENT '发帖时间',
  `top` tinyint NULL DEFAULT NULL COMMENT '是否置顶0不置顶1置顶',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '帖子信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_topic
-- ----------------------------
INSERT INTO `db_topic` VALUES (1, '八股链接', '{\"ops\":[{\"insert\":\"太难起\\n\"}]}', 1, 1, '2025-02-15 07:16:42', 0);
INSERT INTO `db_topic` VALUES (2, '打牌的狐朋狗友', '{\"ops\":[{\"insert\":\"一起打牌的朋友\"},{\"insert\":{\"image\":\"http://localhost:8080/images/cache/20250217/4b76d592a4ff4ba688c9a82c62b793f8\"}},{\"insert\":\"\\n\"}]}', 1, 2, '2025-02-17 03:19:09', 0);
INSERT INTO `db_topic` VALUES (3, '下头男', '{\"ops\":[{\"insert\":\"太唐了,唐人街探案\"},{\"insert\":{\"image\":\"http://localhost:8080/images/cache/20250217/7143a54e6b824caaaeabb73f062fdbd9\"}},{\"insert\":\"\\n\"}]}', 1, 2, '2025-02-17 05:45:08', 0);
INSERT INTO `db_topic` VALUES (4, '哪吒哪吒', '{\"ops\":[{\"insert\":\"《哪吒2》的成功，离不开四川动画产业的全面发力：电影由导演饺子所在的成都可可豆动画影视有限公司全程操刀，天府长岛数字文创园内多家本土企业参与特效、美术等环节，形成高效协作的“动画奥运村”；三星堆元素、四川方言、火锅场景等本土文化符号的融入，让影片兼具国际视野与地域特色；此外，成都以全国第三的票房贡献，成为影片市场表现的重要引擎。\r\n\r\n四川动画产业的崛起，背后是政策扶持与高校的产教融合创新。近年来，四川省通过建设国家级网络视听产业基地、提供税收优惠、开通影视审核“快速通道”等举措，吸引620余家影视企业落户。2024年，四川电影电视学院（以下简称川影）与成都高新区、大邑县签约共建“数字影视文旅产业发展先行示范区”，推动影视全产业链孵化，助力“成都出品”品牌走向全球。 如今，成都影视行业从业者超10万人，在读影视专业学生达22万人。作为西部影视教育的标杆，川影为《哪吒2》输送了关键人才： 影片中“儿童哪吒”“太乙真人”“李靖”等角色的配音由川影校友担纲。川影播音主持学院副教授吕艳婷带领的“配音提高班”，通过“磨耳朵”“演角色”的实践教学，培养出兼具情感张力与专业技巧的配音人才，为角色注入灵魂。此外，川影设计与美术学院毕业生张书绫参与影片美术组，负责道具设计、特效细化等工作。她表示，川影的实践课程让自己“从模糊概念到清晰掌握动画工业全流程”，体现了高校教育与产业需求的无缝对接。\r\n\r\n“目前我们正在积极筹备即将在四月发布的‘中国电影投融资发展报告（2024）’，不断通过和产业机构，行业学会协会的合作，来为这座城市正试图构建的‘内容-技术-资本’全链条生态贡献价值。”四川电影电视学院校长罗思表示，希望未来能有更多的内容与产业扶持政策，挖掘更多生动可塑的IP，搭建与行业接轨的投资平台。\r\n\r\n《哪吒2》登顶全球动画之巅，标志着中国动画从“追赶者”到“领跑者”的质变。中国电影评论学会会长饶曙光指出，中国动画电影的水平提升和工业化完善，都会起到积极的推动作用。“我们的动画电影创作者，一方面要苦练内功，另一方面也要积极追踪技术发展，掌握新的科技为自己创作赋能。”\r\n\r\n从“区域标杆”到“世界级IP工厂”，四川的野心不止于此。放眼未来，四川动画产业正以政策支持与川影等高校为支点，构建“文化+科技+人才”的生态体系。随着天府国际动漫城等项目的推进，四川或将引领中国动画从“单点突破”迈向“全球领跑”。\\n\"}]}', 1, 1, '2025-02-17 06:37:56', 0);
INSERT INTO `db_topic` VALUES (5, '饺子导演', '{\"ops\":[{\"insert\":\"据此前报道，饺子导演在采访的时候曾表示：“我本身就是非常喜欢周星驰的电影，所以现在我创作的哪吒系列电影当中，很多趣味性的东西，是受到了他的启发。”\\n例如，太乙真人那贱兮兮地说“你打我撒”的样子，让人联想到《九品芝麻官》中方唐镜的搞笑场景；太乙真人说的“不知道我顶不顶得住”，跟《少林足球》里三师兄的话完全对应。还有很多个搞笑场面，都致敬了周星驰的电影经典画面。\\n对此，不少网友纷纷戏称：“这波属于饺子和周星驰双向奔赴了。”\\n\"}]}', 1, 1, '2025-02-19 02:44:06', 0);
INSERT INTO `db_topic` VALUES (6, '不少韩国人控制开销“抛弃”咖啡，喝上“无豆咖啡”', '{\"ops\":[{\"insert\":\"据韩媒近日报道，经济低迷之下，韩国不少人开始控制日常开销，相当比例的人选择从不买或少买咖啡开始，而韩国餐饮业也适时推出了成本较低的“无豆咖啡”饮品。\\n韩国《中央日报》17日援引当天公布的一份统计数据报道，韩国2024年第四季度咖啡厅营业额比上一季度下降9.5%，降幅在所有类别中最大。相比之下，酒吧降幅为1.7%，快餐店是1.8%。\\n咖啡一度在韩国非常流行，咖啡馆可谓遍地开花。2023年5月一项调查显示，约三分之一的韩国成年人去咖啡馆的次数比此前两三年多。\\n但《中央日报》说，最新数据表明，经济不景气开始影响韩国人对咖啡的热爱，促使消费者减少对酒类和咖啡等非生活必需品的消费。\\n另据报道，为缓解主要原料成本上涨的压力，最近，韩国餐饮业推出了用其他原料替代咖啡豆的“无豆咖啡”饮品。在首尔市中心一家咖啡馆，咖啡师只需把杯子放在萃取机上就能接到“原液”，然后根据顾客口味需求，往“原液”里加水或牛奶进行调制。\\n据负责人介绍，大麦、木槿、橘子皮等12种原材料经过发酵-烘干-研磨-烘焙等工序，就能制成“无豆咖啡”。这些原材料的价格相对稳定，而且市场供应充足。\\n\"}]}', 1, 2, '2025-02-19 03:08:25', 0);
INSERT INTO `db_topic` VALUES (7, '京东外卖骑手迎福利！2025年起全面享五险一金，行业标杆再升', '{\"ops\":[{\"insert\":\"京东近日传来重大消息，宣布了一项针对外卖骑手的全新福利政策。据悉，自2025年3月1日起，京东将率先在行业内为全职外卖骑手缴纳五险一金，同时，兼职骑手也将享受到意外险和健康医疗险的保障。这一举措无疑为外卖骑手群体提供了更为全面的保护，标志着京东在推动外卖骑手权益保障方面迈出了重要一步。\\n作为科技领域的佼佼者，京东一直以来都在积极履行社会责任，推动行业的高质量发展。早在之前，京东就为快递小哥缴纳了五险一金，而此次将这一福利扩展到外卖骑手群体，再次彰显了京东对于员工关怀的深厚底蕴。此举不仅为骑手们提供了更为坚实的保障，也为整个外卖行业树立了新的典范。\\n京东方面表示，此次为外卖骑手提供五险一金保障，是公司对于骑手群体辛勤付出的认可和回报。京东深知，外卖骑手作为连接消费者与商家的桥梁，他们的健康与福祉对于整个外卖行业的健康发展至关重要。因此，京东决定率先行动起来，为骑手们提供更好的保障。\\n\"}]}', 1, 1, '2025-02-19 05:27:40', 0);
INSERT INTO `db_topic` VALUES (8, '华为Mate XT 非凡大师海外亮相', '{\"ops\":[{\"insert\":\"2月18日，华为创新产品全球发布会在马来西亚吉隆坡盛大举行，全球首款商用三折叠屏手机——华为Mate XT 非凡大师首次亮相海外。这款集突破性设计与领先技术于一身的旗舰产品，以其创新的三折叠形态重新定义了智能手机的形态边界，同时凭借卓越的科技实力与极致用户体验，向全球展示了中国科技企业的创新实力与无限潜能，彰显了中国智造在全球科技舞台上的领先地位。\\n\"},{\"insert\":{\"image\":\"http://localhost:8080/images/cache/20250219/408cdd1f997e4ed685019796ff3c500b\"}},{\"insert\":\"\\n发布会结束后，华为还将于2月19日至25日在吉隆坡举办为期一周的折叠创新展，旨在全方位展示其在柔性显示、精密铰链、材料工艺等核心技术上的突破性成果。展览不仅将呈现华为折叠屏产品从概念到量产的创新历程，更将通过沉浸式互动体验，向全球消费者传递品牌对科技美学与用户体验的极致追求，彰显其在高端折叠屏市场的技术实力与创新领导力。\\n华为Mate XT 非凡大师的卓越体验，离不开华为天工铰链系统的强力支撑。它首次实现了双轨联动与内外弯折的创新功能，为折叠屏手机带来了全新设计理念。这一独特结构显著缩小了铰链活动空间，减少了弧臂转动的角度，从而大幅减薄了整机厚度，使设备在视觉和手感上都更加纤薄。通过精密的多驱调校技术，华为Mate XT 非凡大师在展开与折叠时，手感更加顺滑、稳定，每一次操作都如同行云流水般自然\\n\"},{\"insert\":{\"image\":\"http://localhost:8080/images/cache/20250219/7a39c7e72c9041dab0ee6c7b06d4cc94\"}},{\"insert\":\"\\n\"}]}', 1, 1, '2025-02-19 06:27:25', 0);
INSERT INTO `db_topic` VALUES (9, 'vultr的vps虚拟主机配置', '{\"ops\":[{\"insert\":\"VMESS配置\\n\"},{\"insert\":{\"image\":\"http://localhost:8080/images/cache/20250219/7b968f8a9408470cb0f60e7c93662ece\"}},{\"insert\":\"\\n服务器端配置\\n\"},{\"insert\":{\"image\":\"http://localhost:8080/images/cache/20250219/0fada7d0c2314828b4448319853d73db\"}},{\"insert\":\"\\n\"}]}', 1, 5, '2025-02-19 06:33:20', 0);
INSERT INTO `db_topic` VALUES (10, '安装docker时出现问题', '{\"ops\":[{\"insert\":\"确认 Dockerfile 文件是否正确，可使用 docker build 命令进行构建并检查报错信息。\\n检查网络连接是否正常，尝试更换国内镜像源。\\n确认虚拟化功能是否启用，如果未启用，需要在BIOS中设置启用。\\n检查并安装依赖关系，确保您的系统上已安装所有必要的依赖关系。\\n清理 apt 缓存并重新安装 Docker。\\n\"},{\"insert\":{\"image\":\"http://localhost:8080/images/cache/20250224/8498ca8b314d4b5e9974407caecb4a58\"}},{\"insert\":\"\\n\"}]}', 1, 3, '2025-02-24 06:29:39', 0);
INSERT INTO `db_topic` VALUES (11, '被曝与关晓彤分手后，鹿晗首露面疑喝醉痛哭，邓超陪他喝酒安慰', '{\"ops\":[{\"insert\":\"近日，鹿晗与关晓彤的分手传闻再度引发热议，相关话题连续多日登上热搜榜单，成为网友关注的焦点。2月24日，又有爆料称鹿晗深夜与好友邓超等人聚餐买醉，画面显示鹿晗情绪激动，甚至在醉酒后失控指向同桌友人，最终被邓超安抚。聚餐结束后，鹿晗走路踉踉跄跄，神情颓废，似乎符合失恋后的低落状态。\\n\"},{\"insert\":{\"image\":\"http://localhost:8080/images/cache/20250224/e7aea59d450f45d6b0034734449b97f0\"}},{\"insert\":\"\\n\"}]}', 1, 4, '2025-02-24 06:30:26', 1);
INSERT INTO `db_topic` VALUES (12, '马航MH370新一轮搜索工作重启，“更加集中和有针对性”', '{\"ops\":[{\"insert\":\"据央视新闻消息，总台记者 获悉，当地时间2月25日，对马航MH370的新一轮搜索将在 澳大利亚西海岸约1500公里处重启 。\\n\"},{\"insert\":{\"image\":\"http://localhost:8080/images/cache/20250225/2609e88b010647198b4ae46dfb15fe19\"}},{\"insert\":\"\\n\"}]}', 1, 1, '2025-02-25 01:39:22', 1);
INSERT INTO `db_topic` VALUES (13, '单休比双休多上七年班？！时间都去哪儿了？', '{\"ops\":[{\"insert\":\"朋友们，今天算了一笔账，直接给我整emo了\\n单休和双休的差距，居然有七年那么多！没错，就是七年！具体的计算如下：\\n单休：每周多上1天班，一年52周，单休人比双休人多上52天班，工作30年，单休人多上1560天，约等于 4.3年，如果算上加班和调休，直接奔着7年去了！\\n七年能干啥？环游世界，打卡100个国家；读完1000本书，成为行走的百科全书；学会10项技能，从烘焙到编程全能选手；陪伴家人，见证孩子从小学到高中\\n单休人的痛，谁懂啊？生活只剩下“上班-睡觉-上班”的死循环，时间就是生命，别让单休偷走你的七年！ 单休只是目前短暂的，相信后面就有双休跟无限休等着你~P2-P5有一些单休回血的小方法分享给单休的宝子！希望对大家有帮助！\\n\"},{\"insert\":{\"image\":\"http://localhost:8080/images/cache/20250225/e825d490e2e34c0fb3ca954ab88337f1\"}},{\"insert\":\"\\n\"}]}', 1, 1, '2025-02-25 03:00:23', NULL);
INSERT INTO `db_topic` VALUES (14, '肝血虚眼花，肝气郁腹胀，肝火旺口苦！教你3个便宜中成药，疏肝', '{\"ops\":[{\"insert\":\"肝火旺盛常因情志过激、长期压抑或过度劳累等因素引起。这时就像烧开的水一样，蒸汽（肝火）不断上升。\\n\"},{\"insert\":{\"image\":\"http://localhost:8080/images/cache/20250304/6d93a6523c8a4edfb32d9c5f974dc075\"}},{\"insert\":\"\\n肝火上炎时，人的情绪会变得异常暴躁，一点小事就可能引发激烈的情绪爆发，很难控制自己的脾气。\\n这种情绪上的失控，又会进一步加重肝气的郁结，导致肝火愈发旺盛\\n\"}]}', 5, 1, '2025-03-04 05:15:05', NULL);
INSERT INTO `db_topic` VALUES (15, '小米SU7 Ultra装上绿牌颜值砍半！雷军两会建议优化新能', '{\"ops\":[{\"insert\":\"快科技3月4日消息，今天雷军公布了自己的2025两会建议，其中一项就是建议优化新能源车牌设计。\\n雷军表示，随着年轻消费者数量增多，用户对汽车外观与个性化功能拥有更加多元化的需求。\\n例如，新能源汽车的绿色号牌在某种程度上制约了汽车产品的设计效果，现有汽车号牌不具备智能化能力，难以满足日常出行、交通管理的智能化发展需求。\\n\"},{\"insert\":{\"image\":\"http://localhost:8080/images/cache/20250304/0e80b3b4c66344b486df83168504ea83\"}},{\"insert\":\"\\n\"}]}', 1, 1, '2025-03-04 07:42:47', NULL);
INSERT INTO `db_topic` VALUES (16, '今年5%的GDP增速目标高不高？《政府工作报告》起草组解答', '{\"ops\":[{\"attributes\":{\"size\":\"large\"},\"insert\":\"2025年《政府工作报告》提出，2025年经济增长预期目标为5%左右。这一目标高于一些机构的预测。\"},{\"insert\":\"\\n“今年5%左右的经济增长预期目标，是经过反复研究论证设定的。”《政府工作报告》起草组负责人、国务院研究室主任沈丹阳今日在国新办举行的吹风会上解释。\\n\"}]}', 1, 1, '2025-03-05 07:07:06', NULL);
INSERT INTO `db_topic` VALUES (17, '小米SU7 Ultra首撞', '{\"ops\":[{\"insert\":\"3月5日，小米SU7 Ultra在交付后发生首例事故，一辆闪电黄配色的车辆左前翼子板及大灯受损，事故原因暂未公布。该事件迅速引发社交平台热议，成为小米汽车交付后的首个舆论焦点。\\n\"}]}', 1, 1, '2025-03-06 03:28:54', NULL);
INSERT INTO `db_topic` VALUES (18, '新人报道请多指教', '{\"ops\":[{\"insert\":\"大家好，我是新人tomcat\\n\"}]}', 6, 1, '2025-03-10 06:05:14', NULL);
INSERT INTO `db_topic` VALUES (19, '近日，山东泰安26岁小伙在泰山陪爬一年赚30多万元引发热议。', '{\"ops\":[{\"insert\":\"近日，山东泰安26岁小伙在泰山陪爬一年赚30多万元引发热议。当事人陈先生称自己一毕业就开始干这个了，生意好的话一个月能挣四五万。陪爬泰山白天收费600元，晚上700元，顾客主要是女性，年龄20岁到35岁。\\n\"}]}', 5, 1, '2025-03-11 02:47:45', NULL);

-- ----------------------------
-- Table structure for db_topic_comment
-- ----------------------------
DROP TABLE IF EXISTS `db_topic_comment`;
CREATE TABLE `db_topic_comment`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `uid` int NULL DEFAULT NULL COMMENT '用户id',
  `tid` int NULL DEFAULT NULL COMMENT '帖子id',
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '帖子内容',
  `time` datetime NULL DEFAULT NULL COMMENT '发表时间',
  `quote` int NULL DEFAULT NULL COMMENT '引用评论id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_topic_comment
-- ----------------------------
INSERT INTO `db_topic_comment` VALUES (2, 1, 15, '{\"ops\":[{\"insert\":\"are you ok?\\n\"}]}', '2025-03-05 07:09:25', -1);
INSERT INTO `db_topic_comment` VALUES (4, 1, 16, '{\"ops\":[{\"insert\":\"学习强国\\n\"}]}', '2025-03-06 03:14:21', -1);
INSERT INTO `db_topic_comment` VALUES (5, 1, 16, '{\"ops\":[{\"insert\":\"天天学习\\n\"}]}', '2025-03-06 03:23:09', -1);
INSERT INTO `db_topic_comment` VALUES (6, 1, 16, '{\"ops\":[{\"attributes\":{\"color\":\"#4d4d4d\"},\"insert\":\"Consumer的语义是消费的意思\"},{\"insert\":\"\\n\"}]}', '2025-03-06 03:27:39', -1);
INSERT INTO `db_topic_comment` VALUES (7, 1, 17, '{\"ops\":[{\"insert\":\"破车\"},{\"attributes\":{\"header\":1},\"insert\":\"\\n\"}]}', '2025-03-06 03:29:10', -1);
INSERT INTO `db_topic_comment` VALUES (8, 1, 16, '{\"ops\":[{\"insert\":\"厉害了我的国1\\n\"}]}', '2025-03-09 15:03:12', -1);
INSERT INTO `db_topic_comment` VALUES (9, 1, 16, '{\"ops\":[{\"insert\":\"厉害了我的国2\\n\"}]}', '2025-03-09 15:03:12', -1);
INSERT INTO `db_topic_comment` VALUES (10, 1, 16, '{\"ops\":[{\"insert\":\"厉害了我的国3\\n\"}]}', '2025-03-09 15:03:12', -1);
INSERT INTO `db_topic_comment` VALUES (11, 1, 16, '{\"ops\":[{\"insert\":\"爱国红\\n\"}]}', '2025-03-09 07:04:07', -1);
INSERT INTO `db_topic_comment` VALUES (12, 1, 16, '{\"ops\":[{\"insert\":\"太强了\\n\"}]}', '2025-03-09 15:03:12', -1);
INSERT INTO `db_topic_comment` VALUES (13, 1, 16, '{\"ops\":[{\"insert\":\"厉害\\n\"}]}', '2025-03-09 15:03:12', -1);
INSERT INTO `db_topic_comment` VALUES (17, 1, 16, '{\"ops\":[{\"insert\":\"我是最新的评论\\n\"}]}', '2025-03-09 07:13:17', -1);
INSERT INTO `db_topic_comment` VALUES (18, 1, 16, '{\"ops\":[{\"insert\":\"我是厉害了我的国的评论\\n\"}]}', '2025-03-09 07:57:26', 3);
INSERT INTO `db_topic_comment` VALUES (19, 1, 16, '{\"ops\":[{\"insert\":\"我是最新评论的评论\\n\"}]}', '2025-03-09 07:59:48', 17);
INSERT INTO `db_topic_comment` VALUES (20, 1, 16, '{\"ops\":[{\"insert\":\"我是厉害了我的国的评论的评论\\n\"}]}', '2025-03-09 08:07:01', 18);
INSERT INTO `db_topic_comment` VALUES (21, 1, 16, '{\"ops\":[{\"insert\":\"666\\n\"}]}', '2025-03-09 08:28:40', 18);
INSERT INTO `db_topic_comment` VALUES (22, 1, 16, '{\"ops\":[{\"insert\":\"回复666的评论\\n\"}]}', '2025-03-09 08:29:41', 21);
INSERT INTO `db_topic_comment` VALUES (23, 1, 16, '{\"ops\":[{\"insert\":\"回复\\n\"}]}', '2025-03-09 08:31:24', 4);
INSERT INTO `db_topic_comment` VALUES (24, 1, 15, '{\"ops\":[{\"insert\":\"I\'m fine。\\n\"}]}', '2025-03-10 05:43:47', 2);
INSERT INTO `db_topic_comment` VALUES (25, 6, 18, '{\"ops\":[{\"insert\":\"Hello everyone\\n\"}]}', '2025-03-10 06:11:26', -1);
INSERT INTO `db_topic_comment` VALUES (26, 5, 18, '{\"ops\":[{\"insert\":\"卧槽牛B\\n\"}]}', '2025-03-10 06:12:41', 25);
INSERT INTO `db_topic_comment` VALUES (27, 5, 17, '{\"ops\":[{\"insert\":\"are you ok?\\n\"}]}', '2025-03-10 08:29:46', -1);
INSERT INTO `db_topic_comment` VALUES (28, 1, 18, '{\"ops\":[{\"insert\":\"I’m fine\\n\"}]}', '2025-03-11 01:34:42', 26);
INSERT INTO `db_topic_comment` VALUES (29, 1, 17, '{\"ops\":[{\"insert\":\"I\'m fine.\\n\"}]}', '2025-03-11 01:39:24', 27);
INSERT INTO `db_topic_comment` VALUES (30, 5, 17, '{\"ops\":[{\"insert\":\"雷军\\n\"}]}', '2025-03-11 02:15:55', -1);
INSERT INTO `db_topic_comment` VALUES (31, 1, 19, '{\"ops\":[{\"insert\":\"卧槽这么赚钱的吗\\n\"}]}', '2025-03-11 02:48:30', -1);
INSERT INTO `db_topic_comment` VALUES (32, 5, 19, '{\"ops\":[{\"insert\":\"对啊，要努力了\\n\"}]}', '2025-03-11 02:48:59', 31);
INSERT INTO `db_topic_comment` VALUES (33, 1, 19, '{\"ops\":[{\"insert\":\"996启动！\\n\"}]}', '2025-03-11 02:50:10', 32);

-- ----------------------------
-- Table structure for db_topic_interact_collect
-- ----------------------------
DROP TABLE IF EXISTS `db_topic_interact_collect`;
CREATE TABLE `db_topic_interact_collect`  (
  `tid` int NULL DEFAULT NULL COMMENT '帖子id',
  `uid` int NULL DEFAULT NULL COMMENT '用户id',
  `date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  UNIQUE INDEX `tid_uid_like`(`tid` ASC, `uid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '帖子收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_topic_interact_collect
-- ----------------------------
INSERT INTO `db_topic_interact_collect` VALUES (13, 1, '2025-03-02 07:13:18');
INSERT INTO `db_topic_interact_collect` VALUES (11, 1, '2025-03-03 03:33:03');
INSERT INTO `db_topic_interact_collect` VALUES (12, 5, '2025-03-03 08:36:42');
INSERT INTO `db_topic_interact_collect` VALUES (13, 5, '2025-03-04 05:14:30');
INSERT INTO `db_topic_interact_collect` VALUES (18, 6, '2025-03-10 06:05:28');
INSERT INTO `db_topic_interact_collect` VALUES (19, 5, '2025-03-11 02:47:57');

-- ----------------------------
-- Table structure for db_topic_interact_like
-- ----------------------------
DROP TABLE IF EXISTS `db_topic_interact_like`;
CREATE TABLE `db_topic_interact_like`  (
  `tid` int NULL DEFAULT NULL COMMENT '贴子id',
  `uid` int NULL DEFAULT NULL COMMENT '用户id',
  `date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  UNIQUE INDEX `tid_uid_like`(`tid` ASC, `uid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '帖子喜欢表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_topic_interact_like
-- ----------------------------
INSERT INTO `db_topic_interact_like` VALUES (11, 1, '2025-03-02 07:02:13');
INSERT INTO `db_topic_interact_like` VALUES (13, 1, '2025-03-02 07:13:17');
INSERT INTO `db_topic_interact_like` VALUES (12, 1, '2025-03-03 02:22:58');
INSERT INTO `db_topic_interact_like` VALUES (12, 5, '2025-03-03 08:36:41');
INSERT INTO `db_topic_interact_like` VALUES (13, 5, '2025-03-04 05:14:27');
INSERT INTO `db_topic_interact_like` VALUES (15, 1, '2025-03-04 07:48:25');
INSERT INTO `db_topic_interact_like` VALUES (5, 1, '2025-03-06 02:40:02');
INSERT INTO `db_topic_interact_like` VALUES (18, 6, '2025-03-10 06:05:27');
INSERT INTO `db_topic_interact_like` VALUES (19, 5, '2025-03-11 02:47:56');

-- ----------------------------
-- Table structure for db_topic_type
-- ----------------------------
DROP TABLE IF EXISTS `db_topic_type`;
CREATE TABLE `db_topic_type`  (
  `id` int UNSIGNED NOT NULL COMMENT '帖子类型id',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '名称',
  `desc` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '描述',
  `color` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '颜色',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '帖子类型信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_topic_type
-- ----------------------------
INSERT INTO `db_topic_type` VALUES (1, '日常闲聊', '在这里分享你的各种日常', '#1E90FF');
INSERT INTO `db_topic_type` VALUES (2, '真诚交友', '在校园里寻找与自己志同道合的朋友', '#CE1EFF');
INSERT INTO `db_topic_type` VALUES (3, '问题反馈', '反馈你在校园里遇到的问题', '#E07373');
INSERT INTO `db_topic_type` VALUES (4, '恋爱官宣', '向大家展示你的恋爱成果', '#E0CE73');
INSERT INTO `db_topic_type` VALUES (5, '踩坑记录', '将你遇到的坑分享给大家，防止其他人再次入坑', '#3BB62A');

SET FOREIGN_KEY_CHECKS = 1;
