# 管理端投票功能实现文档（Admin）

> 统一约定  
> - **前缀**：所有管理端接口均以 `/api/admin/**` 开头。  
> - **鉴权**：除登录外，所有接口**必须**携带请求头 `Authorization: Bearer <adminToken>`。    
> - **分页**：`page_meta = { page, pageSize, total, hasMore }`；`hasMore = page * pageSize < total`。  
> - **时间格式**：`YYYY-MM-DD HH:mm:ss`（示例见下）。  
> - **字段命名**：接口层使用 `camelCase`，数据库层使用下划线命名；文档内给出字段映射。
---

> 接口层仅做**驼峰转换**与**聚合**。

---
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

### 1.1 注册获取验证码
- **接口**：`POST /api/admin/verification`  
- **请求头**：无  
- **请求体**

请求参数

| 字段名 | 类型  | 必填   | 说明 |  
|------ |-------|-------|-------|
| phone | String | 是 | 手机号 |
| password | String | 是 | 密码 |
**示例请求体：**
```json
{
  "phone": "13800000000",
  "scene":"register"
}
```

- **成功返回**
```json
{ "code": 200, "message": "success", "data": null }
```
- **失败返回**
```json
{ "code": 400, "message": "手机号已注册", "data": null }
```

### 1.2 管理端注册
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

- **可能的成功返回**
```json
{ "code": 200, "message": "success", "data": { "phone": "13800000000" } }
```

- **可能的错误返回**
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

### 1.3 登录获取验证码
- **接口名称**：用户端注册获取验证码
- **请求方式**：POST
- **请求路径**：`/api/admin/verification`
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

### 1.4 管理端登录（签发 adminToken）
- **接口名称**：管理端登录
- **请求方式**：POST
- **请求路径**：`/api/admin/login`
- **请求参数类型**：JSON
- **返回类型**：统一响应结构（Result）

请求参数
| 字段名    | 类型    | 必填   | 说明    |
|-----------|--------|--------|--------|
| loginType | String |   是   | `password` \| `sms` |
| phone     | String |    是  | 手机号 |
| password  | String | 当 `password` 登录时必填 | 密码 |
| code      | String | 当 `sms` 登录时必填 | 短信验证码 |


返回参数
| 字段名    | 类型    | 必填   | 说明    |
|adminToken | string  | 是    |登录成功后返回 adminToken；除登录/注册/验证码外，其它都要|

- **成功返回**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "admin": { "id": 10001, "phone": "138****0001" },
    "adminToken": "eyJhbGciOi..."
  }
}
```

- **错误返回**
{ "code": 400, "message": "账号或密码不正确", "data": null }
{ "code": 400, "message": "验证码不正确或已过期", "data": null }

---


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


### 2.1 获取投票活动列表（筛选 + 分页）
- **接口**：`GET /api/admin/votes`
- **请求头**：`Authorization: Bearer <adminToken>`
- **Query 参数**

请求参数
| 字段名           |       类型 | 必填 | 说明                                                          |
| ------------- | -------: | -: | ----------------------------------------------------------- |
| keyword       |   String |  否 | 活动标题或小区名模糊匹配                                                  |
| status        |   String |  否 | 活动状态枚举（`not_started/ongoing/finished`）not_started为还没开始的活动、ongoing/finished分别为正在进行的活动和结束的 |
| communityName |   String |  否 | 小区名关键字                                                      |
| startFrom     | DateTime |  否 | 起始时间下限（`YYYY-MM-DD HH:mm:ss`）                               |
| endTo         | DateTime |  否 | 结束时间上限                                                      |
| page          |  Integer |  否 | 默认 1                                                        |
| pageSize      |  Integer |  否 | 默认 10                                                       |




- **成功返回（示例）**
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "activityId": 1,
      "title": "小区绿化整改投票",
      "attachmentUrl": null,
      "startTime": "2025-09-01 00:00:00",
      "endTime": "2025-09-10 23:59:59",
      "isOfficial": 1,
      "voteScope": "幸福小区,3栋 1单元",
      "communityName": "幸福小区",
      "createdAt": "2025-08-25 09:00:00",
      "questionCount": 3,
      "totalVotes": 420,
      "status": "ongoing"
    },
    {
      "activityId": 2,
      "title": "小区绿化整改投票",
      "attachmentUrl": null,
      "startTime": "2025-09-01 00:00:00",
      "endTime": "2025-09-10 23:59:59",
      "isOfficial": 1,
      "voteScope": "幸福小区,3栋 1单元",
      "communityName": "幸福小区",
      "createdAt": "2025-08-25 09:00:00",
      "questionCount": 3,
      "totalVotes": 420,
      "status": "published"
    }
  ],
  "page_meta": { "page": 1, "pageSize": 10, "total": 2 }
}
```

