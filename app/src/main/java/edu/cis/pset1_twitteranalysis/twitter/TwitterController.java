package edu.cis.pset1_twitteranalysis.twitter;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.io.PrintStream;

public class TwitterController
{
    private String tweetFields =
        "attachments,author_id,context_annotations,conversation_id,created_at,edit_controls,entities," +
                "geo,id,in_reply_to_user_id,lang,public_metrics,possibly_sensitive,referenced_tweets," +
                "reply_settings,source,text,withheld";
    private String tweetFieldsFull =
        "attachments,author_id,context_annotations,conversation_id,created_at,edit_controls,entities," +
                "geo,id,in_reply_to_user_id,lang,public_metrics,possibly_sensitive,referenced_tweets," +
                "reply_settings,source,text,withheld,non_public_metrics,organic_metrics,promoted_metrics";


    private Twitter twitter;
    private TwitterV2 twitterV2;
    private ArrayList<Status> statuses;
    private ArrayList<String> tokens;
    private HashMap<String, Integer> wordCounts;
    ArrayList<String> commonWords;
    private String popularWord;
    private int frequencyMax;
    Context context;

    public TwitterController(Context currContext)
    {
        context = currContext;

        ConfigurationBuilder cb =  new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("***")
                .setOAuthConsumerSecret("***")
                .setOAuthAccessToken("***")
                .setOAuthAccessTokenSecret("***");
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
        twitterV2 = TwitterV2ExKt.getV2(twitter);

        statuses = new ArrayList<Status>();
        tokens = new ArrayList<String>();
        wordCounts = new HashMap<>();
        commonWords = new ArrayList<String>();
        getCommonWords();
    }

    /********** PART 1 *********/

    //can be used to get common words from the commonWords txt file
    public void getCommonWords() {

        try {
            AssetManager am = context.getAssets();

            //this file can be found in src/main/assets
            InputStream myFile = am.open("commonWords.txt");
            Scanner sc = new Scanner(myFile);
            while (sc.hasNextLine()) {
                commonWords.add(sc.nextLine());
            }
        } catch (Exception err) {
            Log.d("COMMON_WORDS", err.toString());
        }
    }

    public String postTweet(String message)
    {
        String statusTextToReturn = "";
        try
        {
            Log.d("", "Posting tweet");
            // Refer to createTweet method:
            // https://github.com/takke/twitter4j-v2/blob/master/twitter4j-v2-support/src/main/kotlin/twitter4j/TwitterV2.kt
            twitterV2.createTweet(null, null, null, null, null, null,
                null, null, null, null, null, message);
            Log.d("", "Tweet posted!");
            statusTextToReturn = message;
        }
        catch (TwitterException e){
            Log.d("", e.getErrorMessage());
            System.out.println(e.getErrorMessage());
        }
        return statusTextToReturn;
    }

    private void fetchTweets(String handle)
    {
        String[] handleArr = {handle};
        try
        {
            /*
            * getUsersBy requires special access to twitter's V2 endpoints which is only accessible
            * with the paid version
            * */
            UsersResponse users = twitterV2.getUsersBy(handleArr,tweetFields, tweetFieldsFull,"pinned_tweet_id");
            // TODO: Test fetch tweets (code below is not tested due to no access to V2 endpoints)
            List<User2> usersList = users.getUsers();
            User2 user = usersList.get(0);
            long userID = user.getId();

            TweetsResponse tweets = twitterV2.getUserTweets(
                    userID, null, null, null, null, null, null,
                    null, null, null, null, null, null, null);
            int tweetsSize = tweets.getTweets().size();
            Log.d("", "Number of tweets: " + tweetsSize);
        }
        catch (Exception e)
        {
            Log.d("", e.toString());
        }
    }

    /********** PART 2 *********/

    /*
     * TODO 2: this method splits a whole status into different words. Each word
     * is considered a token. Store each token in the "tokens" arrayList
     * provided. Loop through the "statuses" ArrayList.
     */
    private void splitIntoWords()
    {
        
    }


    /*
     * TODO 3: return a word after removing any punctuation and turn to lowercase from it.
     * If the word is "Adam!!", this method should return "adam".
     * We'll need this method later on.
     * If the word is a common word, return null
     */
    @SuppressWarnings("unchecked")
    private String cleanOneWord(String word)
    {
        return "";
    }


    /*
     * TODO 4: loop through each word, get a clean version of each word
     * and save the list with only clean words.
     */
    @SuppressWarnings("unchecked")
    private void createListOfCleanWords()
    {

    }

    /*
     * TODO 5: count each clean word using. Use the frequentWords Hashmap.
     */
    @SuppressWarnings("unchecked")
    private void countAllWords()
    {

    }


    //TODO 6: return the most frequent word's string in any appropriate format
    @SuppressWarnings("unchecked")
    public String getTopWord()
    {
        return "";

    }

    //TODO 7: return the most frequent word's count as an integer.
    @SuppressWarnings("unchecked")
    public int getTopWordCount()
    {
        return 0;
    }


    public String findUserStats(String handle)
    {
        /*
         * TODO 8: you put it all together here. Call the functions you
         * finished in TODO's 2-7. They have to be in the correct order for the
         * program to work.
         * Remember to use .clear() method on collections so that
         * consecutive requests don't count words from previous requests.
         * See flowchart for order
         */

        fetchTweets(handle);

        return "";
    }

    /*********** PART 3 **********/

    //TODO 9: Create your own method.

    // Example: A method that returns 100 tweets from keyword(s).
    public List<Status> searchKeywords(String keywords)
    {
        //Use the Query object from Twitter
        Query query = new Query(keywords);
        query.setCount(100);
        query.setSince("2015-12-1");

        //create an ArrayList to store results, which will be of type Status
        List<Status> searchResults = new ArrayList<>();
        try
        {
            //we try to get the results from twitter
            QueryResult result = twitter.search(query);
            searchResults = result.getTweets();
        }
        catch (TwitterException e)
        {
            //if an error happens, like the connection is interrupted,
            //we print the error and return an empty ArrayList
            e.printStackTrace();
        }
        return searchResults;
    }
}
