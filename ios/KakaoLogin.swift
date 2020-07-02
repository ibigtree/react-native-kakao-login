import Foundation
import KakaoOpenSDK

@objc(KakaoLogin)
class KakaoLogin: NSObject, RCTBridgeModule {
    static func moduleName() -> String! {
        return "KakaoLogin"
    }
    
    static func requiresMainQueueSetup() -> Bool {
        return false
    }
    
    @objc(login:rejecter:)
    func login(resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
        if let session = KOSession.shared() {
            session.close()
            
            DispatchQueue.main.async {
                session.open(completionHandler: { (error) in
                    switch (error) {
                    case .some(let error as NSError):
                        var errorCode = "E_UNKNOWN"
                        
                        if (error.code == KOErrorCancelled.rawValue) {
                            errorCode = "E_CANCELLED"
                        }
                        
                        reject(errorCode, error.localizedDescription, error)
                        
                    case .none:
                        if let token = session.token {
                            resolve([
                                "accessToken": token.accessToken,
                                "refreshToken": token.refreshToken,
                            ])
                        }
                    }
                }, authTypes: [NSNumber(value: KOAuthType.talk.rawValue)])
            }
        }
    }
    
    @objc(logout:rejecter:)
    func logout(resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
        if let session = KOSession.shared() {
            session.close()
            
            session.logoutAndClose { (success, error) in
                switch (error) {
                case .some(let error as NSError):
                    reject("\(error.code)", error.localizedDescription, error)
                    
                case .none:
                    resolve(success)
                }
            }
        }
    }
}
