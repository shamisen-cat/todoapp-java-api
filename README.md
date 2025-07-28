# TodoApp: Spring Boot REST API Sample

Spring BootによるシンプルなToDo管理API  
ETagによる楽観的ロックをサポートし、ページング対応のREST APIを提供

## 特徴

- ToDoのCRUD操作
- ETagによる更新競合防止
- ページング対応の一覧取得
- H2（ローカル開発用）とPostgreSQL（Raspberry Pi OS上）で動作確認済み

## 動作環境

- Java 17以上
- Spring Boot 3.5
- Gradle 8.14

### 動作確認済みの環境

| 用途       | DB                 | OS                           |
|------------|--------------------|------------------------------|
| 開発環境   | H2 2.3             | macOS                        |
| 本番想定   | PostgreSQL 16.9    | Raspberry Pi OS Lite (64bit) |

## ビルドと起動

```bash
./gradlew bootJar
java -jar build/libs/todoapp-0.0.1-SNAPSHOT.jar
```

## API例

### 一覧取得（GET）

```bash
curl http://localhost:8080/api/todos
```

### 取得（GET）

```bash
curl http://localhost:8080/api/todos/{id}
```

### 作成（POST）

```bash
curl -X POST 'http://localhost:8080/api/todos' \
    -H 'Content-Type: application/json' \
    -d '{"title":"新規作成"}'
```

### 更新（PUT）

```bash
curl -X PUT 'http://localhost:8080/api/todos/{id}' \
    -H 'Content-Type: application/json' \
    -H 'If-Match: "ETag"' \
    -d '{"title":"更新","completed":true}'
```

### 削除（DELETE）

```bash
curl -X DELETE 'http://localhost:8080/api/todos/{id}' \
    -H 'If-Match: "ETag"'
```

## 注意事項

- ETagは楽観的ロックに使用します。
- 一覧取得APIのレスポンスでは、各ToDoの `eTag` フィールドに含まれます。
- 単一取得APIのレスポンスでは、HTTPレスポンスヘッダの `ETag` に含まれます。
- 更新・削除APIのリクエスト時は、 `If-Match` ヘッダでETagを指定してください。
- 本プロジェクトは学習を目的とした個人開発のため、セキュリティ・可用性の検討は限定的です。
- 実運用の際は十分ご注意ください。
