# 管理端

## 1. 管理端注册和登录接口基本信息
数据库对应数据表admin

| 字段名          | 类型         | 必填 | 说明           |
|------------------|--------------|------|----------------|
| id               | bigint(20)   | 是   | 主键           |
| phone            | varchar(20)  | 是   | 手机号         |
| password         | varchar(100) | 是   | 密码（加密后） |
| idCard           | varchar(18)  | 否   | 身份证号码     |
| name             | varchar(50)  | 否   | 姓名           |
| avatar           | varchar(255) | 否   | 头像图片 URL   |
| gender           | varchar(50)  | 否   | 性别           |
| birthday         | date         | 否   | 出生日期       |
| education        | varchar(20)  | 否   | 学历           |
| politicalStatus  | varchar(20)  | 否   | 政治面貌       |
| address          | varchar(255) | 否   | 地址           |
| validFrom        | date         | 否   | 有效期开始时间 |
| validTo          | date         | 否   | 有效期结束时间 |
| organization     | varchar(100) | 否   | 组织           |
| position         | varchar(50)  | 否   | 岗位           |
| create_time      | datetime     | 是   | 创建时间       |
| update_time      | timestamp    | 是   | 修改时间       |

### (1).注册获取验证码

- **接口名称**：管理端获取验证码
- **请求方式**：POST
- **请求路径**：`/api/admin/verification`
- **请求参数类型**：JSON
- **返回类型**：统一响应结构（Result）

请求参数
| 字段名           | 类型      | 必填 | 说明         |
|------------------|-----------|------|--------------|
| phone            | String    | 是   | 手机号       |
| password         | String    | 是   | 密码         |
| verification     | String    | 是   | 验证码   |


**示例请求体：**
```json
{
  "phone": "13800000000",
  "password": "123456"
}
```
可能的错误返回
```json
{
  "code": 400,
  "message": "手机号已注册",
  "data": null
}
```
```json
{
"code": 404,
"message": "程序更新中清等待",
"data": null
}
```
可能的正确返回
```json
{
"code": 200,
"message": "success",
"data": null
}

```

### (2). 注册
- **接口名称**：管理端注册
- **请求方式**：POST
- **请求路径**：`/api/admin/register`
- **请求参数类型**：JSON
- **返回类型**：统一响应结构（Result）


**示例请求体：**
```json
{
  "phone": "13800000000",
  "password": "123456",
  "verification":"24565"
}
```
返回参数

- **code**：状态码（200为成功）
- **message**：提示信息
- **data**：用户信息（UserVO）

**示例返回体：**
可能的错误返回

- 手机号已注册时，返回：
```json
{
  "code": 400,
  "message": "验证码过期或者验证码不准确",
  "data": null
}

```
```json
{
  "code": 400,
  "message": "手机号已经注册",
  "data": null
}
```

### (3). 登录
- **接口名称**：管理端注册
- **请求方式**：POST
- **请求路径**：`/api/admin/login`
- **请求参数类型**：JSON
- **返回类型**：统一响应结构（Result）

## 2. 管理员投票管理接口文档
涉及的数据库  投票活动vote_activities

| 字段名         | 类型      | 必填 | 说明                                                         |
|----------------|-----------|------|--------------------------------------------------------------|
| id             | Integer   | 是   | 投票活动ID，自增主键                                         |
| title          | String    | 是   | 活动标题，例如“小区绿化整改投票”                             |
| attachment_url | String    | 否   | 活动补充材料的文件或图片URL                                   |
| start_time     | DateTime  | 是   | 投票开始时间                                                 |
| end_time       | DateTime  | 是   | 投票结束时间                                                 |
| is_official    | Boolean   | 是   | 是否为官方投票：1-是，0-否                                    |
| vote_scope     | String    | 否   | 投票范围，例如“小区A 3栋 1单元”                               |
| created_at     | DateTime  | 是   | 活动创建时间                                                 |

投票活动议题vote_questions

