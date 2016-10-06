# socialite-android

Social plugins are used widely in the majority of services, including mobile applications. Some are essential like social sharing, and some - like social login - are becoming more and more trusted and used.

The **socialite** library provides a set of those plugins. Currently, it covers several social login, sharing and fetching share counts, detailed below.
 
#### Setup

Add the gradle dependency to your module

```xml
compile 'com.vinelab.android.socialite:socialite:1.1.8'
```

In case an error appeared related to Fabric, add the following maven repository.

```xml
maven { url 'https://maven.fabric.io/repo' }
```

## Login

For quick login implementations, it's faster to use Facebook for example, instead of developing a webservice that provides an email registration/login solution. 

Nowadays social login is trusted by the user. Considering the same example, a normal user finds it's normal to log in using his Facebook account (usually authenticated on his device) to access an application. He might check the permissions page for a second before proceeding.

So for us devs, we often use classes we have written previously to integrate the social login behavior in a new project. We check for any SDK updates before doing so. We decided to share a simple integration that simplify this process. The library currently covers Facebook and Twitter logins. It uses a unified callbacks interface for any social provider to be introduced in the future. Later on we will be adding the Google+ option.

### Facebook Login

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

To use any Facebook functionality in the library, you have to trigger the **FacebookConfig** class to initialize the SDK. 

Once set, trigger the login request when ready. **LoginActivity** showcases a simple implementation. First check if the user already has an active session, and request to login if that's not the case:
 
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

That's it! Just remember to forward the login results in *onActivityResult* to the provider.

### Twitter Login

Almost a year ago, Twitter has introduced [Fabric](https://get.fabric.io/). The devs can now integrate the Twitter SDK to login, tweet, fetch a timeline, etc. In our case, we will be using the TwitterCore kit, providing auth functionality.

Just like the previous integration, few steps are required in order to implement Twitter login:

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

To use any Twitter functionality in the library, you have to trigger the **TwitterConfig** class to initialize the SDK. 

Once set, the same Facebook flow is applied. Don't forget to forward the login results in *onActivityResult* to the provider.

One issue confronted with the Twitter SDK is the cancel callback. When the user hits the cancel button on the authorization page, the SDK treats this result as an error. We tried to find a workaround for it, like intercepting the result in **onActivityResult**, but we found no luck.

## Sharing

The library provides an interface for sharing callbacks. This is only used with Facebook and Twitter, because theirs SDKs offer such behavior. Don't forget to forward the share results in *onActivityResult* to the providers.

For Facebook and Twitter, the steps to integrate the SDKs and put them to work, mentioned previously, are required at first.

### Facebook Sharing

Triggering the share provider will request the SDK to open the ShareDialog, as follows:

```java
FacebookShareProvider.getInstance().postOnMeFeed(activity, contentUrl, contentTitle, imageUrl, contentDescription, shareListener);
```

Sharing on Messenger will open the MessageDialog. It's triggerd as follows:

```java
FacebookShareProvider.getInstance().sendMessage(activity, contentUrl, contentTitle, imageUrl, contentDescription, shareListener);
```

If Facebook is not installed, the SDK will display a web dialog for the user to authenticate before sharing. 

The callbacks on Android vary depending on few criteria:

1. If the user didn't give your Facebook-app any permissions, cancelling the operation will trigger the success flag. The post ID won't be returned also when the operation is successful.

2. If the user gave your Facebook-app the basic permissions, all 3 flags will be triggered correctly, but the post ID won't on success.

3. If the user gave your Facebook-app extra `publish_actions` permission, the post ID will be returned after a successful sharing.

### Twitter Sharing

First, the Composer kit dependency is required: 

```
compile('com.twitter.sdk.android:tweet-composer:1.0.1@aar') {
        transitive = true;
    }
```

Sharing on Twitter is triggered simply as follows:

```java
TwitterShareProvider.getInstance().composeTweet(activity, contentTitle + " " + contentUrl, shareListener);
```

If Twitter is not installed, the user will be redirected to a web page to authenticate before sharing. The drawback of this is that the user will be stuck on the browser without being redirected back to your app.

One other drawback, it's impossible to know if a tweet was posted or not. Twitter shares the tweet asynchronously, and thus returns a success flag once the user hits on *Tweet*. Only the cancel flag is guaranteed. 

### Whatsapp Sharing

Whatsapp doesn't offer any SDK for Android. Sharing via Whatsapp is done by requesting the system to do it. There will be no callbacks integrated like the Facebook and Twitter case. The app will be notified if the Whatsapp app was opened, or it's not installed.

```java
if(!WhatsappShareProvider.shareMessage(activity, message)) {
    // inform user that it's not installed
}
```

### Email Sharing

This is one of the basics sharing. It's also done by requesting the system to handle it.

```java
EmailShareProvider.shareMessage(activity, dialogTitle, subject, body);
```

## Social Share Count

Many of you are familiar with a counter next to share buttons part of an article page for example. Those numbers are provided directly from the social networks.

In our case, we need the share counts (of a certain web link) on both Facebook and Twitter. For now, we're fetching the data directly from their APIs.

The count will be returned in the [SocialiteShareCount](https://github.com/Vinelab/socialite-android/blob/master/socialite/src/main/java/com/vinelab/android/socialite/sharing/SocialiteShareCount.java) object, containing the link and the sharing count returned from the API.

#### Facebook 

```java
SocialiteShareCount countFacebook = FacebookShareCountProvider.getShareCount(link);
```

Note that it is planned to fetch the data from the SDK first if installed in the next versions.

#### Twitter

```java
SocialiteShareCount countTwitter = TwitterShareCountProvider.getShareCount(link);
```

The Twitter SDK has no support for fetching such count.