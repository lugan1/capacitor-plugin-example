export interface SpeakerPhonePlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
