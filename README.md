# Ant Server

Ant Server 是一个基于 Java NIO 开发的多线程 HTTP 服务器，支持基本的 HTTP/1.1 协议。

## Ant Server 支持特性

- [x] 多线程处理
- [x] HTTP请求
    - [x] POST
    - [x] GET
    - [ ] OPTIONS
- [x] HTTP响应
    - [x] 200
    - [ ] 300
    - [ ] 301
    - [x] 302
    - [ ] 401
    - [ ] 403
    - [x] 404
- [x] Cookies
- [ ] Session
- [ ] HTTP访问控制（CORS）
- [ ] HTTP 身份验证
- [x] 静态资源
- [ ] 通过注解配置服务器
- [x] 通过XML配置服务器

### 应用开发

- [x] HttpServlet
    - [x] Cookies
    - [ ] Session
    - [x] File upload
    - [x] Forward
    - [x] sendRedirect
    - [ ] Async context
    - [ ] WebSocket
- [ ] HttpFilter
- [ ] EventListener
    - [ ] ServletContextListener
    - [ ] ServletContextAttributeListener
    - [ ] ServletRequestListener
    - [ ] ServletRequestAttributeListener
    - [ ] HttpSessionAttributeListener
    - [ ] HttpSessionIdListener
    - [ ] HttpSessionListener

## 即将支持特性（0.20）

+ 服务端响应包支持多格式传输
+ 支持cookie，session
+ 支持filter
+ 修复多线程并发问题
+ 为代码添加比较详细的注释

