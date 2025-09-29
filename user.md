
# 用户接口
## 1. 用户注册登录
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
| created_at       | DateTime  | 是   | 注册时间                             |
### (1).注册获取验证码

- **接口名称**：用户端注册获取验证码
- **请求方式**：POST
- **请求路径**：`/api/user/verification`
- **请求参数类型**：JSON
- **返回类型**：统一响应结构（Result）

请求参数
| 字段名           | 类型      | 必填 | 说明         |
|------------------|-----------|------|--------------|
| phone            | String    | 是   | 手机号       |
| password         | String    | 是   | 密码         |



**示例请求体：**
```json
{
  "phone": "13800000000",
  "scene":"register"
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
- **接口名称**：用户注册
- **请求方式**：POST
- **请求路径**：`/api/user/register`
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
### (3). 登录获取验证码
- **接口名称**：用户端注册获取验证码
- **请求方式**：POST
- **请求路径**：`/api/user/verification`
- **请求参数类型**：JSON
- **返回类型**：统一响应结构（Result）

请求参数
| 字段名           | 类型      | 必填 | 说明         |
|------------------|-----------|------|--------------|
| phone            | String    | 是   | 手机号       |




**示例请求体：**
```json
{
  "phone": "13800000000",
  "scene":"login"
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
### 用户端登录
- **接口名称**：用户登录
- **请求方式**：POST
- **请求路径**：`/api/user/login`
- **请求参数类型**：JSON
- **返回类型**：统一响应结构（Result）获取验证码获取验证码
  
请求参数

  | 字段            | 类型      | 是否必填                        | 约束                           | 说明             |
  | ------------- | ------- | --------------------------- | ---------------------------- | -------------- |
  | `phone`       | string  | 是                           | 中国大陆手机号 11 位                 | 用户手机号          |
  | `loginType`  | enum    | 是                           | `password` \| `sms`          | 登录方式           |
  | `password`    | string  | 当 `login_type=password` 时必填 | *bcrypt/scrypt* 哈希后再经 TLS 传输 | 登录密码           |
  | `code`        | string  | 当 `login_type=sms` 时必填      | 6 位数字                        | 短信验证码          |

示例返回
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "user": {
      "id": 123,
      "phone": "13812345678",
      "name": "张三",
      "community_name": "幸福小区",
      "building_number": "3栋",
      "unit_number": "1单元",
      "room_number": "1504",
      "is_verified": true
    },
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

## 2. 投票功能
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
| created_at     | DateTime  | 是   | 模板创建时间                                                 |

### (1). 获取总体投票活动

- **接口名称**：用户获取
- **请求方式**：GET
- **请求路径**：`/api/user/vote `
- **请求参数类型**：JSON
- **返回类型**：统一响应结构（Result）
 **请求头**：Authorization: Bearer <token>

返回参数
| 字段名           | 类型      | 必填 | 说明         |
|------------------|-----------|------|--------------|
| id              | Integer  | 是  | 活动 ID     |
| title           | String   | 是  | 活动标题（大标题） |
| attachmentUrl  | String   | 否  | 活动补充材料的文件或图片URL |
| startTime     | DateTime | 是  | 开始时间      |
| endTime       | DateTime | 是  | 结束时间      |
| isOfficial    | Boolean  | 是  | 是否官方投票    |
| communityName | String   | 是  | 小区名称      |
| createdAt     | String   | 是  | 活动创建时间      |
| voteScope     | String   | 是  | 投票范围      |


请求参数

| 字段名             | 类型       | 必填 | 说明                                                          |
| --------------- | -------- | -- | ------------------------------------------------------------------- |
| communityName | String   | 否  | 小区名称**关键字**）              |
| endTime   | DateTime | 否  | **截止时间**（仅返回 `end_time ≤ end_time_to` 的活动；|
| page            | Integer  | 否  | 页码，默认 1                                                         |
| pageSize      | Integer  | 否  | 每页数量，默认 10                                                      |

| 字段名        | 类型      | 必填 | 说明                                  |
| ---------- | ------- | -- | ----------------------------------- |
| page       | Integer | 是  | 当前页码                                |
| pageSize | Integer | 是  | 当前每页数量                              |
| total      | Integer | 是  | 筛选后总记录数                         |
| hasMore  | Boolean | 是  | 是否还有下一页                          |



可能的错误返回
```json
{
  "code": 400,
  "message": "目前暂无投票",
  "data": null
}
{ "code": 401, 
  "message": "未登录或登录已过期", 
  "data": null 
}

{ "code": 403, 
  "message": "无权限访问或不在投票范围内", 
  "data": null 
}
```
可能的正确返回
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 17,
      "title": "物业费调整方案投票",
      "startTime": "2025-09-01 00:00:00",
      "endTime": "2025-09-10 23:59:59",
      "isOfficial": true,
      "communityName": "蓝天小区"
    },
    {
      "id": 16,
      "title": "电梯维保服务更换投票",
      "startTime": "2025-08-25 09:00:00",
      "endTime": "2025-09-05 23:59:59",
      "isOfficial": false,
      "communityName": "白云小区"
    },
    {
      "id": 15,
      "title": "小区绿化整改投票",
      "startTime": "2025-08-20 00:00:00",
      "endTime": "2025-09-05 23:59:59",
      "isOfficial": true,
      "communityName": "蓝天小区"
    }
  ],
  "page_meta": {
    "page": 1,
    "pageSize": 10,
    "total": 27
  }
}

