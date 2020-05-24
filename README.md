# 業務日誌　ウェブサービス

## セットアップ方法
1. データベースをインストールしてください。以下にMySQLの例を記載します。
2. データベースとユーザを作成  
データベース名と、ユーザは以下の例で記載します。  

| 項目 | 設定 |
| :-- | :-- |
| データベース名 | jobsheetdb |
| ユーザ名 | jobsheet |
| パスワード | jobsheet |

データベース作成
```
mysql> create database jobsheetdb;
```
ユーザ作成
```
mysql> create user 'jobsheet'@'%' identified by 'jobsheet';
```
ユーザに権限追加
```
mysql> grant all on jobsheetdb.* to 'jobsheet'@'%';
```

3. Javaとアプリケーションサーバを設定  
Javaのバージョンは11  
アプリケーションサーバはPayara5

4. JDBCドライバを配置  
Payara5に、JDBCドライバを配置します。  
C:\serverにPayaraを配置した場合は、  
C:\server\payara-5.193\payara5\glassfish\domains\domain1\lib\mysql-connector-java-5.1.47-bin.jar  
のようにしてください。

5. Netbeansをダウンロード  
開発ではNetbeansを使用します。

6. NetbeansでPayara設定  
Netbeansの「services」タブのServersを右クリックして、「Add Server...」をクリックし、  
Payaraを追加します。

7. コネクションプールを作成する。  
6で追加したPayaraを右クリックしてStartする。  
起動したら、Payaraを右クリックしてView Domain Admin Consoleをクリックして  
管理画面を起動する。  
Resources→JDBC→JDBC Connction Pools でコネクションプールを作成する。

プロパティには以下の項目を設定  
| Name | Value |
| :-- | :-- |
| password | jobsheet |
| databaseName | jobsheetdb |
| serverName | localhost |
| user | jobsheet |
| portNumber | 3306 |
| driverClass | com.mysql.jdbc.Driver |
| URL | jdbc:mysql://localhost:3306/jobsheetdb?zeroDateTimeBehavior=convertToNull&useSSL=false|

8. JDBCリソースを作成  
Payara管理画面のResources→JDBC→JDBC Resourcesで作成します。  
PoolNameには、7で作成したコネクションプールを選択してください。

9. Netbeansにソースをインポート  
Netbeansの「Projects」タブで、右クリック→Open Project...をクリックし、  
ダウンロードしたソースを選択します。

10. プロジェクト設定  
プロジェクトを右クリックし、Propatiesで、使用するJavaとPayaraを指定します。

11. データソース設定  
プロジェクトのsrc/conf/persistence.xmlのデータソース名を  
8で作成したJDBCリソース名にしてください。
```
<jta-data-source>jdbc/jobsheetdb</jta-data-source>
```
12. ビルド、デプロイ  
Netbeansでプロジェクトを右クリックし、Buildおよび、Deployをしてください。  
Deployをすると、データベースに必要なテーブルが自動的に作成されます。

以上でセットアップは完了です。