- **错误返回（示例）**
```json
{ "code": 401, "message": "认证失败，请重新登录", "data": null }

```

### 2.2 获取投票活动详情议题投票详情
- **接口说明**
  点击任一投票活动，查看其详细的统计数据，包括图表所需数据。
- **接口**：`GET /api/admin/votes/{id}`
- **请求方式：GET
- **请求头**：`Authorization: Bearer <adminToken>`
- **请求参数类型**：Path
- **返回类型**：统一响应结构（Result）

#### Query 参数
| 字段名        | 类型   | 必填 | 说明 |
| ------------- | ------ | ---- | ---- |
| withStats     | Bool   | 否   | 是否返回统计数据（summary、charts）。默认 true |
| withQuestions | Bool   | 否   | 是否返回议题明细（questions）。默认 true |

请求参数

| 字段名 | 类型  | 必填 | 说明                           |
| --- | --- | -- | ---------------------------- |
| id  | Int | 是  | 投票活动ID（`vote_activities.id`） |

返回参数

| 接口字段          | 类型              | 映射/来源                            | 说明   |
| ------------- | --------------- | -------------------------------- | ---- |
| id            | Integer         | `vote_activities.id`             | 活动ID |
| title         | String          | `vote_activities.title`          | 标题   |
| attachmentUrl | String\|null    | `vote_activities.attachment_url` | 附件   |
| startTime     | DateTime        | `vote_activities.start_time`     | 开始   |
| endTime       | DateTime        | `vote_activities.end_time`       | 结束   |
| isOfficial    | Boolean\|Number | `vote_activities.is_official`    | 官方   |
| voteScope     | String\|Object  | `vote_activities.vote_scope`     | 直接拼接communityName，buildingNumber, unitNumber返回投票范围  |
| createdAt     | DateTime        | `vote_activities.created_at`     | 创建时间 |
| status        | String          | 状态机/时间计算 (投票的状态未开始、进行中、已结束、已取消) | 状态   |
| questionCount | Integer         | 聚合 `vote_questions`              | 议题数  |
| totalVotes    | Integer         | 聚合 `user_votes`                  | 总投票数 |

summary（仅当 withStats=true）
| 字段                | 类型      | 说明                |
| ----------------- | ------- | ----------------- |
| eligible          | Integer | 具备投票资格的住户数（范围内用户） |
| participants      | Integer | 参与投票的用户数（至少投过一题）  |
| participationRate | Number  | 参与率（四舍五入到2位）      |
| ballots           | Integer | 累计选票数（所有题合计）      |

questions（仅当 withQuestions=true，数组项结构）
| 接口字段         | 类型        | 映射/来源                          | 说明                           |
| ------------ | --------- | ------------------------------ | ---------------------------- |
| questionId   | Integer   | `vote_questions.id`            | 议题ID                         |
| questionText | String    | `vote_questions.question_text` | 议题内容                         |
| templateId   | Integer   | `vote_questions.template_id`   | 模板ID                         |
| createdAt    | DateTime  | `vote_questions.created_at`    | 创建时间                         |
| options      | Object\[] | 模板快照 × `user_votes` 聚合         | 选项统计（结构见下）                   |
| series       | Object    | 由 `options` 生成                 | 图表序列（`labels/counts/ratios`） |

