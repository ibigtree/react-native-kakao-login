export interface KakaoAuthTokenInfo {
    accessToken: string;
    refreshToken: string;
}

interface KakaoLogin {
    login(): Promise<KakaoAuthTokenInfo>;
    logout(): Promise<boolean>;
}

export default KakaoLogin;
