# capacitor-auto-speakerphone

**필요 권한**

AndroidManifest.xml 에 다음과 같이 권한 입력 필요
```xml
<!-- 스피커폰으로 변경위해 필요-->
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
<!-- 전화착신 상태, 전화번호 읽어오기 위해 필요 -->
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>

<!-- API 30부터 전화번호를 읽는 권한은 이 권한으로 대체 -->
<uses-permission android:name="android.permission.READ_PHONE_NUMBERS" />
```

## Install

```bash
npm install capacitor-auto-speakerphone
npx cap sync
```

## API

<docgen-index>

* [`requestPermissions()`](#requestpermissions)
* [`requestPhoneScreening()`](#requestphonescreening) : API 29 부터 수신 전화번호를 읽는 방법이 바뀌어서, 수신 번호 읽을시 해당 권한필요

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### requestPermissions()

```typescript
requestPermissions() => Promise<void>
```

--------------------


### requestPhoneScreening()

```typescript
requestPhoneScreening() => Promise<void>
```

--------------------

</docgen-api>
