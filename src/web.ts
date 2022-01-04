import { WebPlugin } from '@capacitor/core';

import type { SpeakerPhonePlugin } from './definitions';

export class SpeakerPhoneWeb extends WebPlugin implements SpeakerPhonePlugin {
  async setSpeakerphoneOn(options: { value: boolean }): Promise<{ value: boolean }> {
    return options;
  }
}
