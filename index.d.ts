declare module '@ibigtree/react-native-kakao-login' {
    export interface KakaoAuthTokenInfo {
        accessToken: string;
        refreshToken: string;
    }

    namespace KakaoLogin {
        function login(): Promise<KakaoAuthTokenInfo>;
        function logout(): Promise<boolean>;
    }

    export default KakaoLogin;
}