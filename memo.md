# spring-batch playground

- JobRepository
  - バッチの実行結果を保存してくれる機能
  - バッチに渡したパラメータ、件数、ステータスなどを保存
- JobLauncher
  - バッチを実行してくれる機能
- Job
  - バッチ処理全体、複数の Step で構成される
- Step
  - バッチ処理の最小単位

- Step には2つのモデルがある
  - Chunk
    - データの読み込み、加工、書き込みなどの定型処理を作るときに使用
    - ItemReader,ItemProcessor,ItemWriter の Interface が提供されている
  - Tasklet
    - Chunk では作れない機能を実装するときに使用

- Listener(even listener)
  - 何かしらをきっかけに起動する処理のこと
  - Webアプリケーションと違い画面がないためログ出力が肝心
