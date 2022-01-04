export interface SpeakerPhonePlugin {
  setSpeakerphoneOn(options: { value: boolean}): Promise<{value:boolean}>;
}
