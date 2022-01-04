import { WebPlugin } from '@capacitor/core';

import type { SpeakerPhonePlugin } from './definitions';

export class SpeakerPhoneWeb extends WebPlugin implements SpeakerPhonePlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
