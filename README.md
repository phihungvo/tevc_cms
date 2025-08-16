TEVC CMS là hệ thống quản lý được xây dựng bằng Spring Boot và ReactJS, bao gồm hai thành phần chính:
1. API (tevc_cms_api)
Cung cấp các RESTful API để quản lý người dùng, phân quyền, bài viết và các chức năng CMS khác. Công nghệ: Spring Boot, JPA, Hibernate, Maven.

2. APP (tevc_cms_app)
Mục đích: Cung cấp giao diện người dùng (frontend) để tương tác với hệ thống CMS thông qua các API. Công nghệ: ReactJS

Format code with Spotless:

```sql
mvn spotless:apply
```
or
```sql
mvn spotless:check
```