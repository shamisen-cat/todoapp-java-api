# TodoApp: Spring Boot REST API Sample

Spring BootによるシンプルなTo-do管理API  
ETagの楽観的ロックとページング機能に対応したREST APIを提供

## 特徴

- To-doのCRUD操作
- ETagによる更新の競合防止
- ページング対応の一覧取得

## 動作環境

- Java 17以上
- Spring Boot 3.5
- Gradle 8.14

### 動作確認済みの環境

| 環境     | OS                           | DB              |
|----------|------------------------------|-----------------|
| 開発     | macOS                        | H2 2.3          |
| 本番想定 | Raspberry Pi OS Lite (64bit) | PostgreSQL 16.9 |

## フォルダ構成

```text
todoapp
├── todo
│   ├── model
│   ├── dto
│   ├── repository
│   ├── service
│   │   ├── finder
│   │   ├── query
│   │   ├── command
│   │   ├── factory
│   │   └── mapper
│   ├── controller
│   │   ├── query
│   │   └── command
│   ├── exception
│   └── handler
├── etag
│   ├── dto
│   ├── validation
│   ├── factory
│   ├── exception
│   └── handler
├── common
│   ├── audit
│   ├── validation
│   └── error
│       ├── builder
│       └── handler
├── config
└── TodoappApplication.java
```

## ビルドと起動

```bash
./gradlew bootJar
java -jar build/libs/todoapp-0.0.1-SNAPSHOT.jar
```

## API例

### 一覧取得（GET）

```bash
curl http://localhost:8080/api/todos | jq
```

#### レスポンス構造（一覧取得）

```text
{
  "content": [                 // 配列
    {
      "body": {
        "id": string,          // ID
        "title": string,       // タイトル
        "completed": boolean,  // 完了状態
        "createdAt": string,   // 作成日時（ISO8601形式）
        "updatedAt": string    // 更新日時（ISO8601形式）
      },
      "eTag": string           // ETag
    },
    ...
  ],
  "pageable": {                // ページ情報
    "pageNumber": number,      // ページ番号
    "pageSize": number         // 表示件数
  },
  "totalPages": number,        // 総ページ数
  "totalElements": number,     // 総件数
  "number": number,            // ページ番号
  "size": number,              // 表示件数
  "first": boolean,            // 最初のページ判定
  "last": boolean,             // 最後のページ判定
  "numberOfElements": number,  // 現在のページの件数
  "empty": boolean             // 現在のページの空判定
}
```

### 取得（GET）

```bash
curl http://localhost:8080/api/todos/{id} | jq
```

#### レスポンス構造（単体取得）

ETagはHTTPレスポンスの `ETag` ヘッダから取得してください。

```text
{
  "id": string,          // ID
  "title": string,       // タイトル
  "completed": boolean,  // 完了状態
  "createdAt": string,   // 作成日時（ISO8601形式）
  "updatedAt": string    // 更新日時（ISO8601形式）
}
```

### 作成（POST）

```bash
curl -X POST 'http://localhost:8080/api/todos' \
    -H 'Content-Type: application/json' \
    -d '{"title":"作成"}'
```

### 更新（PUT）

取得したETagを `If-Match` ヘッダに設定してください。

```bash
curl -X PUT 'http://localhost:8080/api/todos/{id}' \
    -H 'Content-Type: application/json' \
    -H 'If-Match: "ETag"' \
    -d '{"title":"更新","completed":true}'
```

### 削除（DELETE）

取得したETagを `If-Match` ヘッダに設定してください。

```bash
curl -X DELETE 'http://localhost:8080/api/todos/{id}' \
    -H 'If-Match: "ETag"'
```

## 注意事項

- ETagは楽観的ロックのために使用します。
- 本プロジェクトは学習を目的とした個人開発です。
- 実運用を前提とする場合には、十分な設計・検証を行なってください。
