/* eslint-disable no-alert */
import React from 'react';
import {Button, SafeAreaView} from 'react-native';
import KakaoLogin from '@ibigtree/react-native-kakao-login';

function App() {
  return (
    <SafeAreaView>
      <Button
        title="Login"
        onPress={async () => {
          try {
            const result = await KakaoLogin.login();
            alert(`Login Success\nAccess Token: ${result.accessToken}`);
          } catch (error) {
            if (error.code === 'E_CANCELLED') {
              alert('User Cancelled');
            } else {
              console.error(error);
            }
          }
        }}
      />
    </SafeAreaView>
  );
}

export default App;