```

###（2) 获取具体活动和里面议题内容

- **接口名称**：用户获取
- **请求方式**：GET
- **请求路径**：`/api/user/vote/:activityId`
- **请求参数类型**：JSON
- **返回类型**：统一响应结构（Result）
- **请求头**：Authorization: Bearer <token>

返回参数

| 字段名            | 类型             | 必填 | 说明                              |
| -------------- | -------------- | -- | ------------------------------- |
| id             | Integer        | 是  | 议题 ID                           |
| activityId   | Integer        | 是  | 活动 ID                           |
| questionText | String         | 是  | 议题文案                            |
| templateId   | Integer        | 是  | 选项模板 ID                         |
| createdAt      | String        | 是  | 议题创建时间                       |


请求参数

| 字段名          | 类型      | 必填 | 说明   |
| ------------ | ------- | -- | ----- |
| activityId | Integer | 是  | 活动 ID |


可能的错误返回
```json
{
  "code": 400,
  "message": "目前无该投票信息",
  "data": null
}
{ "code": 401, 
  "message": "未登录或登录已过期", 
  "data": null 
}
{ "code": 403, 
  "message": "无权限访问或不在投票范围内", 
  "data": null 
}

```
可能的正确返回
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "小区绿化整改投票",
    "attachmentUrl": null,
    "startTime": "2025-08-10T01:00:00.000+00:00",
    "endTime": "2025-08-15T10:00:00.000+00:00",
    "isOfficial": true,
    "communityName": "幸福花园",
    "buildingNumber": "1栋",
    "unitNumber": "1单元",
    "createdAt": "2025-08-08 10:20:00",
    "voteScope": "幸福花园 1栋栋 1单元单元",
    "questions": [
      {
        "questionsId": 1,
        "activityId": 1,
        "questionText": "是否同意更换小区草坪",
        "options": [
          "赞同",
          "反对",
          "弃权",
          "从多"
        ],
        "myVote": null,
        "createdAt": "2025-08-08 10:30:00"
      },
      {
        "questionsId": 2,
        "activityId": 1,
        "questionText": "是否增加休闲座椅",
        "options": [
          "赞同A",
          "赞同B",
          "反对",
          "弃权",
          "从多"
        ],
        "myVote": null,
        "createdAt": "2025-08-08 10:31:00"
      },
      {
        "questionsId": 3,
        "activityId": 1,
        "questionText": "是否增设健身器材",
        "options": [
          "满意",
          "基本满意",
          "一般",
          "较差",
          "不满意"
        ],
        "myVote": null,
        "createdAt": "2025-08-08 10:32:00"
      }
    ]
  }
}
```

