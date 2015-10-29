# socialite-android

Social login has become essential in most applications. For quick login implementations, it's faster to use Facebook for example instead of implementing a user credentials registration/login solution. 

Nowadays social login is trusted by the user. Considering the same example, a normal user finds it's normal to log in using his Facebook account (already authenticated on his device) to access an application. He might check the permissions page for a second before proceeding.

So for us devs, we often use classes we have written previously to integrate the social login behavior in a new app. We check before for any SDK updates before doing so. So we decided to share a simple integration that simplify this process.

For now let's start with Facebook and Twitter. 

### Facebook

The Facebook SDK is moving fast, and the techies behind it are working hard to provide the devs with better integration. We're now dealing with version 4.7 and the login integration cannot be more simple than this.

Before checking the solution, few steps are needed:

1. Create a Facebook application and add it to the values or strings xml file of the project.

2. Generate a Hashkey from the machine used in development, and add the Hashkey to the Settings console of the Facebook application.

3. Add the Facebook SDK dependency in the gradle.   
    ```
    compile 'com.facebook.android:facebook-android-sdk:4.7.0'    
    ```

4. Add the internet permission and the meta-data definition to the manifest file. In the Login (or Sharing) case, add also the definition of the Facebook activity.

```xml
<meta-data
	android:name="com.facebook.sdk.ApplicationId"
    android:value="@string/facebook_app_id" />

<activity
	android:name="com.facebook.FacebookActivity"
	android:configChanges="keyboard|keyboardHidden|screenLayout|screenSize|orientation"
	android:label="@string/app_name"
	android:theme="@android:style/Theme.Translucent.NoTitleBar" />
```

Once set, initialize the Facebook provider, and trigger the login request when ready. The **LoginActivity** shows a simple implementation for Facebook login. First check if the user already has an active session, and request to login if that's not the case:
 
        FacebookLoginProvider.getInstance().isUserLoggedIn(new SocialiteUserStateListener() {
            @Override
            public void onLoggedIn(SOCIALITE_PROVIDER provider, SocialiteCredentials credentials) {
                loginListener.onSuccess(provider, credentials);
            }

            @Override
            public void onLoggedOut(SOCIALITE_PROVIDER provider) {
                FacebookLoginProvider.getInstance().logInWithReadPermissions(LoginActivity.this, null, loginListener);
            }
        });

That's it! Just remember to forward the login results in **onActivityResult** to the provider.

### Twitter

Almost a year ago, Twitter has introduced [Fabric](https://get.fabric.io/). The devs can now integrate the Twitter SDK to login, tweet, fetch a timeline, etc. In our case, we will be using the TwitterCore kit, providing the login functionality.

Just like the previous integration, few steps are required before in order to implement Twitter login:

1. Create a Twitter application (https://apps.twitter.com). Configure the needed permissions. Allow the application to be used to Sign in with Twitter. Add the consumer key and secret to the values or strings xml file of the project.

2. Add the Twitter dependency to the gradle file. 

```
repositories {
    mavenCentral()
    maven { url 'https://maven.fabric.io/repo' }
}

dependencies {
    compile('com.twitter.sdk.android:twitter-core:1.6.0@aar') {
        transitive = true;
    }
}
```

Once set, the same Facebook flow is applied. Don't forget to forward the login results in **onActivityResult** to the provider.

One issue confronted with the Twitter SDK is the cancel callback. When the user hits the cancel button on the authorization page, the SDK treats this result as an error. We tried to find a workaround for it, like intercepting the result in **onActivityResult**, but we found no luck.

### Final Words

This implementation uses a unified callbacks interface for any social provider to be introduced in the future. Later on we will be adding the Google+ option.
I must admit that this [repository](https://github.com/andrebts/login-basics) inspired us to push this solution! We moved away from using the custom buttons provided by the SDK though, and implemented the wrapped functionality.