| 字段名         | 类型      | 必填 | 说明                                                         |
|----------------|-----------|------|--------------------------------------------------------------|
| id             | Integer   | 是   | 议题ID，自增主键                                             |
| activity_id    | Integer   | 是   | 所属投票活动ID（关联 vote_activities.id）                    |
| question_text  | String    | 是   | 议题内容，例如“您是否同意小区绿化整改方案？”                  |
| template_id    | Integer   | 是   | 投票选项模板ID（关联 vote_option_templates.id）               |
| created_at     | DateTime  | 是   | 议题创建时间                                                 |

用户投票表（user_votes）

| 字段名         | 类型      | 必填 | 说明                                                         |
|----------------|-----------|------|--------------------------------------------------------------|
| id             | Integer   | 是   | 用户投票ID，自增主键                                         |
| user_id        | Integer   | 是   | 用户ID（关联 users.id）                                      |
| activity_id    | Integer   | 是   | 投票活动ID（关联 vote_activities.id）                        |
| question_id    | Integer   | 是   | 投票议题ID（关联 vote_questions.id）                         |
| selected_option| String    | 是   | 用户选择的投票选项，例如“赞同A”                               |
| vote_method    | String    | 是   | 投票方式，例如“线上”、“短信”、“线下”                         |
| vote_time      | DateTime  | 是   | 投票时间                                                     |
| area_size      | Decimal   | 否   | 用户对应房屋面积（平方米），可用于加权统计                     |
模板项表（vote_option_templates）

| 字段名         | 类型      | 必填 | 说明                                                         |
|----------------|-----------|------|--------------------------------------------------------------|
| id             | Integer   | 是   | 模板ID，自增主键                                             |
| template_name  | String    | 是   | 模板名称，例如“赞同/反对/弃权/从多”                           |
| option_list    | String    | 是   | 该模板包含的选项，多个选项用逗号分隔，例如“赞同,反对,弃权,从多” |
| created_at     | DateTime  | 是   | 模板创建时间   

涉及的数据库users
| 字段名           | 类型      | 必填 | 说明                                 |
|------------------|-----------|------|--------------------------------------|
| id               | Integer   | 是   | 用户ID，自增主键                     |
| phone            | String    | 是   | 手机号，例如 13812345678              |
| password         | String    | 是   | 用户密码（加密存储）                  |
| name             | String    | 否   | 姓名                                 |
| gender           | Enum      | 否   | 性别                                 |
| id_card          | String    | 否   | 身份证号码                           |
| community_name   | String    | 是   | 小区名称                             |
| building_number  | String    | 否   | 楼栋号，例如 3 栋                     |
| unit_number      | String    | 否   | 单元号，例如 1 单元                   |
| room_number      | String    | 否   | 房间号，例如 1504                     |
| area_size        | Decimal   | 否   | 房屋面积（平方米）                    |
| is_verified      | Boolean   | 是   | 是否认证成功：1-是，0-否               |
| created_at       | DateTime  | 是   | 注册时间       |

###  (1). 获取投票活动列表接口

- **接口说明**
管理员登录后，在“投票管理”页面加载所有投票活动列表，
支持通过关键词和状态进行筛选。该接口将一次性返回所有符合条件的投票活动。
- **接口名称**：获取投票活动列表
- **请求方式**：Get
- **请求路径**：`/api/admin/votes`
- **请求参数类型**：Query
- **返回类型**：统一响应结构（Result）

请求参数

| 字段名     | 类型     | 必填 | 说明                               |
| ------- | ------ | -- | -------------------------------- |
| keyword | String | 否  | 搜索关键词，可匹配小区名或投票议题                |
| status  | String | 否  | 投票状态 (例如: "ongoing", "finished") |

