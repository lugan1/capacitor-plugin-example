import { WebPlugin } from '@capacitor/core';

import type { SpeakerPhonePlugin } from './definitions';

export class SpeakerPhoneWeb extends WebPlugin implements SpeakerPhonePlugin {
  async requestPermissions(): Promise<void>{}
  async requestPhoneScreening(): Promise<void> {}
}
