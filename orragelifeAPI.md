# 管理员注册接口文档

## 1. 管理员注册接口基本信息

注册获取验证码

- **接口名称**：管理端获取
- **请求方式**：POST
- **请求路径**：`/api/admin/verification`
- **请求参数类型**：JSON
- **返回类型**：统一响应结构（Result）

请求参数

| 字段名           | 类型      | 必填 | 说明         |
|------------------|-----------|------|--------------|
| phone            | String    | 是   | 手机号       |
| password         | String    | 是   | 密码         |
| verification           | String    | 是   | 验证码   |


**示例请求体：**
```json
{
  "phone": "13800000000",
  "password": "123456"
}
```
## 4. 可能的错误返回
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


### 2. 注册
- **接口名称**：用户注册
- **请求方式**：POST
- **请求路径**：`/api/admin/register`
- **请求参数类型**：JSON
- **返回类型**：统一响应结构（Result）

---



**示例请求体：**
```json
{
  "phone": "13800000000",
  "password": "123456",
  "verification":"24565"
}
```

---

返回参数

- **code**：状态码（200为成功）
- **message**：提示信息
- **data**：用户信息（UserVO）

**示例返回体：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "phone": "13800000000"
  }
}
```

---

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

# 用户接口
## 1. 用户获取投票信息接口

 请求参数

| 字段名           | 类型      | 必填 | 说明         |
|------------------|-----------|------|--------------|
| phone            | String    | 是   | 手机号       |
| password         | String    | 是   | 密码         |
| idCard           | String    | 是   | 身份证号码   |
| name             | String    | 是   | 姓名         |
| avatar           | String    | 否   | 头像图片URL  |
| gender           | String    | 否   | 性别         |
| birthday         | Date      | 否   | 出生日期     |
| education        | String    | 否   | 学历         |
| politicalStatus  | String    | 否   | 政治面貌     |
| address          | String    | 否   | 地址         |
| validFrom        | Date      | 否   | 有效期开始   |
| validTo          | Date      | 否   | 有效期结束   |
| organization     | String    | 否   | 组织         |
| position         | String    | 否   | 岗位         |




## 1. 用户注册接口基本信息

### 1. 获取投票信息

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


## 用户点击投票“同意按钮”

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

