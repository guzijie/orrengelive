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

- **接口名称**：管理端获取
- **请求方式**：GET
- **请求路径**：`/api/user/vote `
- **请求参数类型**：JSON
- **返回类型**：统一响应结构（Result）


| 字段名           | 类型      | 必填 | 说明         |
|------------------|-----------|------|--------------|
| activity_name        | String    | 是   | 活动名称       |
| topics_1        | String    | 是   | 议题一         |
| topics_2          | String    | 否   | 议题二   |
| topics_3             | String    | 否   | 议题三         |
| begin_time          | String    | 是   | 开始时间  |
| end_time           | String    | 是   | 结束时间         |_
| official        | bool      | 是   | 官方投票    |
| community_name        | String    | 是   | 小区名字         |
| vote_num              | int       | 是    |   票数
| vote_scope          | String    | 是   | 已投票范围         |

请求参数

无

可能的错误返回
```json
{
  "code": 400,
  "message": "目前暂无投票",
  "data": null
}

```
可能的正确返回
```json
{
"code": 200,
"message": "success",
"data": {
"phone": 123456,
"title": "小区绿化方案投票",
"description": "请选择您支持的绿化方案",
"status": "进行中",
"endTime": "2025-07-20 23:59:59"
}
}
```
```json
{
"code": 200,
"message": "success",
"data": {
"activityName": "小区绿化方案投票",
"topics1": "是否增加绿化面积",
"topics2": "是否修建电梯",
"topics3": "无",
"beginTime": "2025-05-20 23:59:59",
"endTime": "2025-07-20 23:59:59",
"official": "1",
"communityName": "蓝天小区 白云小区",
"voteNum": "110",
"voteScope": ""
}
}
```


### (2) 用户点击投票“同意按钮”

- **接口名称**：管理端获取
- **请求方式**：POST
- **请求路径**：`/api/user/voteAgree `
- **请求参数类型**：JSON
- **返回类型**：统一响应结构（Result）


| 字段名           | 类型      | 必填 | 说明         |
|------------------|-----------|------|--------------|
| vote_num        | int    | 是   | 点击同意票数加一      |


请求参数

无

可能的错误返回
```json
{
  "code": 400,
  "message": "目前无该投票信息",
  "data": null
}

```
可能的正确返回
```json
{
"code": 200,
"message": "success",
"data": {
"vote_num": 1
}
}
```