questions[].options 数组项结构
| 字段    | 类型      | 说明                               |
| ----- | ------- | -------------------------------- |
| text  | String  | 选项文本（来源模板快照）                     |
| count | Integer | 该选项票数                            |
| ratio | Number  | 占比（`count / sum(options.count)`） |

questions[].series 结构
| 字段     | 类型         | 说明                          |
| ------ | ---------- | --------------------------- |
| labels | String\[]  | 选项文本数组（与 `options.text` 对齐） |
| counts | Integer\[] | 票数数组（与 `options.count` 对齐）  |
| ratios | Number\[]  | 占比数组（与 `options.ratio` 对齐）  |


- **成功返回**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 2,
    "title": "杭州市临平区小区加装电梯项目",
    "attachmentUrl": null,
    "startTime": "2025-06-25 00:00:00",
    "endTime": "2025-07-08 13:07:00",
    "isOfficial": 1,
    "voteScope": "乐茄小区",
    "createdAt": "2025-06-18 09:00:00",
    "status": "ongoing",
    "questionCount": 3,
    "totalVotes": 420,
    "summary": {
      "eligible": 320,
      "participants": 150,
      "participationRate": 0.47,
      "ballots": 420
    },
    "questions": [
      {
        "questionId": 1001,
        "questionText": "是否同意方案A？",
        "templateId": 10,
        "createdAt": "2025-06-18 09:10:00",
        "options": [
          { "text": "赞同", "count": 90, "ratio": 0.6 },
          { "text": "反对", "count": 50, "ratio": 0.333 },
          { "text": "弃权", "count": 10, "ratio": 0.067 }
        ],
        "series": {
          "labels": ["赞同","反对","弃权"],
          "counts": [90,50,10],
          "ratios": [0.6,0.333,0.067]
        }
      },
      {
        "questionId": 1002,
        "questionText": "是否同意方案B？",
        "templateId": 10,
        "createdAt": "2025-06-18 09:12:00",
        "options": [
          { "text": "赞同", "count": 80, "ratio": 0.571 },
          { "text": "反对", "count": 50, "ratio": 0.357 },
          { "text": "弃权", "count": 10, "ratio": 0.071 }
        ],
        "series": {
          "labels": ["赞同","反对","弃权"],
          "counts": [80,50,10],
          "ratios": [0.571,0.357,0.071]
        }
      }
    ]
  }
}

