# @ibigtree/react-native-kakao-login

React Native를 위한 카카오 로그인 라이브러리. [@react-native-seoul/kakao-login](https://github.com/react-native-seoul/react-native-kakao-login)와 비슷하나 다음 차이점이 있습니다.

* 별도 다이얼로그 없이 바로 카카오톡 로그인으로 연결됩니다.
* 현재 accessToken, refreshToken 발급 외 다른 기능은 없습니다.

지원 기능

## Getting started

`$ npm install @ibigtree/react-native-kakao-login --save`


### 공통

[Kakao Developers](https://developers.kakao.com) 에서 앱 생성 및 사용할 플랫폼 등록을 먼저 진행해야 합니다.

### iOS

등록한 앱의 Bundle Identifier와 프로젝트의 값이 일치해야 정상 동작합니다.


Info.plist 에 다음 내용 추가 ({APP_KEY} 는 사용할 앱의 Native App Key로 대체)

```plist
<dict>
    <!-- Native App Key --!>
	<key>KAKAO_APP_KEY</key>
	<string>{APP_KEY}</string>

    <!-- Bundle URL Scheme --!
	<key>CFBundleURLTypes</key>
	<array>
		<dict>
			<key>CFBundleURLSchemes</key>
			<array>
				<string>kakao{APP_KEY}</string>
			</array>
		</dict>
	</array>

	<key>LSApplicationQueriesSchemes</key>
	<array>
        <!-- 공통 -->
		<string>kakao{APP_KEY}</string>

        <!-- 간편로그인 -->
		<string>kakaokompassauth</string>
		<string>storykompassauth</string>

        <!-- 카카오톡링크 -->
		<string>kakaolink</string>
		<string>kakaotalk-5.9.7</string>

        <!-- 카카오스토리링크 -->
		<string>storylink</string>
	</array>
</dict>
```

AppDelegate.m에 다음 내용 추가

```objective-c
/*
 Deprecated(iOS 4.2-9.0)
 https://developer.apple.com/documentation/uikit/uiapplicationdelegate/1623073-application?language=objc
 */
- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation
{
  if ([KOSession isKakaoAccountLoginCallback:url]) {
    return [KOSession handleOpenURL:url];
  }

  return NO;
}

- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url options:(NSDictionary<NSString *,id> *)options
{
  if ([KOSession isKakaoAccountLoginCallback:url]) {
    return [KOSession handleOpenURL:url];
  }
  return NO;
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
  [KOSession handleDidBecomeActive];
}
@end
```

### Android

등록한 앱의 서명 Key Hash가 일치해야 정상 작동 합니다.

React Native 기본 Debug Key Hash:
```
Xo8WBi6jzSxKDVR4drqm84yr9iU=
```

android/build.gradle에 다음 내용 추가

```groovy
allprojects {
    repositories {
        // ...
        maven { url 'https://devrepo.kakao.com/nexus/content/groups/public/' }
    }
}
```

AndroidManifest.xml에 다음 내용 추가 ({APP_KEY} 는 사용할 앱의 Native App Key로 대체)

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <application>
+        <meta-data android:name="com.kakao.sdk.AppKey" android:value="{APP_KEY}" />
    </application>
</manifest>
```

## Usage
```javascript
import KakaoLogin from 'react-native-kakao-login';

async function processLogin() {
    try {
        const tokenInfo = await KakaoLogin.login();
        console.log(tokenInfo.accessToken);
        await KakaoLogin.logout();
    } catch (error) {
        if (error.code === 'E_CANCELLED') {
            // 사용자가 로그인을 취소함
        }

        // 기타 정의되지 않은 에러 (E_UNKNOWN)
    }
}
```
