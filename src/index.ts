import { registerPlugin } from '@capacitor/core';

import type { SpeakerPhonePlugin } from './definitions';

const SpeakerPhone = registerPlugin<SpeakerPhonePlugin>('SpeakerPhone', {
  web: () => import('./web').then(m => new m.SpeakerPhoneWeb()),
});

export * from './definitions';
export { SpeakerPhone };
