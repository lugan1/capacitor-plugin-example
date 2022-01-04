import Foundation

@objc public class SpeakerPhone: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