```

- **错误返回**
```json
{ "code": 401, "message": "未登录或登录已过期", "data": null }
{ "code": 403, "message": "无权限访问该活动", "data": null }
{ "code": 404, "message": "活动不存在或已被删除", "data": null }
{ "code": 500, "message": "服务器内部错误，获取详情失败", "data": null }
```


### 2.3 新增投票活动
- **接口**：`POST /api/admin/votes`
- **请求方式**：post
- **请求头**：`Authorization: Bearer <adminToken>`
- **请求参数类型**：Path
- **返回类型**：统一响应结构（Result）

请求参数
| 字段名           | 类型                | 必填 | 映射/来源                            | 说明                |
| ------------- | ----------------- | -- | -------------------------------- | ---------------------------- |
| title         | String            | 是  | `vote_activities.title`          | 活动标题                      |
| attachmentUrl | String \| null    | 否  | `vote_activities.attachment_url` | 附件URL                        |
| startTime     | DateTime          | 是  | `vote_activities.start_time`     | 开始时间（`YYYY-MM-DD HH:mm:ss`）  |
| endTime       | DateTime          | 是  | `vote_activities.end_time`       | 结束时间（需大于 `startTime`）        |
| isOfficial    | Boolean \| Number | 否  | `vote_activities.is_official`    | 是否官方（`true/false` 或 `1/0`）   |
| voteScope     | String \| Object  | 是  | `vote_activities.vote_scope`     | 投票范围，"ALL（全体业主）" 或votescope="PARTIAL"（部分业主）|
| communityName | String \| null    | 条件  |  `vote_activities.community_name`| 小区名必填，在哪个小区投票               |
| scopeItems   | Object[]          | 条件  |  `vote_activities.vote_scope`  | 当 voteScope="partial" 时,传栋号和单元号列表（可多栋多单元|

scopeItems 数组项结构（多栋/多单元）
| 字段名           | 类型                | 必填 | 映射/来源                            | 说明                |
| ------------- | ----------------- | -- | -------------------------------- | ---------------------------- |
| buildingNumber | String \| null   | 否  | `vote_activities.community_name` | 栋号，当voteScope = PARTIAL  必须填该字段  |
| unitNumber    | String[] \| null    | 否  |  `vote_activities.community_name`| 单元号，选填，但只有在已填 buildingNumber 时允许,可以多单元 |

"说明：新增活动仅创建“活动基本信息”。议题在后续接口“批量创建议题”里创建；发布前系统要求至少存在 1 个议题
voteScope === "ALL" → communityName必填
voteScope === "PARTIAL" → 展示“buildingNumber （必填），unitNumber（可选）注意顺序先栋号才有单元号,在前端均是下滑选择"

- **请求体（示例全体范围）**
```json
{
  "title": "小区绿化整改投票",
  "attachmentUrl": null,
  "startTime": "2025-09-01 00:00:00",
  "endTime": "2025-09-10 23:59:59",
  "isOfficial": 1,
  "voteScope": "ALL",
  "communityName": "幸福小区"
}
```
- **请求体（示例部分范围）**
```json
{
  "title": "加装电梯意向征集",
  "startTime": "2025-09-10 09:00:00",
  "endTime": "2025-09-15 18:00:00",
  "isOfficial": 1,
  "communityName": "乐茄小区",
  "voteScope": "partial",
  "scopeItems": [
    { "buildingNumber": "1栋"},
    { "buildingNumber": "2栋", "unitNumbers": ["2单元, 3单元"] }
  ]
}

```


- **成功返回**
```json
{ "code": 200, "message": "success", "data": { "id": 101 } }
```
- **失败返回**
```json
{ "code": 400, "message": "参数不合法（结束时间必须大于开始时间等）", "data": null }
{ "code": 409, "message": "同名活动或时间区间冲突", "data": null }
{ "code": 400, "message": "voteScope不合法：partial需提供scopeItems；all不得携带scopeItems", "data": null }

```

### 2.4 删除投票活动

- **接口**：DELETE /api/admin/votes/{id}
- **请求方式**：DELETE
- **请求头**：Authorization: Bearer <adminToken>
- **Path 参数**：id: Integer（vote_activities.id）
- **返回类型**：统一响应结构（Result）

"注意：若无投票记录（user_votes 不存在该活动的记录），允许物理删除：同时删除其下 vote_questions；

若已有投票记录，禁止物理删除，返回 409；前端应引导使用“修改活动状态”为 canceled（参见状态接口），以保留审计链路。"

- **成功返回**
```json
{ "code": 200, "message": "success", "data": null }
```

- **错误返回**
```json
{ "code": 404, "message": "活动不存在", "data": null }

```
```json
{ "code": 409, "message": "活动已有投票记录，禁止删除（请改为取消）", "data": null }
```


### 2.5 新增投票议题（单条）

- **接口**：'POST /api/admin/votes/{activityId}/questions'
- **请求方式**：POST
- **请求头**：Authorization: Bearer <adminToken>
- **返回类型**：统一响应结构（Result）

说明：在已创建的投票活动下新增一条议题。

请求参数

| 字段名           | 类型              |  必填 | 映射/来源                           | 说明                                                     |
| ------------- | --------------- | :-: | ------------------------------- | ------------------------------------------------------ |
| questionText  | String          |  是  | `vote_questions.question_text`  | 议题题面                                                   |
| sortOrder       | Integer         |  否  | `vote_questions.order_no`       | 排序号（越小越靠前；缺省由后端顺延）                                     |
| options       | String[]        |  条件 | 快照表（由后端写入）               | 传此字段表示直接采用自定义选项（≥2 个、不重复、非空） |
| startTime     | DateTime | null |  否  | `vote_questions.start_time`     | 议题开始时间（可选；**需落在活动时间窗内**）                               |
| endTime       | DateTime | null |  否  | `vote_questions.end_time`       | 议题截止时间（可选；**需落在活动时间窗内，且 `endTime` > `startTime`**）     |
| attachmentUrl | String | null   |  否  | `vote_questions.attachment_url` | 议题附件URL（可为空）                                           |

- **请求体示例**
```json
{
  "questionText": "对方案C的意见征集",
  "orderNo": 3,
  "options": ["支持", "中立", "反对", "弃权"],
  "startTime": "2025-06-26 00:00:00",
  "endTime":   "2025-06-29 23:59:59",
  "attachmentUrl": "https://cdn.xxx/q_c_attach.pdf"
}

