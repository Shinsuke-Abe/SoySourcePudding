# テキスト学習

## input

コーパスファイル名(ディレクトリは/opt/learning/input固定)

## output

形態素ファイル(/opt/learning/morpheme/)
学習モデル(/opt/learning/model/model-wordvectors.txt固定)

## Docker

dockerで実行する際は `/opt/learning` に必要なディレクトリをマウントしてください。

Webアプリケーション側でも同ディレクトリをマウントしておけば連携が楽です。

/opt/learning/input ... コーパスファイル置き場
/opt/learning/dic ... ユーザ辞書ファイル配置ディレクトリ
/opt/learning/model ... モデル出力ディレクトリ
/opt/learning/morpheme ... コーパスから分析された形態素ファイル

### ビルド

```
## プロジェクトルートからのパス
gradle :learning.backend:distTar
cp ./learning.backend/docker/Dockerfile ./learning.backend/build/distributions/

## カレントをdistributionに
cd ./learning.backend/build/distributions/
docker build -t soysourcepudding/learning:latest ./
```

### 実行

`docker run --rm -t -i -v [マウントするアプリケーション用ファイル名]:/opt/learning/ soysourcepudding/learning [コーパスファイル名]`
