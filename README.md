# 博友圈网站源码

## 博友圈介绍

博友圈（[www.boyouquan.com](https://www.boyouquan.com)）成立于 2023 年，是博客人的专属朋友圈，提供博客收录、文章聚合展示、星球穿梭等功能。

若想了解博友圈的建站初衷、主要功能和技术架构详情，请站长的「[一篇博客文章](https://leileiluoluo.com/posts/boyouquan-introduction.html)」。

## 工程介绍

博友圈应用程序主要使用 Java 语言编写，是一个集前后台于一体的单体服务，使用了 Spring Boot + Thymeleaf + MyBatis
技术。其中，Spring Boot 负责请求处理和依赖注入，Thymeleaf 负责模板渲染，MyBatis 负责数据库访问。此外，应用程序的持久化数据存储使用的是
MariaDB。

## 应用程序架构

博友圈应用程序架构如下图所示，自上而下使用了经典的三层架构：即控制器层（Controller Layer）、业务逻辑层（Service
Layer）、数据访问层（DAO Layer）。

![博友圈应用程序架构](https://leileiluoluo.com/static/images/uploads/2024/04/boyouquan-application-architecture.svg#center)

- 控制器层包含一组 SpringMVC 控制器，负责请求的接收、参数校验、服务调用和结果的返回；
- 业务逻辑逻辑层包含一组服务，负责核心业务逻辑处理；
- 数据访问层包含包含一组 Mabatis 接口，负责与数据库的交互。

此外，附加的调度器层（Scheduler Layer）和帮手层（Helper Layer）则分别包含了一组定时任务和辅助工具类。

## 程序设置与启动

### 依赖项

本工程依赖的 Java 与 MariaDB 版本如下：

- Java 17+
- MariaDB 10.5+

### 数据库

本工程已将所有用到的 MySQL 建库与建表语句置于 `sql/ddl` 目录下。

首先，在您本地安装的 MariaDB 上执行如下建库语句：

[./sql/ddl/database.sql](./sql/ddl/database.sql)

然后，在建好的数据库上执行 [./sql/ddl/](./sql/ddl/) 目录下除 `database.sql` 之外的所有 SQL 文件中的语句。

### 编译与运行
