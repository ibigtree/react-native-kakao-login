export interface KakaoAuthTokenInfo {
    accessToken: string;
    refreshToken: string;
}
interface KakaoLogin {
    login(): Promise<KakaoAuthTokenInfo>;
    logout(): Promise<boolean>;
}

declare const KakaoLogin: KakaoLogin;

export default KakaoLogin;
