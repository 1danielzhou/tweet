package com.daniel.tweet.model;

import com.daniel.tweet.domain.TweetBaseContent;
import com.daniel.tweet.domain.TweetRelationMention;
import com.daniel.tweet.domain.TweetRelationPostView;
import com.daniel.tweet.domain.TweetRelationTopic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class TweetContent {
    private TweetBaseContent tweetBaseContent;
    private List<TweetRelationTopic> tweetTopics;
    private List<TweetRelationMention> tweetMentions;
    private List<TweetRelationPostView> postViews;

    public TweetContent() {
        this.tweetBaseContent = new TweetBaseContent();
        this.tweetTopics = new ArrayList<>();
        this.tweetMentions = new ArrayList<>();
        this.postViews = new ArrayList<>();
    }
}