正确返回示例

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": "1",
      "topics": [],
      "communityName": "乐茄小区",
      "title": "杭州市临平区商品房性质老旧小区住宅加装电梯项目",
      "beginTime": "2025/06/25 00:00",
      "endTime": "2025/07/08 13:07",
      "official": 1,
      "voteNum": 0,
      "vote_scope": "ALL"
    },
    {
      "id": "2",
      "topics": [],
      "communityName": "乐茄小区",
      "title": "杭州市临平区商品房性质老旧小区住宅加装电梯项目",
      "beginTime": "2025/06/25 00:00",
      "endTime": "2025/07/08 13:07",
      "official": 1,
      "voteNum": 0,
      "vote_scope": "ALL"
    }
  ]
}
```

错误返回示例

```json
{
  "code": 401,
  "message": "认证失败，请重新登录",
  "data": null
}
```

```json
{
  "code": 500,
  "message": "服务器内部错误，获取列表失败",
  "data": null
}
```

### (2). 获取投票统计详情接口

- **接口说明**
  点击任一投票活动，查看其详细的统计数据，包括图表所需数据。
- **接口名称**：获取投票统计详情
- **请求方式**：GET
- **请求路径**：`/api/admin/votes/{id}`
- **请求参数类型**：Path
- **返回类型**：统一响应结构（Result）

请求路径参数

| 字段名 | 类型     | 必填 | 说明     |
| --- | ------ | -- | ------ |
| id  | Int | 是  | 投票活动ID |

正确返回示例

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": "2",
    "topics": [],
    "communityName": "乐茄小区",
    "title": "杭州市临平区商品房性质老旧小区住宅加装电梯项目",
    "beginTime": "2025/06/25 00:00",
    "endTime": "2025/07/08 13:07",
    "official": 1,
    "voteNum": 0,
    "vote_scope": "ALL"
  }
}

```

错误返回示例

```json
{
  "code": 500,
  "message": "数据库操作失败，发起投票失败",
  "data": null
}
```

---

### (3). 新增投票活动接口

- **接口说明**：管理员在投票管理页面点击右下角加号，填写信息并发起新的投票活动。
- **接口名称**：新增投票活动
- **请求方式**：POST
- **请求路径**：`/api/admin/votes`
- **请求参数类型**：JSON
- **返回类型**：统一响应结构（Result）
请求参数
| 字段名           | 类型              | 必填 | 说明                                   |
| ------------- | --------------- | -- | ------------------------------------ | | activity_name    | varchar(100) | 是   | 活动名称                 |
  | topics_1         | varchar(200) | 是   | 议题一                   |
  | topics_2         | varchar(200) | 否   | 议题二                   |
  | topics_3         | varchar(200) | 否   | 议题三                   |
  | begin_time       | datetime   | 是   | 开始时间                 |
  | end_time         | datetime   | 是   | 结束时间                 |
  | official         | tinyint(1) | 是   | 是否官方投票：0-否，1-是 |
  | community_name   | varchar(100) | 是   | 小区名称                 |
  | vote_scope       | varchar(200) | 是   | 已设置投票范围           |
  | attachmentUrl       | varchar(200) | 否   | 附件           |

| 字段名       | 类型            | 必填 | 说明                                |
| --------- | ------------- | -- | --------------------------------- |
| type      | String        | 是  | 范围类型。"ALL"代表全体业主；"PARTIAL"代表部分业主。 |
| selection | Array[Object] | 否  | 当 type 为 "PARTIAL" 时必填。定义具体的楼幢和单元 |

voteScope.selection 数组内对象结构

| 字段名          | 类型            | 必填 | 说明                 |
| ------------ | ------------- | -- | ------------------ |
| buildingName | String        | 是  | 楼幢名称，例如 "1幢", "A座" |
| units        | Array[String] | 是  | 该楼幢下被选中的单元列表       |

Issue 对象结构

| 字段名           | 类型            | 必填 | 说明                                      |
| ------------- | ------------- | -- | --------------------------------------- |
| title         | String        | 是  | 议题标题                                    |
| optionsType   | String        | 是  | 选项类型。"STANDARD" 代表常用选项；"CUSTOM" 代表自定义选项 |
| customOptions | Array[String] | 否  | 当 optionsType 为 "CUSTOM" 时必填，数组长度不超过 10 |

正确返回示例

```json
{
  "code": 200,
  "message": "投票发起成功",
  "data": {
    "voteId": "vote_new_005"
  }
}
```

错误返回示例





```json
{
  "code": 400,
  "message": "选择部分业主时，必须指定楼幢和单元",
  "data": null
}
```


```json
{
  "code": 409,
  "message": "该小区已存在同名投票活动",
  "data": null
}
```

```json
{
  "code": 500,
  "message": "数据库操作失败，发起投票失败",
  "data": null
}
```
