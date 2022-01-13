export interface SpeakerPhonePlugin {
  requestPermissions():Promise<{result : string}>;
  setRegNumber(option:{number:Array<string>}):Promise<{result:string}>
}
