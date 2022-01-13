import { WebPlugin } from '@capacitor/core';

import type { SpeakerPhonePlugin } from './definitions';

export class SpeakerPhoneWeb extends WebPlugin implements SpeakerPhonePlugin {
  async requestPermissions(): Promise<{ result: string }> {
    return new Promise<{result:string}>(resolve => {
      return resolve
    }).catch(reason => {
      return reason;
    })
  }

  setRegNumber(_option:{number:Array<string>}): Promise<{ result: string }> {
    return new Promise<{result : string}>(
        resolve=>{
          return resolve;
        }
    ).catch(reason => {
      return reason;
    });
  }
}
