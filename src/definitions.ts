export interface SpeakerPhonePlugin {
  requestPermissions():Promise<void>;
  requestPhoneScreening():Promise<void>;
}
