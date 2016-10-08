# テキスト学習

## input

コーパスファイル

## output

形態素ファイル
学習モデル

## Docker

dockerで実行する際は `/opt/learning` に出力先をマウントしてください。

Webアプリケーション側でも同ディレクトリをマウントしておけば連携が楽です。

/opt/learning/dic ... ユーザ辞書ファイル配置ディレクトリ
/opt/learning/model ... モデル出力ディレクトリ
/opt/learning/morpheme ... コーパスから分析された形態素ファイル