###（3）提交投票（不传 userId，后端从登录态识别）

- **接口名称**：用户获取
- **请求方式**：POST
- **请求路径**：`/api/user/vote`
- **请求参数类型**：Query
- **返回类型**：统一响应结构（Result）
- **请求头**：Authorization: Bearer <token>

返回参数

无

请求参数

| activityId    | Integer   | 是   | 投票活动ID（关联 vote_activities.id）                        |
| questionId    | Integer   | 是   | 投票议题ID（关联 vote_questions.id）                         |
| selectedOption| String    | 是   | 用户选择的投票选项，例如“赞同A”                               |
| voteMethod    | String    | 是   | 投票方式，例如“线上”、“短信”、“线下”                         |
| voteTime      | DateTime  | 是   | 投票时间                                                     |



可能的正确返回
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "activityId": 1,
    "questionId": 101,
    "selectedOption": "赞同",
    "voteMethod": "线上",
    "voteTime": "2025-08-31 15:20:00"
  }
}
```

可能的错误返回
```json
{  "code": 400,
   "message": "活动未开始或已结束", 
   "data": null
}

{  "code": 400,
   "message": "您已完成该议题投票", 
   "data": null 
}
{ "code": 401, 
  "message": "未登录或登录已过期", 
  "data": null 
}
{ "code": 403, 
  "message": "不在投票范围", 
  "data": null 
}
```

（4）投票历史（不传 userId，后端从登录态识别）
 获取投票历史列表（当前登录用户）
- **接口名称**：用户获取（投票历史）
- **请求方式**：GET
- **请求路径**：/api/user/vote/history
- **请求参数类型**：JSON
- **返回类型**：统一响应结构（Result）
- **请求头**：Authorization: Bearer <token>

请求参数

| 字段名        | 类型      | 必填 | 说明               |
| ---------- | ------- | -- | ---------------- |
| activityId | Integer | 否  | 指定活动筛选（不传返回全部历史） |
| page       | Integer | 否  | 页码，默认 1          |
| pageSize   | Integer | 否  | 每页数量，默认 10       |

鉴权：从登录态 Authorization: Bearer <token> 识别当前用户，后端以此查询，不信任前端自填 userId。

返回参数

| 字段名            | 类型       | 必填 | 说明                          |
| -------------- | -------- | -- | --------------------------- |
| activityId     | Integer  | 是  | 活动 ID                       |
| activityTitle  | String   | 是  | 活动标题（展示用）                   |
| communityName  | String   | 是  | 小区名称（展示用）                   |
| questionId     | Integer  | 是  | 议题 ID                       |
| questionText   | String   | 是  | 议题内容（展示用）                   |
| selectedOption | String   | 是  | 我选择的选项                      |
| voteMethod     | String   | 是  | 投票方式（如：线上）                  |
| voteTime       | DateTime | 是  | 投票时间（`YYYY-MM-DD HH:mm:ss`） |

| 字段名      | 类型      | 必填 | 说明                                 |
| -------- | ------- | -- | ---------------------------------- |
| page     | Integer | 是  | 当前页码                               |
| pageSize | Integer | 是  | 当前每页数量                             |
| total    | Integer | 是  | 总记录数                               |
| hasMore  | Boolean | 是  | 是否还有下一页（`page * pageSize < total`） |

可能的错误返回
```json
{   "code": 401, 
	"message": "未登录或登录已过期", 
	"data": null 
	}
	
{  "code": 400, 
   "message": "暂无投票历史", 
   "data": null
}

```

可能的正确返回
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "activityId": 17,
      "activityTitle": "物业费调整方案投票",
      "communityName": "蓝天小区",
      "questionId": 301,
      "questionText": "是否同意调整收费标准A？",
      "selectedOption": "赞同",
      "voteMethod": "线上",
      "voteTime": "2025-09-02 10:15:20"
    }
  ],
  "page_meta": { "page": 1, "pageSize": 10, "total": 12, "hasMore": true }
}

```