- **请求体示例**
```json
{
  "questionText": "是否同意方案B？",
  "sortOrder": 2,
  "options": ["同意", "反对", "弃权"]
}
```


- **成功返回**
```json
{
  "code": 200,
  "message": "success",
  "data": { "activityId": 2, "questionId": 1005 }
}
```


- **错误返回**
```json
{ "code": 400, "message": "参数不合法：templateId 与 options 需二选一", "data": null }
{ "code": 400, "message": "options 非法：至少 2 个不重复的非空选项", "data": null }
{ "code": 400, "message": "议题时间需落在活动时间范围内，且结束晚于开始", "data": null }
{ "code": 404, "message": "活动不存在或已删除", "data": null }
{ "code": 409, "message": "活动状态不允许新增议题（已结束/已取消）", "data": null }

### 2.5 修改投票议题

- **接口**：PUT /api/admin/votes/questions/{questionId}
- **请求方式**：PUT（已有字段更新）
- **请求头**：Authorization: Bearer <adminToken>
- **Path 参数**：id: Integer（vote_activities.id）
- **返回类型**：统一响应结构（Result）

返回参数
无

请求参数：

| 字段名          | 类型               |  必填 | 映射/来源                          | 说明                                                    |
| ------------ | ---------------- | :-: | ------------------------------ | ----------------------------------------------------- |
| activityId   | Integer          |  是  | `vote_questions.activity_id`   | 活动ID（先定位活动）                                           |
| questionId   | Integer          |  是  | `vote_questions.id`            | 议题ID（再定位议题）                                           |
| questionText | String           |  否  | `vote_questions.question_text` | 题面（可改）                                                |
|  sortOrder     | Integer          |  否  | `vote_questions.order_no`      | 排序号（越小越靠前）                                            |
|   options    | String[]         |  是  |vote_question_options（快照）    |最终选项文本数组（以此整包覆盖原快照）|
| startTime    | DateTime \| null |  否  | `vote_questions.start_time`    | 议题开始时间（可选；**必须落在“活动时间窗”内**）                           |
| endTime      | DateTime \| null |  否  | `vote_questions.end_time`      | 议题截止时间（可选；**必须落在“活动时间窗”内，且 `endTime` > `startTime`**） |
| attachmentUrl | String\|null    |  否  |`vote_activities.attachment_url` | 活动附件                      |

注意:
"只可编辑议题，不可编辑总投票，只能删除总投票"
"修改限制（强制约束）
当该活动已产生投票数据（存在 user_votes 记录）或当前时间已进入/超过投票周期（状态为 ongoing/finished），禁止修改：startTime/endTime/voteScope/isOfficial/title；仅允许改 attachmentUrl。
当无投票记录且未到开始时间（状态 not_started 且 user_votes 计数为 0），可任意修改以上字段但仍需满足校验（如时间合法）。

若传 startTime/endTime：必须满足activity.start_time <= question.start_time < question.end_time <= activity.end_time"


- **请求体（示例）**
```json
{
  "questionText": "对方案B的意见征集",
  "sortOrder": 3,
  "options": ["支持", "中立", "反对", "弃权"],
  "startTime": "2025-06-26 00:00:00",
  "endTime":   "2025-06-29 23:59:59",
  "attachmentUrl":Null
}

