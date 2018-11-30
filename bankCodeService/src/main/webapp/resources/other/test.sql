

CREATE TABLE `cnaps` (
	`code` BIGINT(20) NOT NULL COMMENT '联行号',
	`name` VARCHAR(50) NULL DEFAULT NULL COMMENT '银行名称',
	`clearing_bank_code` BIGINT(20) NULL DEFAULT NULL COMMENT '清算行号',
	`clearing_bank_level` SMALLINT(2) NULL DEFAULT NULL COMMENT '清算行级别',
	`provider_code` VARCHAR(20) NULL DEFAULT NULL COMMENT '银行编码',
	`ad_Code` INT(8) NULL DEFAULT NULL COMMENT '行政区划码',
	`create_date` DATETIME NULL DEFAULT NULL COMMENT '创建日期',
	`last_modify_date` DATETIME NULL DEFAULT NULL COMMENT '最后修改日期',
	`vision` SMALLINT(5) NULL DEFAULT NULL COMMENT '版本号',
	PRIMARY KEY (`code`)
)
COMMENT='联行号信息管理'
COLLATE='utf8_general_ci'
ENGINE=InnoDB;


insert into cnaps(code, name,clearing_bank_code,clearing_bank_level,provider_code,ad_Code)   
values("102100000030","中国工商银行股份有限公司北京市分行营业部", "102100099996","-1", "ICBC", "1000"); 
insert into cnaps(code, name,clearing_bank_code,clearing_bank_level,provider_code,ad_Code)   
values("102100000021","中国工商银行股份有限公司北京通州支行新华分理处", "102100099996","-1", "ICBC", "1000"); 



/*==============================================================*/
/* Table ： 红包表 */
/*==============================================================*/

CREATE TABLE T_RED_PACKET(
id  INT(12) NOT NULL AUTO_INCREMENT,
user_id  INT(12) NOT NULL COMMENT '发红包用户ID',
amount  DECIMAL(16,2) NOT NULL COMMENT '红包金额',
send_date  TIMESTAMP  NOT NULL COMMENT '发红包时间',
total  INT(12)  NOT NULL COMMENT '小红包总数',
unit_amount  DECIMAL(12)  NOT NULL COMMENT '单个小红包金额',
stock  INT(12)  NOT NULL COMMENT '剩余小红包个数',
version  INT(12)  DEFAULT 0  NOT NULL COMMENT '版本',
note  VARCHAR(256)  NULL COMMENT '备注',
PRIMARY KEY CLUSTERED(id)
);


/*==============================================================*/
/* Table ：用户抢红包表 */
/*==============================================================*/

CREATE TABLE T_USER_RED_PACKET(
id  INT(12)  NOT NULL AUTO_INCREMENT,
red_packet_id  INT(12)  NOT NULL COMMENT '红包编号',
user_id  INT(12)  NOT NULL COMMENT '抢红包用户编号',
amount  DECIMAL(16,2)  NOT NULL COMMENT '抢红包金额',
grab_time  TIMESTAMP  NOT NULL COMMENT '抢红包时间',
note  VARCHAR(256)  NULL COMMENT '备注',
PRIMARY KEY CLUSTERED(id)
);

/**
* 插入一个 20 万元金额， 2 万个小红包，每个 10 元的红包数据
*/
INSERT INTO T_RED_PACKET(id,user_id,amount,send_date,total,unit_amount,stock,note)
                  VALUES(1,1,200000.00,now(),20000,10.00,20000,"20万元金额，2万个小红包，每个10元");


UserRedPacketService  RedPacketService	

sha1: 7cfb4342127e7ab3d63ac05e0d3615fd50b45b06			  
				  

--缓存抢红包列表信息 列表key
local listKey = 'red_packet_list_'..KEYS[1]	
--当前被抢红包key
local redPacket = 'red_packet_'..KEYS[1]
--获取当前红包库存
local stock = tonumber(redis.call('hget',redPacket,'stock'))	
--没有库存，返回0
if stock <= 0 then return 0 end
--库存减1
stock = stock -1
--保存当前库存
redis.call('hset',redPacket,'stock',tostring(stock))
--往链表中加入当前红包信息
redis.call('rpush',listKey,ARGV[1])
--如果是最后一个红包，则返回2，表示抢红包已经结束，需要将列表中的数据保存到数据库中
if stock == 0 then return 2 end
--如果并非最后一个红包，则返回1，表示抢红包成功
return 1


hset red_packet_5 stock 20000
hset red_packet_5 unit_amount 10

hset red_packet_1 stock 20
hset red_packet_1 unit_amount 10
hgetall red_packet_1	

FLUSHALL
























