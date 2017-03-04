# app_android
Afrika Burn Android App

The Afrika Burn app is an opensource project for ongoing development of mobile systems. Providing a mobile interface to burners onsite, giving them a map of roads, camps, artworks, mutant vehicles, performances etc. with corresponding information. AfrikaBurn is a Regional Burning Man festival and community.

You can build the app without the keystore file. If you need to build a release version, get the keystore file from the vault and follow [this gist](https://gist.github.com/maiatoday/2df1e24224b9def4fa0b11cd2d5a6ff6) to set it up. Use AB for the app indicator instead of APP because this what is in the gradle file.

# Debug
## How to debug with Realm and Stetho
1. Open chrome and go to chrome://inspect
2. Find the emulator and the AfrikaBurn powered by Stetho link
3. click the ling and open WebSQL and then default.realm 

## How to check if the background task to fetch data has run
` adb shell dumpsys activity service GcmService --endpoints DataFetchService
`
