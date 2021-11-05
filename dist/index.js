import {NativeModules} from "react-native";

const {KakaoLogin} = NativeModules;

export type KakaoAuthTokenInfo = {
  accessToken: string,
  refreshToken: string,
};

async function login(): Promise<KakaoAuthTokenInfo> {
  return await KakaoLogin.login();
}

async function logout(): Promise<boolean> {
  return await KakaoLogin.logout();
}

export default {login, logout};
