# capacitor-auto-speakerphone

**필요 권한**

`Manifest.permission.MODIFY_AUDIO_SETTINGS`

`Manifest.permission.READ_PHONE_STATE`


AndroidManifest.xml 에 다음과 같이 권한 입력 필요
```xml
 <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>
 <uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

## Install

```bash
npm install capacitor-auto-speakerphone
npx cap sync
```

## API

<docgen-index>

* [`setSpeakerphoneOn(options {value : boolean})`](#setspeakerphoneon) : 전화 받은 후 스피커폰 전환 설정

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### setSpeakerphoneOn(...)

```typescript
setSpeakerphoneOn(options: { value: boolean; }) => Promise<{ value: boolean; }>
```

| Param         | Type                             |
| ------------- | -------------------------------- |
| **`options`** | <code>{ value: boolean; }</code> |
value : True 일 경우 스피커폰 상태 on

value : False 일 경우 스피커폰 상태 off

**Returns:** <code>Promise&lt;{ value: boolean; }&gt;</code>

--------------------

</docgen-api>
