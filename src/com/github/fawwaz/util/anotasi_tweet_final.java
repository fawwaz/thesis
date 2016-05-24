/*
 * File ini adalah object mapping dari tabel anotasi_tweet_final di dalam database.
 */
package com.github.fawwaz.util;

/**
 *
 * @author Fawwaz Muhammad
 */
public class anotasi_tweet_final {

    public Integer sequence_num;
    public String twitter_tweet_id;
    public String token;
    public boolean isnumber;
    public boolean isplacedirective;
    public boolean istimedirective;
    public boolean isurl;
    public boolean ismonthname;
    public String label;
    public String guessed_rule;

    public anotasi_tweet_final() {
        super();
    }

    public anotasi_tweet_final(Integer sequence_num, String twitter_tweet_id,
            String token, String label) {
        super();
        this.sequence_num = sequence_num;
        this.twitter_tweet_id = twitter_tweet_id;
        this.token = token;
        this.label = label;
    }

    public anotasi_tweet_final(Integer sequence_num, String twitter_tweet_id,
            String token, boolean isnumber, boolean isplacedirective,
            boolean istimedirective, boolean isurl, boolean ismonthname,
            String label, String guessed_rule) {
        super();
        this.sequence_num = sequence_num;
        this.twitter_tweet_id = twitter_tweet_id;
        this.token = token;
        this.isnumber = isnumber;
        this.isplacedirective = isplacedirective;
        this.istimedirective = istimedirective;
        this.isurl = isurl;
        this.ismonthname = ismonthname;
        this.label = label;
        this.guessed_rule = guessed_rule;
    }

}