```

- **成功返回**
```json
{ "code": 200, "message": "success", "data": null}
```

- **错误返回**
```json
{ "code": 400, "message": "参数不合法（结束时间需大于开始时间）", "data": null }
{ "code": 403, "message": "活动已开始或已有投票，禁止修改关键字段", "data": null }
{ "code": 404, "message": "活动不存在", "data": null }
```

### 2.6 修改投票议题

- **接口**：PUT /api/admin/votes/questions/{questionId}
- **请求方式**：PUT（已有字段更新）
- **请求头**：Authorization: Bearer <adminToken>
- **Path 参数**：id: Integer（vote_activities.id）
- **返回类型**：统一响应结构（Result）

返回参数
无

请求参数：

| 字段名          | 类型               |  必填 | 映射/来源                          | 说明                                                    |
| ------------ | ---------------- | :-: | ------------------------------ | ----------------------------------------------------- |
| activityId   | Integer          |  是  | `vote_questions.activity_id`   | 活动ID（先定位活动）                                           |
| questionId   | Integer          |  是  | `vote_questions.id`            | 议题ID（再定位议题）                                           |
| questionText | String           |  否  | `vote_questions.question_text` | 题面（可改）                                                |
|  sortOrder     | Integer          |  否  | `vote_questions.order_no`      | 排序号（越小越靠前）                                            |
|   options    | String[]         |  是  |vote_question_options（快照）    |最终选项文本数组（以此整包覆盖原快照）|
| startTime    | DateTime \| null |  否  | `vote_questions.start_time`    | 议题开始时间（可选；**必须落在“活动时间窗”内**）                           |
| endTime      | DateTime \| null |  否  | `vote_questions.end_time`      | 议题截止时间（可选；**必须落在“活动时间窗”内，且 `endTime` > `startTime`**） |
| attachmentUrl | String\|null    |  否  |`vote_activities.attachment_url` | 活动附件                      |

注意:
"只可编辑议题，不可编辑总投票，只能删除总投票"
"修改限制（强制约束）
当该活动已产生投票数据（存在 user_votes 记录）或当前时间已进入/超过投票周期（状态为 ongoing/finished），禁止修改：startTime/endTime/voteScope/isOfficial/title；仅允许改 attachmentUrl。
当无投票记录且未到开始时间（状态 not_started 且 user_votes 计数为 0），可任意修改以上字段但仍需满足校验（如时间合法）。

若传 startTime/endTime：必须满足activity.start_time <= question.start_time < question.end_time <= activity.end_time"


- **请求体（示例）**
```json
{
  "questionText": "对方案B的意见征集",
  "sortOrder": 3,
  "options": ["支持", "中立", "反对", "弃权"],
  "startTime": "2025-06-26 00:00:00",
  "endTime":   "2025-06-29 23:59:59",
  "attachmentUrl":Null
}

```

- **成功返回**
```json
{ "code": 200, "message": "success", "data": null}
```

- **错误返回**
```json
{ "code": 400, "message": "参数不合法（结束时间需大于开始时间）", "data": null }
{ "code": 403, "message": "活动已开始或已有投票，禁止修改关键字段", "data": null }
{ "code": 404, "message": "活动不存在", "data": null }
```

## 2.7 删除投票议题
- **接口**：DELETE /api/admin/votes/{activityId}/questions/{questionId}
- **请求方式**：DELETE
- **请求头**：Authorization: Bearer <adminToken>
- **返回类型**：统一响应结构（Result）

请求参数
| 字段名        | 类型      |  必填 | 映射/来源                        | 说明                |
| ---------- | ------- | :-: | ---------------------------- | ----------------- |
| activityId | Integer |  是  | `vote_questions.activity_id` | 活动ID（校验议题确实属于该活动） |
| questionId | Integer |  是  | `vote_questions.id`          | 删除的议题ID              |

返回参数
无


- **成功返回**
```json
{ "code": 200, "message": "success", "data": null }
```

- **错误返回**
```json
{ "code": 404, "message": "活动不存在", "data": null }
{ "code": 409, "message": "活动已有投票记录，禁止删除（请改为取消）", "data": null }

