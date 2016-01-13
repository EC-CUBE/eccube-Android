# eccube-Android

Sample app for EC-CUBE hosted online shops.  
Easily customized & ulilized for the owned shops.  
Also, implements Push Notification receiver to fully  
maximize the potential of mobile shoppers.

## 目次

- [このアプリについて](#このアプリについて)
  - [概要](#概要)
  - [使い方](#使い方)
- [アプリをカスタマイズする](#アプリをカスタマイズする)
  - [パッケージ名を変更する](#パッケージ名を変更する)
  - [ウェブサイトを設定する](#ウェブサイトを設定する)
  - [スプラッシュ画像を変更する](#スプラッシュ画像を変更する)
  - [アイコン画像を変更する](#アイコン画像を変更する)
  - [メニュー画像を変更する](#メニュー画像を変更する)
  - [プッシュ受信の設定を変更する](#プッシュ受信の設定を変更する)
  - [keystoreを設定する](#keystoreを設定する)


## このアプリについて

### 概要

EC-CUBE3系で運営するショップを Android のアプリとして連携することができます

プッシュ通知を受信するための例として [「Appiaries プッシュ通知プラグイン」](http://www.ec-cube.net/products/detail.php?product_id=1030) を用いた仕組みを実装していますが、それ以外のサービスを利用することも可能です。

### 使い方

Android Studio のプロジェクトになっています。 
git clone でローカルにソースコードを展開して下さい。

## アプリをカスタマイズする

カスタマイズは、アプリで具現化したい内容によって異なりますが、ここではカスタマイズする上で **いちばん基本的な箇所についてその主要なポイントと注意点** を説明します。

### パッケージ名を変更する

パッケージ名（アプリ ID）の変更は Android Studio に一般的な方法でおこなわれますが、以下に作業の流れを要約します。

1. Android Studio で本アプリを開きます。
2. プロジェクトエクスプローラーを開きます。
3. app ディレクトリを右クリックし、Open Module Settings を選択します。
4. Module Settings 画面に表示されている Flavors タブを開きます。
5. Flavors タブ内の Application ID の項目が net.ec_cube.eccube_Android となっている部分を新しいパッケージ名に変更します。
6. Android Studio を閉じます。
7. 物理的なプロジェクトのディレクトリである eccube-Android のディレクトリ名を希望するプロジェクト名に変更します。
8. プロジェクト直下にある eccube-Android.iml を削除します。
9. プロジェクト直下にある .name ファイルに eccube-Android という記述を変更します。
10. プロジェクト直下にある .idea ディレクトリの下の module.xml と workspace.xml の中において eccube-Android と記述されている部分をすべて変更します。
11. app/src/main 直下にある AndroidManifest.xml のなかで net.ec_cube.eccube_Android となっている部分をすべて新しいパッケージ名に変更します。
12. app/src/main/java の下の net/ec_cube/eccube_Android という物理的なディレクトリ構成を、新しいパッケージ名のディレクトリ構成に合わせて変更します。
13. またその中のすべての *.java ファイルに記載されているパッケージ名 net.ec_cube.eccube_Android をすべて新しいパッケージ名に変更します。
14. Android Studio を起動します。
15. プロジェクト直下に [新しいパッケージ名].iml ファイルが生成されることを確認します。

### 運営しているEC-CUBEのショップのURLを設定する

アプリと連携するショップの URL を設定します。

1. net/ec_cube/eccube_Android/Config.java を開きます。
2. BASE_URL にすでに設定されている www.example.net をショップの URL に書き換えます。


### スプラッシュ画像を変更する

スプラッシュ画像は res/drawable-xhdpi/logo.png に置いてあります。  
必要であれば、解像度にあわせた画像をそれぞれの drawable に配置してください。  
またファイル名を変更する場合は res/layout/activity_splash.xml の記述を変更してください。

### アイコン画像を変更する

アイコン画像は res/mipmap-xxhdpi/ic_launcher.png に置いてあります。  
必要であれば、解像度にあわせた画像を配置してください。  
ファイル名を変更する場合は AndroidManifest.xml も変更して下さい。

### メニュー画像を変更する

メニュー画像は res/drawable-xxhdpi, res/drawable-xxhdpi, res/drawable-xxhdpi に置いてあります。  
ファイル名を変更する場合は res/layout/activity_main.xml の記述を変更して下さい。

### プッシュ受信の設定を変更する

運営しているEC-CUBE3系のショップに、[「Appiaries プッシュ通知プラグイン」](http://www.ec-cube.net/products/detail.php?product_id=1030) を利用することで、簡単にプッシュ通知に対応することができます。

* アピアリーズとの連携の設定が別途必要です
* プッシュ通知の配信や受信は他のサービスを利用して実装することも可能です。

GCM とアピアリーズに登録し、それぞれの情報をプラグインに設定する方法については[「Appiaries プッシュ通知プラグイン」](http://www.ec-cube.net/products/detail.php?product_id=1030) をインストールした後でプラグインの「設定」ページに詳細がありますのでそちらを参照して下さい。  

プラグインの設定の完了後、SDK をプロジェクトに追加し、コメントアウトを外し、必要なパラメータを設定する必要があります。

1. Appiaries 公式ウェブサイトから Android SDK を [ダウンロード](http://docs.appiaries.com/?p=14066) します。
2. ダウンロードしたファイルを解凍し、プロジェクトエクスプローラー内で app/libs フォルダにコピーします。
3. ソースコード OpenMessageService.java および PushRegistrationFragment.java にて「Appiaries プッシュ通知受信機能を使う場合はコメントアウトを外して下さい」と記載された箇所のコメントアウトを外します。
4. アピアリーズとの連携に必要なパラメータを設定するため、ソースコード Config.java を開きます。
5. GCM の Developer Console に表示される「Project Number」を *SENDER_ID* に設定します。
6. アピアリーズで契約した「データストア ID」を *DATASTORE_ID* に設定します。
7. アピアリーズで登録した「アプリ ID」を *APPLICATION_ID* に設定します。
8. アピアリーズで登録したアプリの「アプリトークン」を *APPLICATION_TOKEN* に設定します。

### keystoreを設定する

プロジェクトをビルドするには keystore が必要です。keystore は GCM 側で設定したものを使用します。keystore の作成については一般的な方法でおこなって下さい。以下は、すでに keystore が作成されているものとし、keystore を本アプリに設定するための手順の一つの例です。

1. プロジェクト直下に keystore ディレクトリを作成し（ディレクトリ名は任意です）、keystore ファイルを置きます。
2. Android Studio を起動し、アプリを開きます。
3. プロジェクトエクスプローラーを開きます。
4. app フォルダを右クリックし、Open Module Settings を選択します。
5. Module Settings 画面に表示されている Flavors タブを開きます。
6. Signing タブ内の**「Key Alias」**、**「Key Password」**、**「Store File」**、**「Store Password」**をそれぞれ設定します。