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
- [お問い合わせ](#お問い合わせ)


## このアプリについて

### 概要

EC-CUBE で運営する店舗を Android のアプリで利用するためのサンプルアプリです。  
このサンプルアプリはプッシュ通知を受信するための例として [「Appiaries プッシュ通知プラグイン」](http://www.ec-cube.net/products/detail.php?product_id=) を用いた仕組みを実装していますが、それ以外のサービスを利用することも可能です。

### 使い方

Android Studio のプロジェクトになっています。  
git clone でローカルにソースコードを展開して下さい。

## アプリをカスタマイズする

カスタマイズは、アプリで具現化したい内容によって異なりますが、ここではカスタマイズする上で **いちばん基本的な箇所についてその主要なポイントと注意点** を説明します。

### パッケージ名を変更する

パッケージ名（アプリ ID）の変更は Android Studio に一般的な方法でおこなわれますが、以下に作業の流れを要約します。

1. Android Studio でサンプルアプリを開きます。
2. プロジェクトエクスプローラーを開きます。
3. app ディレクトリを右クリックし、Open Module Settings を選択します。
4. Module Settings 画面に表示されている Flavors タブを開きます。
5. Flavors タブ内の Application ID の項目が net.ec_cube.eccube_Android となっている部分を、希望するパッケージ名へ変更します。

上記の手順でパッケージ名を変更するかぎり、プロジェクト内において変更すべきファイルは **すべて自動的に変更されるはず** ですが、もし変更が上手くいかない場合、手動で以下の作業を行って下さい。

1. 物理的なプロジェクトのディレクトリである eccube-Android のディレクトリ名を変更します。
2. プロジェクト直下にある eccube-Android.iml のファイル名を変更します。
3. プロジェクト直下にある .idea ディレクトリの下の module.xml と workspace.xml の中において eccube-Android と記述されている部分をすべて変更します。
4. プロジェクト直下にある .name ファイルに eccube-Android という記述を変更します。

### ウェブサイトを設定する

アプリがアクセスする店舗（アプリが表示する）のウェブサイトの URL を設定します。

1. net/ec_cube/eccube_Android/Config.java を開きます。
2. BASE_URL にすでに設定されている www.ec-cube.net を店舗のウェブサイトの URL に書き換えます。


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

本アプリは[「Appiaries プッシュ通知プラグイン」](http://www.ec-cube.net/products/detail.php?product_id=) によって配信されるプッシュ通知を受信するように実装されています。したがってアピアリーズとの連携の設定が必要です。もちろん、プッシュ通知の配信や受信は他のサービスを利用して実装することも可能です。

GCM とアピアリーズに登録し、それぞれの情報をプラグインに設定する方法については[「Appiaries プッシュ通知プラグイン」](http://www.ec-cube.net/products/detail.php?product_id=) をインストールした後でプラグインの「設定」ページに詳細がありますのでそちらを参照して下さい。  
プラグインの設定の完了後、アプリがアピアリーズと連携するための値は net/ec_cube/eccube_Android/Config.java に記述されます。

1. Config.java を開きます。
2. GCM の Developer Console に表示される**「Project Number」**を SENDER_ID 設定します。
3. アピアリーズで契約した**「データストア ID」**を DATASTORE_ID 設定します。
4. アピアリーズで登録した**「アプリ ID」**を APPLICATION_ID 設定します。
5. アピアリーズで登録したアプリの**「アプリトークン」**を APPLICATION_TOKEN 設定します。

### keystoreを設定する

プロジェクトをビルドするには keystore が必要です。keystore は GCM 側で設定したものを使用します。keystore の作成については一般的な方法でおこなって下さい。以下は、すでに keystore が作成されているものとし、keystore を本アプリに設定するための手順の一つの例です。

1. プロジェクト直下に keystore ディレクトリを作成し（ディレクトリ名は任意です）、keystore ファイルを置きます。
2. Android Studio を起動し、アプリを開きます。
3. プロジェクトエクスプローラーを開きます。
4. app フォルダを右クリックし、Open Module Settings を選択します。
5. Module Settings 画面に表示されている Flavors タブを開きます。
6. Signing タブ内の**「Key Alias」**、**「Key Password」**、**「Store File」**、**「Store Password」**をそれぞれ設定します。

## お問い合わせ

EC-CUBE お問い合わせ窓口は [こちら]() です。
