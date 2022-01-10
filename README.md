# capacitor-auto-speakerphone

**필요 권한**

AndroidManifest.xml 에 다음과 같이 권한 입력 필요
```xml
<!-- 스피커폰으로 변경위해 필요-->
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />

<!-- 전화착신 상태 읽어오기 위해 필요 -->
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>

<!-- 전화번호 읽어오기 위해 필요 -->
<uses-permission android:name="android.permission.READ_CALL_LOG"/> 

```

## Install

```bash
npm install capacitor-auto-speakerphone
npx cap sync
```

## API

<docgen-index>

* [`requestPermissions()`](#requestpermissions) : 필요 권한들을 요구 (오디오설정, 착신상태 읽어오기 등)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### requestPermissions()

```typescript
requestPermissions() => Promise<void>
```

--------------------

</docgen-api>
