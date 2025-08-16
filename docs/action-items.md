# Action Items

## トランザクションの実装

- サービス層に`@Transactional`アノテーションを適用

## 認証機能の実装

- `Spring Security`を導入し、認証機能を実装
- 認証失敗時には`401 Unauthorized`を返却
- common.securityパッケージに実装を予定

## 更新リクエストの最適化

- 更新時に差分比較を行い、変更がない場合にはDBアクセスをスキップ
- 変更のない更新リクエストには、フロントエンドへ警告を返却

## `TodoService#getTodoPageByUpdatedAt` の修正

- ページ番号が総ページ数を超える場合に、空のページデータを返却
