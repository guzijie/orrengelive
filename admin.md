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
数据库对应数据表activity

| 字段名          | 类型       | 必填 | 说明                     |
|------------------|------------|------|--------------------------|
| id               | bigint(20) | 是   | 主键ID                   |
| activity_name    | varchar(100) | 是   | 活动名称                 |
| topics_1         | varchar(200) | 是   | 议题一                   |
| topics_2         | varchar(200) | 否   | 议题二                   |
| topics_3         | varchar(200) | 否   | 议题三                   |
| begin_time       | datetime   | 是   | 开始时间                 |
| end_time         | datetime   | 是   | 结束时间                 |
| official         | tinyint(1) | 是   | 是否官方投票：0-否，1-是 |
| community_name   | varchar(100) | 是   | 小区名称                 |
| vote_num         | int(11)    | 是   | 票数                     |
| vote_scope       | varchar(200) | 是   | 已设置投票范围           |
| create_time      | timestamp  | 是   | 创建时间                 |
| update_time      | timestamp  | 是   | 更新时间                 |

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
| keyword | String | 否  | 搜索关键词，可匹配小区名或投票主题                |
| status  | String | 否  | 投票状态 (例如: "ongoing", "finished") |

正确返回示例

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": "vote_001",
      "communityName": "乐茄小区",
      "title": "杭州市临平区商品房性质老旧小区住宅加装电梯项目",
      "startTime": "2025/06/25 00:00",
      "endTime": "2025/07/08 13:07",
      "status": "已结束"
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
| id  | String | 是  | 投票活动ID |

正确返回示例

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "title": "杭州市临平区商品房性质老旧小区住宅加装电梯项目",
    "totalVoters": 321,
    "totalArea": "31256平方米",
    "issues": [
      {
        "issueTitle": "议题1 您对小区住宅加装电梯项目持什么态度?",
        "results": [
          {
            "option": "支持",
            "voteCount": 236,
            "percentageByPeople": "44.36%",
            "areaSum": "23766.58",
            "percentageByArea": "44.25%"
          }
        ]
      }
    ]
  }
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
  "code": 404,
  "message": "投票活动未找到",
  "data": null
}
```

```json
{
  "code": 500,
  "message": "服务器内部错误，获取详情失败",
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
| ------------- | --------------- | -- | ------------------------------------ |
| communityName | String          | 是  | 投票项目/小区名称                            |
| title         | String          | 是  | 投票主题                                 |
| voteScope     | Object          | 是  | 投票范围，用于筛选参与投票的用户。详见下方 voteScope 对象结构 |
| startTime     | DateTime String | 是  | 开始时间 (格式: "YYYY-MM-DD HH\:mm\:ss")   |
| endTime       | DateTime String | 是  | 结束时间 (格式: "YYYY-MM-DD HH\:mm\:ss")   |
| issues        | Array[Issue]    | 是  | 议题列表，详见下方 Issue 对象结构                 |
| attachmentUrl | String          | 否  | 附件的 URL 地址                           |

voteScope 对象结构

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
  "message": "请求参数不完整或格式错误，请检查必填项",
  "data": null
}
```

```json
{
  "code": 400,
  "message": "结束时间不能早于或等于开始时间",
  "data": null
}
```

```json
{
  "code": 400,
  "message": "至少需要一个投票议题",
  "data": null
}
```

```json
{
  "code": 400,
  "message": "自定义选项不能为空且最多10个",
  "data": null
}
```

```json
{
  "code": 400,
  "message": "选择部分业主时，必须指定楼幢和单元",
  "data": null
}
```

```json
{
  "code": 401,
  "message": "认证失败，请重新登录",
  "data": null
}
```

```json
{
  "code": 403,
  "message": "无权进行此操作",
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



